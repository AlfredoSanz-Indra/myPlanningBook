package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.usecaseapi.OwnerService
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService
import com.alfred.myplanningbook.domain.usecaseapi.StateService

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class StateServiceImpl( private val ownerService: OwnerService,
                        private val planningBookService: PlanningBookService): StateService {

    override suspend fun loadState(email: String): SimpleResponse {

        var result = SimpleResponse(false, 400, "notLoaded", "")
        Klog.line("StateServiceImpl", "loadState", "loading user state -> email: $email")

        try {
            val respLO = ownerService.getOwner(email)
            if(respLO.result) {
                result = SimpleResponse(true, 200, "owner found", "")
                AppState.owner = respLO.owner!!
                result.owner = respLO.owner

                if(!respLO.owner!!.activePlanningBook.isNullOrBlank()) {
                    Klog.linedbg("StateServiceImpl", "loadState", "loading active PB -> respLO.owner!!.activePlanningBook!!: ${respLO.owner!!.activePlanningBook!!}")
                    val respPB = planningBookService.getPlanningBook(respLO.owner!!.activePlanningBook!!)
                    if(respPB.result) {
                        AppState.activePlanningBook = respPB.planningBook!!
                        result.planningBook = respPB.planningBook
                        Klog.linedbg("StateServiceImpl", "loadState", "Active PB loaded-> result: $result")
                    }
                }

                if(respLO.owner!!.planningBooks!!.isNotEmpty()) {
                    Klog.linedbg("StateServiceImpl", "loadState", "loaded PB -> loading owner planningBooks")
                    respLO.owner!!.planningBooks!!.forEach { it ->
                        val respPB = planningBookService.getPlanningBook(it)
                        if(respPB.result) {
                            AppState.planningBooks.add(respPB.planningBook!!)
                        }
                    }
                }
                Klog.linedbg("StateServiceImpl", "loadState", "loading state all done -> result: $result")
            }
            else if(respLO.code == 400 || respLO.code == 404) {
                Klog.linedbg("StateServiceImpl", "loadState", "Owner didn't found -> respLO: $respLO")
                val respCO = ownerService.createOwner(email)
                if(respCO.result) {
                    AppState.owner = respCO.owner!!
                    result = SimpleResponse(true, 200, "OK", "")
                    result.owner = respCO.owner
                    Klog.linedbg("StateServiceImpl", "loadState", "Owner created -> result: $result")
                }
                else {
                    result = respCO
                    Klog.linedbg("StateServiceImpl", "loadState", "Owner couldn't be created -> result: $result")
                }
            }
        }
        catch(e: Exception) {
            Klog.line("StateServiceImpl", "loadState", " Exception Message: ${e.message}")
            Klog.stackTrace("StateServiceImpl", "loadState", e.stackTrace)

            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.line("StateServiceImpl", "loadState", "result: $result")
        return result
    }

    override suspend fun updateState_activePlanningBook(pbID: String, ownerID: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("StateServiceImpl", "updateState_activePlanningBook", "updating state planningBook active -> pbID: $pbID")

        try {
            val resp = ownerService.updateOwnerActivePlanningBook(pbID, ownerID)
            if(resp.result && resp.code == 200) {
                AppState.owner!!.activePlanningBook = pbID

                val resp2 = planningBookService.getPlanningBook(pbID)
                if(resp2.result && resp2.code == 200) {
                    AppState.activePlanningBook = resp2.planningBook
                    result = SimpleResponse(true, 200, "updated", "")
                    result.planningBook = resp2.planningBook
                }
                else {
                    result = resp2
                }
            }
            else{
                result = resp
            }
        }
        catch(e: Exception) {
            Klog.line("StateServiceImpl", "updateState_activePlanningBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("StateServiceImpl", "updateState_activePlanningBook", "updated Active PB -> result: $result")
        return result
    }

    /**
     * Forget an Owner planningBook and update the State
     */
    override suspend fun forgetSharedPlanningBook(pbID: String): SimpleResponse {

        var result = SimpleResponse(true, 200, "forgotten", "")
        Klog.line("StateServiceImpl", "forgetSharedPlanningBook", "Forgetting a shared planningBook, pbID: $pbID")

        try {
            //**FORGET OWNER PB
            val resp = ownerService.forgetSharedPlanningBook(pbID)
            if(!resp.result || resp.code != 200) {
                return resp
            }

            //**UPDATE OWNER ACTIVE PB
            if(AppState.activePlanningBook != null && AppState.activePlanningBook!!.id == pbID) {
                if(AppState.owner!!.planningBooks!!.isNotEmpty()) {
                    val id: String = AppState.owner!!.planningBooks!!.first()
                    val pb: PlanningBook? = AppState.planningBooks.find { it.id == id }
                    if(pb != null) {
                        result = updateState_activePlanningBook(pb.id, AppState.owner!!.id)
                    }
                    AppState.activePlanningBook = pb
                }
                else {
                    AppState.activePlanningBook = null
                }
            }
            Klog.linedbg("StateServiceImpl", "forgetSharedPlanningBook", "AppState -> $AppState")

            //**UPDATE THE AppState PLANNINGBOOK LIST
            if(AppState.owner!!.planningBooks!!.isNotEmpty()) {
                Klog.linedbg("StateServiceImpl", "forgetSharedPlanningBook", "reloading owner planningBooks")
                val newPBList: MutableList<PlanningBook> = mutableListOf()

                AppState.owner!!.planningBooks!!.forEach { it ->
                    val respPB = planningBookService.getPlanningBook(it)
                    if(respPB.result) {
                        newPBList.add(respPB.planningBook!!)
                    }
                    else {
                        result = respPB
                    }
                }
                AppState.planningBooks = newPBList
                Klog.linedbg("StateServiceImpl", "forgetSharedPlanningBook", "newPBList -> $newPBList")
            }
            Klog.linedbg("StateServiceImpl", "forgetSharedPlanningBook", "State completely updated")
        }
        catch(e: Exception) {
            Klog.line("StateServiceImpl", "forgetSharedPlanningBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("StateServiceImpl", "forgetSharedPlanningBook", "planningbook forgotten")
        return result
    }

    override suspend fun removePlanningBook(pbID: String): SimpleResponse {

        var result = SimpleResponse(true, 200, "removed", "")
        Klog.line("StateServiceImpl", "removePlanningBook", "Removing a planningBook, pbID: $pbID")

        try {
            //**REMOVE PB FROM REPO
            val respPB = planningBookService.removePlanningBook(pbID)
            if(!respPB.result || respPB.code != 200) {
                return respPB
            }
            Klog.line("StateServiceImpl", "removePlanningBook", "PB removed from repo")

            //**LIST OWNERS WITH THE PB IN ITS LIST
            val respOw = ownerService.listOwnersContainingPlanningBook(pbID)
            Klog.line("StateServiceImpl", "removePlanningBook", "Owners containing the PB -> ${respOw.ownerList!!.size}")
            if(!respOw.result || respOw.code != 200) {
                return respOw
            }
            if(respOw.ownerList!!.isEmpty()) {
                return result
            }

            //**REMOVE THE PB FROM EVERY OWNER LIST
            respOw.ownerList!!.forEach {
                it.planningBooks!!.remove(pbID)
                val respUP = ownerService.updateOwnerPlanningBooks(it)
                if(!respUP.result || respUP.code != 200) {
                    result = SimpleResponse(true, 400, "Error removing PB from an Owner", "${respUP.message}")
                }
            }
            Klog.line("StateServiceImpl", "removePlanningBook", "PB removed from owners list")

            //**UPDATING ACTIVE PB
            respOw.ownerList!!.forEach {
                if(it.activePlanningBook != null &&
                   it.activePlanningBook == pbID &&
                   it.planningBooks!!.isNotEmpty()) {

                    val pb = it.planningBooks!!.first()
                    val respAP = ownerService.updateOwnerActivePlanningBook(pb, it.id)
                    if(!respAP.result || respAP.code != 200) {
                        result = SimpleResponse(true, 401, "Error updating active planning book for owner ${it.id}, pb $pb", respAP.message)
                    }
                }
            }
            Klog.line("StateServiceImpl", "removePlanningBook", "PB active in owners updated")

            AppState.cleanState_data()
            loadState(AppState.useremail!!)
        }
        catch(e: Exception) {
            Klog.line("StateServiceImpl", "removePlanningBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("StateServiceImpl", "removePlanningBook", "planningbook removed")
        return result
    }
}