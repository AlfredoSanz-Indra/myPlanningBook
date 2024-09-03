package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.ActivityBook
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.repositoryapi.ActivityRepository
import com.alfred.myplanningbook.domain.usecaseapi.ActivityService

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class ActivityServiceImpl(private val activityRepository: ActivityRepository): ActivityService {

    override suspend fun createActivity(activity: ActivityBook): SimpleResponse {
        var result: SimpleResponse
        Klog.line("ActivityServiceImpl", "createActivity", "creating Activity -> name: ${activity.name}")

        try {
            activity.isActive = 1

            val resp: SimpleDataResponse = activityRepository.createActivity(activity)
            Klog.linedbg("ActivityServiceImpl", "createActivity", "resp: $resp")

            if(!resp.result) {
                result = SimpleResponse(false, resp.code, resp.message, "")
            }
            else {
                result = SimpleResponse(true, 200, "got it", "")
                result.activity = resp.activity
            }
        }
        catch(e: Exception) {
            Klog.line("ActivityServiceImpl", "createActivity", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.linedbg("ActivityServiceImpl", "createActivity", "result: $result")
        return result
    }

    override suspend fun updateActivity(activity: ActivityBook): SimpleResponse {
        var result: SimpleResponse
        Klog.line("ActivityServiceImpl", "updateActivity", "updating Activity -> name: ${activity.name}")

        try {
            activity.isActive = 1

            val resp: SimpleDataResponse = activityRepository.updateActivity(activity)
            Klog.linedbg("ActivityServiceImpl", "updateActivity", "resp: $resp")

            if(!resp.result) {
                result = SimpleResponse(false, resp.code, resp.message, "")
            }
            else {
                result = SimpleResponse(true, 200, "got it", "")
                result.activity = resp.activity
            }
        }
        catch(e: Exception) {
            Klog.line("ActivityServiceImpl", "updateActivity", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.linedbg("ActivityServiceImpl", "updateActivity", "result: $result")
        return result
    }

    override suspend fun getActivityList(planningBookId: String, isActive: Int): SimpleResponse {
        var result: SimpleResponse
        Klog.line("ActivityServiceImpl", "getActivityList", "getting the activityBook list from planningbook -> pb_id: ${planningBookId}")

        if(planningBookId == null) {
            Klog.line("ActivityServiceImpl", "getActivityList", "Missing planningBook id")
            result = SimpleResponse(false, 404, "Fail: Missing planningBook id!", "")
            return result
        }

        try {
            val resp = activityRepository.getActivityList(planningBookId, isActive)

            if(resp.result && resp.code == 200) {
                result = SimpleResponse(true,200, "found", "")
                result.activityBookList = resp.activityBookList
            }
            else {
                result = SimpleResponse(false, resp.code, "not found", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("ActivityServiceImpl", "getActivityList", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.line("ActivityServiceImpl", "getActivityList", "result: $result")
        return result
    }

    override suspend fun deleteActivity(id: String): SimpleResponse {
        var result: SimpleResponse
        Klog.line("ActivityServiceImpl", "deleteActivity", "Removing Activity -> id: $id")

        try {
            val resp = activityRepository.deleteActivity(id)

            result = if (resp.result && resp.code == 200) {
                SimpleResponse(true, 200, "removed", "")
            }
            else {
                SimpleResponse(false, resp.code, "not removed", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("ActivityServiceImpl", "deleteActivity", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("ActivityServiceImpl", "deleteActivity", "removed Activity -> result: $result")
        return result
    }
}