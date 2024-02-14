package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.repositoryapi.PlanningBookRepository
import com.alfred.myplanningbook.domain.usecaseapi.OwnerService
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class PlanningBookServiceImpl(private val planningBookRepository: PlanningBookRepository,
                              private val ownerService: OwnerService): PlanningBookService {

    /**
     * Return a single PlanningBook given its ID
     */
    override suspend fun getPlanningBook(id: String): SimpleResponse {

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

    /**
     * Return the object PlanningBook List given a list of planningBook IDs
     */
    override suspend fun getPlanningBooks(plannings: List<String>): SimpleResponse {

        var result = SimpleResponse(false, 400, "notLoaded", "")
        Klog.line("PlanningBookServiceImpl", "loadPlanningBooks", "loading user planningBooks")

        try {
            val pbList: MutableList<PlanningBook> = mutableListOf()
            plannings.forEach { id ->
                val resp = getPlanningBook(id)
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

            val resp2 = ownerService.updateOwnerPlanningBooks(AppState.owner!!)
            if(!resp2.result) {
                Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "updating owner pblist failed")
                return SimpleResponse(false, resp2.code, resp2.message, "")
            }
            Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "Owner planning books updated")

            if(AppState.owner!!.activePlanningBook == null) {
                AppState.owner!!.activePlanningBook = resp.planningBook!!.id
                AppState.activePlanningBook = resp.planningBook

                val resp3 = ownerService.updateOwnerActivePlanningBook(AppState.owner!!.activePlanningBook!!, AppState.owner!!.id)
                if(!resp3.result) {
                    Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "updating owner active pb failed")
                    return SimpleResponse(false, resp.code, resp3.message, "")
                }
                Klog.line("PlanningBookServiceImpl", "createPlanningBooks", "owner active pb updated")
            }

            result = SimpleResponse(true, 200, "Creation successful", "")
            result.planningBook = resp.planningBook
        }
        catch(e: Exception) {
            Klog.line("PlanningBookServiceImpl", "createPlanningBooks", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("PlanningBookServiceImpl", "createPlanningBooks", "result: $result")
        return result
    }

    private suspend fun createPB(name: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("PlanningBookServiceImpl", "createPB", "creating PB -> name: $name")

        try {
            val resp = planningBookRepository.createPlanningBook(name, AppState.owner!!.id)

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

    override suspend fun removePlanningBook(id: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("PlanningBookServiceImpl", "removePlanningBook", "Removing PB -> id: $id")

        try {
            val resp = planningBookRepository.removePlanningBook(id)

            result = if (resp.result && resp.code == 200) {
                SimpleResponse(true, 200, "removed", "")
            }
            else {
                SimpleResponse(false, resp.code, "not removed", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("PlanningBookServiceImpl", "removePlanningBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("PlanningBookServiceImpl", "removePlanningBook", "removed PB -> result: $result")
        return result
    }
}