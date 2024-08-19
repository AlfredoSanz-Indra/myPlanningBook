package com.alfred.myplanningbook.data.repository

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
import com.alfred.myplanningbook.data.model.Documents
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.Owner
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.domain.model.TaskBook
import com.alfred.myplanningbook.domain.repositoryapi.TaskRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * @author Alfredo Sanz
 * @time 2024
 */
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

    override suspend fun updateTask(taskbook: TaskBook): SimpleDataResponse {
        var result = SimpleDataResponse(false, 100, "not updated")
        Klog.line("TaskRepositoryImpl", "updateTask", "name: ${taskbook.name}")

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
                val task: Task<Void> = FirebaseSession.db.collection(Collections.TASKBOOK).document(taskbook.id!!)
                    .set(taskBookData)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("TaskRepositoryImpl", "updateTask", "task is successful")
                    taskResp = SimpleDataResponse(true, 200, "Task update - ${taskbook.id}")
                }
                else {
                    Klog.line("TaskRepositoryImpl", "updateTask", "error cause: ${task.exception?.cause}")
                    Klog.line("TaskRepositoryImpl", "updateTask", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Updating Task failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.linedbg("TaskRepositoryImpl", "updateTask", "result: $result")
        return result
    }

    override suspend fun getTaskList(planningBookId: String, fromDate: Long): SimpleDataResponse {
        var result = SimpleDataResponse(false, 404, "not found")
        Klog.line("TaskRepositoryImpl", "getTaskList", "Listing tasksBook from the planningBook: pbId $planningBookId")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<QuerySnapshot> = FirebaseSession.db.collection(Collections.TASKBOOK)
                    .whereEqualTo(Documents.TASKBOOK_PLANNINGBOOK_ID, planningBookId)
                    .whereGreaterThanOrEqualTo(Documents.TASKBOOK_DATE_MILLIS, fromDate)
                    .orderBy(Documents.TASKBOOK_DATE_MILLIS)
                    .orderBy(Documents.TASKBOOK_HOUR)
                    .orderBy(Documents.TASKBOOK_MINUTE)
                    .get()
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("TaskRepositoryImpl", "getTaskList", "task is successful")
                    var taskBookList: MutableList<TaskBook> = mutableListOf()
                    for (document in task.result.documents) {
                        var taskbookFound: TaskBook = TaskBook(
                            document.id,
                            document.get(Documents.TASKBOOK_PLANNINGBOOK_ID) as String,
                            document.get(Documents.TASKBOOK_NAME) as String,
                            document.get(Documents.TASKBOOK_DESC) as String?,
                            document.get(Documents.TASKBOOK_DATE_MILLIS) as Long,
                            (document.get(Documents.TASKBOOK_YEAR) as Long).toInt(),
                            (document.get(Documents.TASKBOOK_MONTH) as Long).toInt(),
                            (document.get(Documents.TASKBOOK_DAY) as Long).toInt(),
                            (document.get(Documents.TASKBOOK_HOUR) as Long).toInt(),
                            (document.get(Documents.TASKBOOK_MINUTE) as Long).toInt()
                        )
                        taskBookList.add(taskbookFound)
                    }

                    taskResp = SimpleDataResponse(true, 200, "Owner list - $taskBookList")
                    taskResp.taskBookList = taskBookList
                }
                else {
                    Klog.line("TaskRepositoryImpl", "getTaskList", "error cause: ${task.exception?.cause}")
                    Klog.line("TaskRepositoryImpl", "getTaskList", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Listing owner failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.line("TaskRepositoryImpl", "listOwnersByContainPB", "result: $result")
        return result
    }
}