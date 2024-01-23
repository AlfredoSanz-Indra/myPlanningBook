package com.alfred.myplanningbook.data.repository

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
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
            Collections.PLANNINGBOOK_IDOWNER to idOwner,
            Collections.PLANNINGBOOK_NAME to name
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
     * Puede devolver un planningBook o no.
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
                            task.result.get(Collections.PLANNINGBOOK_NAME) as String,
                            task.result.get(Collections.PLANNINGBOOK_IDOWNER)as String
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

    /**
     * Update the planningbook list of an owner
     */
    override suspend fun updateOwnerPlanningBooks(ownerid: String, pblist: MutableList<String>): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not updated")
        Klog.line("PlanningBookRepositoryImpl", "updateOwnerPlanningBooks", "ownerid: $ownerid")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<Void> = FirebaseSession.db.collection(Collections.OWNER).document(ownerid)
                    .update(Collections.OWNER_PLANNINGBOOKS, pblist)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("PlanningBookRepositoryImpl", "updateOwnerPlanningBooks", "task is successful")
                    taskResp = SimpleDataResponse(true, 200, "Owner PlanningBooks updated")
                }
                else {
                    Klog.line("PlanningBookRepositoryImpl", "updateOwnerPlanningBooks", "error cause: ${task.exception?.cause}")
                    Klog.line("PlanningBookRepositoryImpl", "updateOwnerPlanningBooks", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Updating owner PlanningBooks failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.line("PlanningBookRepositoryImpl", "updateOwnerPlanningBooks", "result: $result")
        return result
    }

    /**
     * Update the owner current active planning book
     */
    override suspend fun updateOwnerActivePlanningBook(ownerid: String, planningbookID: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not updated")
        Klog.line("PlanningBookRepositoryImpl", "updateOwnerActivePlanningBook", "ownerid: $ownerid")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<Void> = FirebaseSession.db.collection(Collections.OWNER).document(ownerid)
                    .update(Collections.OWNER_ACTIVEPLANNINGBOOK, planningbookID)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("PlanningBookRepositoryImpl", "updateOwnerActivePlanningBook", "task is successful")
                    taskResp = SimpleDataResponse(true, 200, "Owner activePlanningBook updated")
                }
                else {
                    Klog.line("PlanningBookRepositoryImpl", "updateOwnerActivePlanningBook", "error cause: ${task.exception?.cause}")
                    Klog.line("PlanningBookRepositoryImpl", "updateOwnerActivePlanningBook", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Updating owner activePlanningBooks failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.line("PlanningBookRepositoryImpl", "updateOwnerActivePlanningBook", "result: $result")
        return result
    }

    override suspend fun getOwner(email: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not found")
        Klog.line("PlanningBookRepositoryImpl", "getOwner", "email: $email")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<QuerySnapshot> = FirebaseSession.db.collection(Collections.OWNER)
                    .whereEqualTo(Collections.OWNER_EMAIL, email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("PlanningBookRepositoryImpl", "getOwner", "task is successful")
                    var ownerFound: Owner? = null
                    for (document in task.result.documents) {
                        ownerFound = Owner(
                            document.id,
                            document.get(Collections.OWNER_NAME) as String,
                            document.get(Collections.OWNER_EMAIL) as String,
                            document.get(Collections.OWNER_ACTIVEPLANNINGBOOK) as? String,
                            mutableListOf()
                        )
                        val pbl: MutableList<String>? = document.get(Collections.OWNER_PLANNINGBOOKS) as? MutableList<String>
                        if(pbl != null) {
                            ownerFound.planningBooks = pbl
                        }
                        Klog.linedbg("PlanningBookRepositoryImpl", "getOwner", "ownerFound: $ownerFound")
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
                    Klog.line("PlanningBookRepositoryImpl", "getOwner", "error cause: ${task.exception?.cause}")
                    Klog.line("PlanningBookRepositoryImpl", "getOwner", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Find owner failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.line("PlanningBookRepositoryImpl", "getOwner", "result: $result")
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
            Collections.OWNER_EMAIL to email,
            Collections.OWNER_NAME to name
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<DocumentReference> = FirebaseSession.db.collection(Collections.OWNER)
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

        Klog.linedbg("PlanningBookRepositoryImpl", "createOwner", "result: $result")
        return result
    }
}