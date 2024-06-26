package com.alfred.myplanningbook.data.repository

import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.Collections
import com.alfred.myplanningbook.data.model.Documents
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.model.Owner
import com.alfred.myplanningbook.domain.repositoryapi.OwnerRepository
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
class OwnerRepositoryImpl(private val ioDispatcher: CoroutineDispatcher): OwnerRepository {
    override suspend fun getOwner(email: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not found")
        Klog.line("OwnerRepositoryImpl", "getOwner", "email: $email")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<QuerySnapshot> = FirebaseSession.db.collection(Collections.OWNER)
                    .whereEqualTo(Documents.OWNER_EMAIL, email)
                    .limit(1)
                    .get()
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("OwnerRepositoryImpl", "getOwner", "task is successful")
                    var ownerFound: Owner? = null
                    for (document in task.result.documents) {
                        ownerFound = Owner(
                            document.id,
                            document.get(Documents.OWNER_NAME) as String,
                            document.get(Documents.OWNER_EMAIL) as String,
                            document.get(Documents.OWNER_ACTIVEPLANNINGBOOK) as? String,
                            mutableListOf()
                        )
                        val pbl: MutableList<String>? = document.get(Documents.OWNER_PLANNINGBOOKS) as? MutableList<String>
                        if(pbl != null) {
                            ownerFound.planningBooks = pbl
                        }
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
                    Klog.line("OwnerRepositoryImpl", "getOwner", "error cause: ${task.exception?.cause}")
                    Klog.line("OwnerRepositoryImpl", "getOwner", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Find owner failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.line("OwnerRepositoryImpl", "getOwner", "result: $result")
        return result
    }

    override suspend fun listOwnersByContainPB(pbId: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not found")
        Klog.line("OwnerRepositoryImpl", "listOwnersByContainPB", "Listing owners containing this planningBook: pbId $pbId")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<QuerySnapshot> = FirebaseSession.db.collection(Collections.OWNER)
                    .whereArrayContains(Documents.OWNER_PLANNINGBOOKS, pbId)
                    .get()
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("OwnerRepositoryImpl", "listOwnersByContainPB", "task is successful")
                    var ownerList: MutableList<Owner> = mutableListOf()
                    for (document in task.result.documents) {
                        var ownerFound: Owner = Owner(
                            document.id,
                            document.get(Documents.OWNER_NAME) as String,
                            document.get(Documents.OWNER_EMAIL) as String,
                            document.get(Documents.OWNER_ACTIVEPLANNINGBOOK) as? String,
                            mutableListOf()
                        )
                        val pbl: MutableList<String>? = document.get(Documents.OWNER_PLANNINGBOOKS) as? MutableList<String>
                        if(pbl != null) {
                            ownerFound.planningBooks = pbl
                        }

                        ownerList.add(ownerFound)
                    }

                    taskResp = SimpleDataResponse(true, 200, "Owner list - $ownerList")
                    taskResp.ownerList = ownerList
                }
                else {
                    Klog.line("OwnerRepositoryImpl", "listOwnersByContainPB", "error cause: ${task.exception?.cause}")
                    Klog.line("OwnerRepositoryImpl", "listOwnersByContainPB", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Listing owner failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        } //scope

        Klog.line("OwnerRepositoryImpl", "listOwnersByContainPB", "result: $result")
        return result
    }


    /**
     * Create an owner in firestore
     *
     * @return SimpleDataResponse with the created owner. code== 200 -> created; code>= 400 not created
     */
    override suspend fun createOwner(email: String, name: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not created")
        Klog.line("OwnerRepositoryImpl", "createOwner", "email: $email")

        val owner = hashMapOf(
            Documents.OWNER_EMAIL to email,
            Documents.OWNER_NAME to name
        )

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<DocumentReference> = FirebaseSession.db.collection(Collections.OWNER)
                    .add(owner)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("OwnerRepositoryImpl", "createOwner", "task is successful")
                    taskResp = SimpleDataResponse(true, 200, "Owner created - ${task.result.id}")
                    taskResp.owner = Owner(task.result.id, name, email, null, null)
                }
                else {
                    Klog.line("OwnerRepositoryImpl", "createOwner", "error cause: ${task.exception?.cause}")
                    Klog.line("OwnerRepositoryImpl", "createOwner", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Creating owner failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.linedbg("OwnerRepositoryImpl", "createOwner", "result: $result")
        return result
    }

    /**
     * Update the planningbook list of an owner
     */
    override suspend fun updateOwnerPlanningBooks(ownerid: String, pblist: MutableList<String>): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not updated")
        Klog.line("OwnerRepositoryImpl", "updateOwnerPlanningBooks", "ownerid: $ownerid")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<Void> = FirebaseSession.db.collection(Collections.OWNER).document(ownerid)
                    .update(Documents.OWNER_PLANNINGBOOKS, pblist)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("OwnerRepositoryImpl", "updateOwnerPlanningBooks", "task is successful")
                    taskResp = SimpleDataResponse(true, 200, "Owner PlanningBooks updated")
                }
                else {
                    Klog.line("OwnerRepositoryImpl", "updateOwnerPlanningBooks", "error cause: ${task.exception?.cause}")
                    Klog.line("OwnerRepositoryImpl", "updateOwnerPlanningBooks", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Updating owner PlanningBooks failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.line("OwnerRepositoryImpl", "updateOwnerPlanningBooks", "result: $result")
        return result
    }

    /**
     * Update the owner current active planning book
     */
    override suspend fun updateOwnerActivePlanningBook(ownerid: String, planningbookID: String): SimpleDataResponse {

        var result = SimpleDataResponse(false, 404, "not updated")
        Klog.line("OwnerRepositoryImpl", "updateOwnerActivePlanningBook", "ownerid: $ownerid")

        withContext(ioDispatcher) {
            val defer = async(ioDispatcher) {
                val task: Task<Void> = FirebaseSession.db.collection(Collections.OWNER).document(ownerid)
                    .update(Documents.OWNER_ACTIVEPLANNINGBOOK, planningbookID)
                    .addOnSuccessListener {}

                task.await()

                var taskResp: SimpleDataResponse
                if(task.isSuccessful) {
                    Klog.line("OwnerRepositoryImpl", "updateOwnerActivePlanningBook", "task is successful")
                    taskResp = SimpleDataResponse(true, 200, "Owner activePlanningBook updated")
                }
                else {
                    Klog.line("OwnerRepositoryImpl", "updateOwnerActivePlanningBook", "error cause: ${task.exception?.cause}")
                    Klog.line("OwnerRepositoryImpl", "updateOwnerActivePlanningBook", "error message: ${task.exception?.message}")

                    taskResp = SimpleDataResponse(false, 400, "Updating owner activePlanningBooks failed.")
                }

                return@async taskResp
            }

            result = defer.await()
        }//scope

        Klog.line("OwnerRepositoryImpl", "updateOwnerActivePlanningBook", "result: $result")
        return result
    }
}