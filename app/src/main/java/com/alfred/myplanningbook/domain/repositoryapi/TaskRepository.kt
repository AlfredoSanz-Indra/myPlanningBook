package com.alfred.myplanningbook.domain.repositoryapi

import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.TaskBook


/**
 * @author Alfredo Sanz
 * @time 2024
 */
interface TaskRepository {

    suspend fun createTask(task: TaskBook): SimpleDataResponse

    suspend fun getTaskList(planningBookId: String): SimpleDataResponse
}