package com.alfred.myplanningbook.data.repository

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
import com.alfred.myplanningbook.data.model.Documents
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.domain.model.TaskBook
import com.alfred.myplanningbook.domain.repositoryapi.TaskRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class TaskRepositoryImpl(private val ioDispatcher: CoroutineDispatcher): TaskRepository {

    override suspend fun createTask(taskbook: TaskBook): SimpleDataResponse {
        var result = SimpleDataResponse(false, 100, "not created")
        Klog.line("TaskRepositoryImpl", "createTask", "name: ${taskbook.name}")

        val taskBookData = hashMapOf(
            Documents.TASKBOOK_NAME to taskbook.name,
            Documents.TASKBOOK_PLANNINGBOOK_ID to taskbook.idPlanningbook,
            Documents.TASKBOOK_DESC to taskbook.description,
            Documents.TASKBOOK_DATE_MILLIS to taskbook.dateInMillis,
            Documents.TASKBOOK_YEAR to taskbook.year,
            Documents.TASKBOOK_MONTH to taskbook.month,
            Documents.TASKBOOK_DAY to taskbook.day,
            Documents.TASKBOOK_HOUR to taskbook.hour,
            Documents.TASKBOOK_MINUTE to taskbook.minute,
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<DocumentReference> = FirebaseSession.db.collection(Collections.TASKBOOK)
                    .add(taskBookData)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("TaskRepositoryImpl", "createTask", "task is successful")
                    taskResp = SimpleDataResponse(true, 200, "Task created - ${task.result.id}")
                    taskbook.id = task.result.id
                    Klog.line("TaskRepositoryImpl", "createTask", "taskbook: $taskbook")
                    taskResp.task = taskbook
                }
                else {
                    Klog.line("TaskRepositoryImpl", "createTask", "error cause: ${task.exception?.cause}")
                    Klog.line("TaskRepositoryImpl", "createTask", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Creating Task failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.linedbg("TaskRepositoryImpl", "createTask", "result: $result")
        return result
    }
}