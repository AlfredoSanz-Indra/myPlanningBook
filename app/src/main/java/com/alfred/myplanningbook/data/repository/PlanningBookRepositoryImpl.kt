package com.alfred.myplanningbook.data.repository

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.Owner
import com.alfred.myplanningbook.domain.model.PlanningBook
import com.alfred.myplanningbook.domain.repositoryapi.PlanningBookRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
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
class PlanningBookRepositoryImpl(private val ioDispatcher: CoroutineDispatcher): PlanningBookRepository {

    override suspend fun createPlanningBook(email: String, name: String): SimpleDataResponse {
        var result = SimpleDataResponse(false, 100, "not created")
        Klog.line("PlanningBookRepositoryImpl", "createPlanningBook", "email: $email")

        return result
    }

    /**
     * Puede devolver un planningBook o no.
     */
    override suspend fun getPlanningBook(idPlanningBook: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not found")
        Klog.line("PlanningBookRepositoryImpl", "getPlanningBook", "idPlanningBook: $idPlanningBook")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<QuerySnapshot> = FirebaseSession.db.collection("PLANNINGBOOK")
                    .whereEqualTo("id", idPlanningBook)
                    .limit(1)
                    .get()
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("PlanningBookRepositoryImpl", "getPlanningBook", "task is successfull")
                    var planningBook: PlanningBook? = null
                    for (document in task.result.documents) {
                        planningBook = PlanningBook(
                            document.id,
                            document.get("idOwner") as String,
                            document.get("name") as String
                        )
                    }
                    if(planningBook != null) {
                        taskResp = SimpleDataResponse(true, 200, "Planning book found - $planningBook")
                        taskResp.planningBook = planningBook
                    }
                    else {
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

    override suspend fun findOwner(email: String): SimpleDataResponse {
        var result = SimpleDataResponse(false, 404, "not found")
        Klog.line("PlanningBookRepositoryImpl", "findOwner", "email: $email")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<QuerySnapshot> = FirebaseSession.db.collection("OWNER")
                    .whereEqualTo("email", email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("PlanningBookRepositoryImpl", "findOwner", "task is successfull")
                    //Klog.line("PlanningBookRepositoryImpl", "findOwner", "documents size: ${task.result.documents.size}")
                    var ownerFound: Owner? = null
                    for (document in task.result.documents) {
                        ownerFound = Owner(
                            document.id,
                            document.get("name") as String,
                            document.get("email") as String,
                            document.get("activePlanningBook") as? String,
                            mutableListOf())
                    }
                    if(ownerFound != null) {
                        taskResp = SimpleDataResponse(true, 200, "Owner found - $ownerFound")
                        taskResp.owner = ownerFound
                    }
                    else {
                        taskResp = result
                    }
                }
                else {
                    Klog.line("PlanningBookRepositoryImpl", "findOwner", "error cause: ${task.exception?.cause}")
                    Klog.line("PlanningBookRepositoryImpl", "findOwner", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Find owner failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.line("PlanningBookRepositoryImpl", "findOwner", "result: $result")
        return result
    }

    /**
     * Create an owner in firestore
     *
     * @return SimpleDataResponse with the created owner. code== 200 -> created; code>= 400 not created
     */
    override suspend fun createOwner(email: String, name: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not created")
        Klog.line("PlanningBookRepositoryImpl", "createOwner", "email: $email")

        val owner = hashMapOf(
            "email" to email,
            "name" to name
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<DocumentReference> = FirebaseSession.db.collection("OWNER")
                    .add(owner)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("PlanningBookRepositoryImpl", "createOwner", "task is successful")
                    taskResp = SimpleDataResponse(true, 200, "Owner created - ${task.result.id}")
                    taskResp.owner = Owner(task.result.id, name, email, null, null)
                }
                else {
                    Klog.line("PlanningBookRepositoryImpl", "createOwner", "error cause: ${task.exception?.cause}")
                    Klog.line("PlanningBookRepositoryImpl", "createOwner", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Creating owner failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.line("PlanningBookRepositoryImpl", "createOwner", "result: $result")
        return result
    }

}