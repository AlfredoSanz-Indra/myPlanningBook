package com.alfred.myplanningbook.data.repository

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
import com.alfred.myplanningbook.data.model.Documents
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.ActivityBook
import com.alfred.myplanningbook.domain.model.TaskBook
import com.alfred.myplanningbook.domain.repositoryapi.ActivityRepository
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
class ActivityRepositoryImpl(private val ioDispatcher: CoroutineDispatcher): ActivityRepository {

    override suspend fun createActivity(activitybook: ActivityBook): SimpleDataResponse {
        var result = SimpleDataResponse(false, 100, "not created")
        Klog.line("ActivityRepositoryImpl", "createActivity", "name: ${activitybook.name}")

        val activityBookData = hashMapOf(
            Documents.ACTIVITYBOOK_NAME to activitybook.name,
            Documents.ACTIVITYBOOK_PLANNINGBOOK_ID to activitybook.idPlanningbook,
            Documents.ACTIVITYBOOK_DESC to activitybook.description,
            Documents.ACTIVITYBOOK_START_HOUR to activitybook.startHour,
            Documents.ACTIVITYBOOK_START_MINUTE to activitybook.startMinute,
            Documents.ACTIVITYBOOK_END_HOUR to activitybook.endHour,
            Documents.ACTIVITYBOOK_END_MINUTE to activitybook.endMinute,
            Documents.ACTIVITYBOOK_WEEKDAYS to activitybook.weekDaysList,
            Documents.ACTIVITYBOOK_ISACTIVE to activitybook.isActive
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<DocumentReference> = FirebaseSession.db.collection(Collections.ACTIVITYBOOK)
                    .add(activityBookData)
                    .addOnSuccessListener {}

                task.await()

                var activityResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("ActivityRepositoryImpl", "createActivity", "task is successful")
                    activityResp = SimpleDataResponse(true, 200, "Activity created - ${task.result.id}")
                    activitybook.id = task.result.id
                    Klog.line("ActivityRepositoryImpl", "createActivity", "activitybook: $activitybook")
                    activityResp.activity = activitybook
                }
                else {
                    Klog.line("ActivityRepositoryImpl", "createActivity", "error cause: ${task.exception?.cause}")
                    Klog.line("ActivityRepositoryImpl", "createActivity", "error message: ${task.exception?.message}")

                    activityResp = SimpleDataResponse(false, 400, "Creating Task failed.")
                }

                return@async activityResp
            }

            result = defer.await()
        } //scope

        Klog.linedbg("ActivityRepositoryImpl", "createActivity", "result: $result")
        return result
    }

    override suspend fun updateActivity(activitybook: ActivityBook): SimpleDataResponse {
        var result = SimpleDataResponse(false, 100, "not updated")
        Klog.line("ActivityRepositoryImpl", "updateActivity", "name: ${activitybook.name}")

        val activityBookData = hashMapOf(
            Documents.ACTIVITYBOOK_NAME to activitybook.name,
            Documents.ACTIVITYBOOK_PLANNINGBOOK_ID to activitybook.idPlanningbook,
            Documents.ACTIVITYBOOK_DESC to activitybook.description,
            Documents.ACTIVITYBOOK_START_HOUR to activitybook.startHour,
            Documents.ACTIVITYBOOK_START_MINUTE to activitybook.startMinute,
            Documents.ACTIVITYBOOK_END_HOUR to activitybook.endHour,
            Documents.ACTIVITYBOOK_END_MINUTE to activitybook.endMinute,
            Documents.ACTIVITYBOOK_WEEKDAYS to activitybook.weekDaysList,
            Documents.ACTIVITYBOOK_ISACTIVE to activitybook.isActive
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<Void> = FirebaseSession.db.collection(Collections.ACTIVITYBOOK).document(activitybook.id!!)
                    .set(activityBookData)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("ActivityRepositoryImpl", "updateActivity", "task is successful")
                    taskResp = SimpleDataResponse(true, 200, "Task update - ${activitybook.id}")
                }
                else {
                    Klog.line("ActivityRepositoryImpl", "updateActivity", "error cause: ${task.exception?.cause}")
                    Klog.line("ActivityRepositoryImpl", "updateActivity", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Updating Activity failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.linedbg("ActivityRepositoryImpl", "updateActivity", "result: $result")
        return result
     }

    override suspend fun getActivityList(planningBookId: String, isActive: Int): SimpleDataResponse {
        var result = SimpleDataResponse(false, 404, "not found")
        Klog.line("ActivityRepositoryImpl", "getActivityList", "Listing activitiesBook from the planningBook: pbId $planningBookId")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<QuerySnapshot> = FirebaseSession.db.collection(Collections.ACTIVITYBOOK)
                    .whereEqualTo(Documents.ACTIVITYBOOK_PLANNINGBOOK_ID, planningBookId)
                    .whereGreaterThanOrEqualTo(Documents.ACTIVITYBOOK_ISACTIVE, isActive)
                    .orderBy(Documents.ACTIVITYBOOK_NAME)
                    .get()
                    .addOnSuccessListener {}


                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("ActivityRepositoryImpl", "getActivityList", "task is successful")
                    var activityBookList: MutableList<ActivityBook> = mutableListOf()
                    for (document in task.result.documents) {
                        var activityBookFound = ActivityBook(
                            document.id,
                            document.get(Documents.ACTIVITYBOOK_PLANNINGBOOK_ID) as String,
                            document.get(Documents.ACTIVITYBOOK_NAME) as String,
                            document.get(Documents.ACTIVITYBOOK_DESC) as String?,
                            (document.get(Documents.ACTIVITYBOOK_START_HOUR) as Long).toInt(),
                            (document.get(Documents.ACTIVITYBOOK_START_MINUTE) as Long).toInt(),
                            (document.get(Documents.ACTIVITYBOOK_END_HOUR) as Long).toInt(),
                            (document.get(Documents.ACTIVITYBOOK_END_MINUTE) as Long).toInt(),
                            (document.get(Documents.ACTIVITYBOOK_WEEKDAYS) as MutableList<String>),
                            (document.get(Documents.ACTIVITYBOOK_ISACTIVE) as Long).toInt(),
                        )
                        activityBookList.add(activityBookFound)
                    }

                    taskResp = SimpleDataResponse(true, 200, "Activities list - $activityBookList")
                    taskResp.activityBookList = activityBookList
                }
                else {
                    Klog.line("ActivityRepositoryImpl", "getActivityList", "error cause: ${task.exception?.cause}")
                    Klog.line("ActivityRepositoryImpl", "getActivityList", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Listing owner failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.line("ActivityRepositoryImpl", "getActivityList", "result: $result")
        return result
    }
}