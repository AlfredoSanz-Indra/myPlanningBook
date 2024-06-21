package com.alfred.myplanningbook.domain.usecaseapi

import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.model.TaskBook

/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface TaskService {

    suspend fun createTask(task: TaskBook): SimpleResponse

    suspend fun getTaskList(planningBookId: String, fromDate: Long): SimpleResponse
}