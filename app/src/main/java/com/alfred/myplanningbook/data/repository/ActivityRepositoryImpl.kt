package com.alfred.myplanningbook.data.repository

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
import com.alfred.myplanningbook.data.model.Documents
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.ActivityBook
import com.alfred.myplanningbook.domain.repositoryapi.ActivityRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
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
            Documents.ACTIVITYBOOK_WEEKDAYS to activitybook.weekDaysList
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
}