package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.AppState
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
                    Klog.line("PlanningBookServiceImpl", "loadState", "loading state PB -> respLO.owner!!.activePlanningBook!!: ${respLO.owner!!.activePlanningBook!!}")
                    val respPB = loadPlanningBook(respLO.owner!!.activePlanningBook!!)
                    if(respPB.result) {
                        AppState.planningBook = respPB.planningBook!!
                        result.planningBook = respPB.planningBook
                        Klog.line("PlanningBookServiceImpl", "loadState", "loading state PB -> result: $result")
                    }
                }
                Klog.line("PlanningBookServiceImpl", "loadState", "loading state all done -> result: $result")
            }
            else if(respLO.code == 400 || respLO.code == 404) {
                Klog.line("PlanningBookServiceImpl", "loadState", "Owner didn't found -> respLO: $respLO")
                val respCO = createOwner(email)
                if(respCO.result) {
                    AppState.owner = respCO.owner!!
                    result = SimpleResponse(true, 200, "OK", "")
                    Klog.line("PlanningBookServiceImpl", "loadState", "Owner created -> result: $result")
                }
                else {
                    result = respCO
                    Klog.line("PlanningBookServiceImpl", "loadState", "Owner not created -> result: $result")
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
            val resp: SimpleDataResponse = planningBookRepository.findOwner(email)
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
                result = SimpleResponse(true, 200, "found", "")
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

    private suspend fun loadPlanningBook(email: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("PlanningBookServiceImpl", "loadPlanningBook", "loading planning book -> email: $email")

        try {
            val resp: SimpleDataResponse = planningBookRepository.getPlanningBook(email)
            Klog.line("PlanningBookServiceImpl", "loadPlanningBook", "resp: $resp")

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

        Klog.line("PlanningBookServiceImpl", "loadPlanningBook", "result: $result")
        return result
    }
}