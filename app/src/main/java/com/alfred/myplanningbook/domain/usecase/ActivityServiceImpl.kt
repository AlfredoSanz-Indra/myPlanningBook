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
}