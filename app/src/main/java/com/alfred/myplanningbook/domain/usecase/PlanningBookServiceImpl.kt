package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.Owner
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.repositoryapi.PlanningBookRepository
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class PlanningBookServiceImpl(private val planningBookRepository: PlanningBookRepository): PlanningBookService {

    override suspend fun loadState(email: String): SimpleResponse {

        var result = SimpleResponse(false, 400, "notLoaded", "")
        Klog.line("PlanningBookServiceImpl", "loadState", "loading user state -> email: $email")

        try {
            val respLO = loadOwner(email)
            if(respLO.result) {
                result = SimpleResponse(true, 200, "owner found", "")
                AppState.owner = respLO.owner!!
                result.owner = respLO.owner

                if(!respLO.owner!!.activePlanningBook.isNullOrBlank()) {
                    Klog.linedbg("PlanningBookServiceImpl", "loadState", "loading PB -> respLO.owner!!.activePlanningBook!!: ${respLO.owner!!.activePlanningBook!!}")
                    val respPB = loadPlanningBook(respLO.owner!!.activePlanningBook!!)
                    if(respPB.result) {
                        AppState.activePlanningBook = respPB.planningBook!!
                        result.planningBook = respPB.planningBook
                        Klog.linedbg("PlanningBookServiceImpl", "loadState", "loaded PB -> result: $result")
                    }
                }

                if(respLO.owner!!.planningBooks!!.isNotEmpty()) {
                    Klog.linedbg("PlanningBookServiceImpl", "loadState", "loaded PB -> loading owner planningBooks")
                    respLO.owner!!.planningBooks!!.forEach { it ->
                        val respPB = loadPlanningBook(it)
                        if(respPB.result) {
                            AppState.planningBooks.add(respPB.planningBook!!)
                            Klog.linedbg("PlanningBookServiceImpl", "loadState", "loaded PB -> respPB: $respPB")
                        }
                    }
                    Klog.linedbg("PlanningBookServiceImpl", "loadState", "loaded PBs -> AppState.planningBooks: ${AppState.planningBooks}")
                }
                Klog.linedbg("PlanningBookServiceImpl", "loadState", "loading state all done -> result: $result")
            }
            else if(respLO.code == 400 || respLO.code == 404) {
                Klog.linedbg("PlanningBookServiceImpl", "loadState", "Owner didn't found -> respLO: $respLO")
                val respCO = createOwner(email)
                if(respCO.result) {
                    AppState.owner = respCO.owner!!
                    result = SimpleResponse(true, 200, "OK", "")
                    result.owner = respCO.owner
                    Klog.linedbg("PlanningBookServiceImpl", "loadState", "Owner created -> result: $result")
                }
                else {
                    result = respCO
                    Klog.linedbg("PlanningBookServiceImpl", "loadState", "Owner not created -> result: $result")
                }
            }
        }
        catch(e: Exception) {
            Klog.line("PlanningBookServiceImpl", "loadState", " Exception Message: ${e.message}")
            Klog.stackTrace("PlanningBookServiceImpl", "loadState", e.stackTrace)

            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.line("PlanningBookServiceImpl", "loadState", "result: $result")
        return result
    }

    private suspend fun loadOwner(email: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("PlanningBookServiceImpl", "loadOwner", "loading owner -> email: $email")

        try {
            val resp: SimpleDataResponse = planningBookRepository.getOwner(email)
            if(resp.result && resp.code == 200) {
                result = SimpleResponse(true,200, "found", "")
                result.owner = resp.owner
            }
            else {
                result = SimpleResponse(false, resp.code, "not found", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("PlanningBookServiceImpl", "loadOwner", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.line("PlanningBookServiceImpl", "loadOwner", "result: $result")
        return result
    }

    private suspend fun createOwner(email: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("PlanningBookServiceImpl", "createOwner", "creating owner -> email: $email")

        try {
            val name = email.split("@")[0]
            val resp = planningBookRepository.createOwner(email, name)

            if (resp.result && resp.code == 200) {
                result = SimpleResponse(true, 200, "created", "")
                result.owner = resp.owner
            }
            else {
                result = SimpleResponse(false, resp.code, "not created", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("PlanningBookServiceImpl", "loadOwner", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.line("PlanningBookServiceImpl", "createOwner", "result: $result")
        return result
    }

    private suspend fun loadPlanningBook(id: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("PlanningBookServiceImpl", "loadPlanningBook", "loading planning book -> id: $id")

        try {
            val resp: SimpleDataResponse = planningBookRepository.getPlanningBook(id)
            Klog.linedbg("PlanningBookServiceImpl", "loadPlanningBook", "resp: $resp")

            if(!resp.result) {
                result = SimpleResponse(false, resp.code, resp.message, "")
            }
            else {
                result = SimpleResponse(true, 200, "got it", "")
                result.planningBook = resp.planningBook
            }
        }
        catch(e: Exception) {
            Klog.line("PlanningBookServiceImpl", "loadPlanningBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.linedbg("PlanningBookServiceImpl", "loadPlanningBook", "result: $result")
        return result
    }

    override suspend fun loadPlanningBooks(plannings: List<String>): SimpleResponse {

        var result = SimpleResponse(false, 400, "notLoaded", "")
        Klog.line("PlanningBookServiceImpl", "loadPlanningBooks", "loading user planningBooks")

        try {
            val pbList: MutableList<PlanningBook> = mutableListOf()
            plannings.forEach { id ->
                val resp = loadPlanningBook(id)
                if(resp.result && resp.code == 200) {
                    pbList.add(resp.planningBook!!)
                }
            }
            Klog.linedbg("PlanningBookServiceImpl", "loadPlanningBooks", "loaded all planning books -> number: ${pbList.size}")
        }
        catch(e: Exception) {
            Klog.line("PlanningBookServiceImpl", "loadPlanningBooks", " Exception Message: ${e.message}")
            Klog.stackTrace("PlanningBookServiceImpl", "loadPlanningBooks", e.stackTrace)

            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.line("PlanningBookServiceImpl", "loadPlanningBooks", "result: $result")
        return result
    }

    /**
     * Crea un nuevo Planning Book en BD.
     * Lo añade a la lista del AppState y del Owner y actualiza Owner en BD.
     * Si el owner no tenia PB Activo, lo añade como activo al AppState y al Owner y actualiza owner en BD.
     */
    override suspend fun createPlanningBook(name: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "creating PlanningBook -> name: $name")

        if(AppState.owner == null) {
            Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "State hasn`t owner, can't create a PlanningBook")
            result = SimpleResponse(false, 404, "Fail: Owner is not present in State!", "")
            return result
        }

        try {
            val resp = createPB(name)
            if(!resp.result || resp.planningBook == null) {
                Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "creation of pb failed")
                return SimpleResponse(false, resp.code, resp.message, "")
            }
            Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "Planning Book Created")

            AppState.owner!!.planningBooks!!.add(resp.planningBook!!.id)
            AppState.planningBooks.add(resp.planningBook!!)

            val resp2 = updateOwnerPlanningBooks(AppState.owner!!)
            if(!resp2.result) {
                Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "updating owner pblist failed")
                return SimpleResponse(false, resp2.code, resp2.message, "")
            }
            Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "Owner planning books updated")

            if(AppState.owner!!.activePlanningBook == null) {
                AppState.owner!!.activePlanningBook = resp.planningBook!!.id
                AppState.activePlanningBook = resp.planningBook

                val resp3 = updateOwnerActivePlanningBook(AppState.owner!!)
                if(!resp3.result) {
                    Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "updating owner actrive pb failed")
                    return SimpleResponse(false, resp.code, resp3.message, "")
                }
            }

            result = SimpleResponse(true, 200, "Creation successful", "")
            result.planningBook = resp.planningBook
        }
        catch(e: Exception) {
            Klog.line("PlanningBookServiceImpl", "createPlanningBooks", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "result: $result")
        return result
    }

    private suspend fun createPB(name: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("PlanningBookServiceImpl", "createPB", "creating PB -> name: $name")

        try {
            val resp = planningBookRepository.createPlanningBook(name, AppState.owner!!.id)
            Klog.linedbg("PlanningBookServiceImpl", "createPB", "created PB -> resp: $resp")

            if (resp.result && resp.code == 200) {
                result = SimpleResponse(true, 200, "created", "")
                result.planningBook = resp.planningBook
            }
            else {
                result = SimpleResponse(false, resp.code, "not created", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("PlanningBookServiceImpl", "createPB", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("PlanningBookServiceImpl", "createPB", "created PB -> result: $result")
        return result
    }

    private suspend fun updateOwnerPlanningBooks(owner: Owner): SimpleResponse {

        var result: SimpleResponse
        Klog.line("PlanningBookServiceImpl", "updateOwnerPlanningBooks", "Updating Owner PBs -> owner.id: $owner.id")

        try {
            val resp = planningBookRepository.updateOwnerPlanningBooks(owner.id, owner.planningBooks!!)
            Klog.linedbg("PlanningBookServiceImpl", "updateOwnerPlanningBooks", "updated Owner -> resp: $resp")

            if (resp.result && resp.code == 200) {
                result = SimpleResponse(true, 200, "updated", "")
            }
            else {
                result = SimpleResponse(false, resp.code, "not updated", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("PlanningBookServiceImpl", "updateOwnerPlanningBooks", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("PlanningBookServiceImpl", "updateOwnerPlanningBooks", "updated owner -> result: $result")
        return result
    }

    private suspend fun updateOwnerActivePlanningBook(owner: Owner): SimpleResponse {

        var result: SimpleResponse
        Klog.line("PlanningBookServiceImpl", "updateOwnerActivePlanningBook", "Updating Owner PBs -> owner.id: $owner.id")

        try {
            val resp = planningBookRepository.updateOwnerActivePlanningBook(owner.id, owner.activePlanningBook!!)
            Klog.linedbg("PlanningBookServiceImpl", "updateOwnerActivePlanningBook", "updated Owner -> resp: $resp")

            if (resp.result && resp.code == 200) {
                result = SimpleResponse(true, 200, "updated", "")
            }
            else {
                result = SimpleResponse(false, resp.code, "not updated", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("PlanningBookServiceImpl", "updateOwnerActivePlanningBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("PlanningBookServiceImpl", "updateOwnerActivePlanningBook", "updated owner -> result: $result")
        return result
    }
}