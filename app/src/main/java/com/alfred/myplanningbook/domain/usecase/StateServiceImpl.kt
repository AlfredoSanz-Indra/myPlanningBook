package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.domain.AppState
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
}