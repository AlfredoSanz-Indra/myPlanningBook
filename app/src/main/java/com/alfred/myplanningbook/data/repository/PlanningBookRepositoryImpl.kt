package com.alfred.myplanningbook.data.repository

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
import com.alfred.myplanningbook.data.model.Documents
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.Owner
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.domain.repositoryapi.PlanningBookRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class PlanningBookRepositoryImpl(private val ioDispatcher: CoroutineDispatcher): PlanningBookRepository {

    /**
     * Create a new PlanningBook
     */
    override suspend fun createPlanningBook(name: String, idOwner: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 100, "not created")
        Klog.line("PlanningBookRepositoryImpl", "createPlanningBook", "name: $name")

        val pb = hashMapOf(
            Documents.PLANNINGBOOK_IDOWNER to idOwner,
            Documents.PLANNINGBOOK_NAME to name
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<DocumentReference> = FirebaseSession.db.collection(Collections.PLANNINGBOOK)
                    .add(pb)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("PlanningBookRepositoryImpl", "createPlanningBook", "task is successful")
                    taskResp = SimpleDataResponse(true, 200, "PlanningBook created - ${task.result.id}")
                    taskResp.planningBook = PlanningBook(task.result.id, name, idOwner)
                }
                else {
                    Klog.line("PlanningBookRepositoryImpl", "createPlanningBook", "error cause: ${task.exception?.cause}")
                    Klog.line("PlanningBookRepositoryImpl", "createPlanningBook", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Creating PlanningBook failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.linedbg("PlanningBookRepositoryImpl", "createPlanningBook", "result: $result")
        return result
    }

    /**
     * Find a planningBook.
     */
    override suspend fun getPlanningBook(idPlanningBook: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not found")
        Klog.line("PlanningBookRepositoryImpl", "getPlanningBook", "idPlanningBook: $idPlanningBook")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<DocumentSnapshot> = FirebaseSession.db
                    .collection(Collections.PLANNINGBOOK)
                    .document(idPlanningBook)
                    .get()
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse

                if(task.isSuccessful) {
                    Klog.line("PlanningBookRepositoryImpl", "getPlanningBook", "task is successfull")
                    if(task.result != null) {
                        val planningBook = PlanningBook(
                            task.result.getId(),
                            task.result.get(Documents.PLANNINGBOOK_NAME) as String,
                            task.result.get(Documents.PLANNINGBOOK_IDOWNER)as String
                        )

                        taskResp = SimpleDataResponse(true, 200, "Planning book found - $planningBook")
                        taskResp.planningBook = planningBook
                    }
                    else {
                        Klog.linedbg("PlanningBookRepositoryImpl", "getPlanningBook", "task planning book didn't found")
                        taskResp = result
                    }
                }
                else {
                    Klog.line("PlanningBookRepositoryImpl", "getPlanningBook", "error cause: ${task.exception?.cause}")
                    Klog.line("PlanningBookRepositoryImpl", "getPlanningBook", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Find planning book failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.line("PlanningBookRepositoryImpl", "getPlanningBook", "result: $result")
        return result
    }

    override suspend fun removePlanningBook(id: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not found")
        Klog.line("PlanningBookRepositoryImpl", "removePlanningBook", "id: $id")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<Void> = FirebaseSession.db
                    .collection(Collections.PLANNINGBOOK)
                    .document(id)
                    .delete()
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse = if(task.isSuccessful) {
                    Klog.line("PlanningBookRepositoryImpl", "removePlanningBook", "task is successfull")
                    SimpleDataResponse(true, 200, "Planning book removed")
                }
                else {
                    Klog.line("PlanningBookRepositoryImpl", "removePlanningBook", "error cause: ${task.exception?.cause}")
                    Klog.line("PlanningBookRepositoryImpl", "removePlanningBook", "error message: ${task.exception?.message}")

                    SimpleDataResponse(false, 400, "Removing planning book failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.line("PlanningBookRepositoryImpl", "removePlanningBook", "result: $result")
        return result
    }
}