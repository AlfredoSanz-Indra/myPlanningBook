package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.model.TaskBook
import com.alfred.myplanningbook.domain.repositoryapi.TaskRepository
import com.alfred.myplanningbook.domain.usecaseapi.OwnerService
import com.alfred.myplanningbook.domain.usecaseapi.TaskService

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class TaskServiceImpl(private val taskRepository: TaskRepository): TaskService {

    override suspend fun createTask(task: TaskBook): SimpleResponse {

        var result: SimpleResponse
        Klog.line("TaskServiceImpl", "createTask", "creating Task -> name: ${task.name}")

        try {
            val resp: SimpleDataResponse = taskRepository.createTask(task)
            Klog.linedbg("TaskServiceImpl", "createTask", "resp: $resp")

            if(!resp.result) {
                result = SimpleResponse(false, resp.code, resp.message, "")
            }
            else {
                result = SimpleResponse(true, 200, "got it", "")
                result.task = resp.task
            }
        }
        catch(e: Exception) {
            Klog.line("TaskServiceImpl", "createTask", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.linedbg("TaskServiceImpl", "createTask", "result: $result")
        return result
    }

    override suspend fun getTaskList(planningBookId: String, fromDate: Long): SimpleResponse {

        var result: SimpleResponse
        Klog.line("TaskServiceImpl", "getTaskList", "getting the taskbook list from planningbook -> pb_id: ${planningBookId}")

        if(planningBookId == null) {
            Klog.line("TaskServiceImpl", "getTaskList", "Missing planningBook id")
            result = SimpleResponse(false, 404, "Fail: Missing planningBook id!", "")
            return result
        }

        try {
            val resp = taskRepository.getTaskList(planningBookId, fromDate)

            if(resp.result && resp.code == 200) {
                result = SimpleResponse(true,200, "found", "")
                result.taskBookList = resp.taskBookList
            }
            else {
                result = SimpleResponse(false, resp.code, "not found", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("TaskServiceImpl", "getTaskList", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.line("TaskServiceImpl", "getTaskList", "result: $result")
        return result
    }
}