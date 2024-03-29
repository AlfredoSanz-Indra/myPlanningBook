package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.Owner
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.repositoryapi.OwnerRepository
import com.alfred.myplanningbook.domain.usecaseapi.OwnerService

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class OwnerServiceImpl(private val ownerRepository: OwnerRepository): OwnerService {

    override suspend fun getOwner(email: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("OwnerServiceImpl", "loadOwner", "loading owner -> email: $email")

        try {
            val resp: SimpleDataResponse = ownerRepository.getOwner(email)
            if(resp.result && resp.code == 200) {
                result = SimpleResponse(true,200, "found", "")
                result.owner = resp.owner
            }
            else {
                result = SimpleResponse(false, resp.code, "not found", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("OwnerServiceImpl", "loadOwner", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.line("OwnerServiceImpl", "loadOwner", "result: $result")
        return result
    }

    override suspend fun createOwner(email: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("OwnerServiceImpl", "createOwner", "creating owner -> email: $email")

        try {
            val name = email.split("@")[0]
            val resp = ownerRepository.createOwner(email, name)

            if (resp.result && resp.code == 200) {
                result = SimpleResponse(true, 200, "created", "")
                result.owner = resp.owner
            }
            else {
                result = SimpleResponse(false, resp.code, "not created", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("OwnerServiceImpl", "loadOwner", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.line("OwnerServiceImpl", "createOwner", "result: $result")
        return result
    }

    override suspend fun updateOwnerPlanningBooks(owner: Owner): SimpleResponse {

        var result: SimpleResponse
        Klog.line("OwnerServiceImpl", "updateOwnerPlanningBooks", "Updating Owner PBs -> owner.id: ${owner.id}")

        try {
            val resp = ownerRepository.updateOwnerPlanningBooks(owner.id, owner.planningBooks!!)

            if (resp.result && resp.code == 200) {
                result = SimpleResponse(true, 200, "updated", "")
            }
            else {
                result = SimpleResponse(false, resp.code, "not updated", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("OwnerServiceImpl", "updateOwnerPlanningBooks", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("OwnerServiceImpl", "updateOwnerPlanningBooks", "updated owner -> result: $result")
        return result
    }

    override suspend fun updateOwnerActivePlanningBook(pbID: String, ownerID: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("OwnerServiceImpl", "updateOwnerActivePlanningBook", "Updating Owner PBs -> ownerID: $ownerID")

        try {
            val resp = ownerRepository.updateOwnerActivePlanningBook(ownerID, pbID)
            Klog.line("OwnerServiceImpl", "updateOwnerActivePlanningBook", "Updated the Owner active pb")
            if (resp.result && resp.code == 200) {
                result = SimpleResponse(true, 200, "updated", "")
            }
            else {
                result = SimpleResponse(false, resp.code, "not updated", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("OwnerServiceImpl", "updateOwnerActivePlanningBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("OwnerServiceImpl", "updateOwnerActivePlanningBook", "updated owner -> result: $result")
        return result
    }

    override suspend fun sharePlanningBookToOtherOwner(shareToEmail: String, pbID: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("OwnerServiceImpl", "sharePlanningBookToOtherOwner", "sharing PlanningBook -> pbID: $pbID")

        try {

            val respSharedToOwner = getOwner(shareToEmail)
            Klog.line("OwnerServiceImpl", "sharePlanningBookToOtherOwner", "getting owner -> respSharedToOwner: $respSharedToOwner")

            if(respSharedToOwner.result && respSharedToOwner.code == 200) {
                val sharedToOwner: Owner = respSharedToOwner.owner!!

                val pb: String? = sharedToOwner.planningBooks!!.find { it -> it == pbID }
                if(null == pb) {
                    sharedToOwner.planningBooks!!.add(pbID)
                    updateOwnerPlanningBooks(sharedToOwner)

                    result = SimpleResponse(true, 200, "SharedToOwner PlanningBook List updated", "")
                }
                else {
                    result = SimpleResponse(true, 201, "SharedToOwner has the PlanningBook in its list yet", "")
                }
            }
            else {
                result = respSharedToOwner
            }
        }
        catch(e: Exception) {
            Klog.line("OwnerServiceImpl", "sharePlanningBookToOtherOwner", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("OwnerServiceImpl", "sharePlanningBookToOtherOwner", "result: $result")
        return result
    }

    /**
     * Elimina el PB de la lista de PBs del AppState.owner.
     * Actualiza el Repositorio con la nueva lista de PBs del owner.
     */
    override suspend fun forgetSharedPlanningBook(pbID: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("OwnerServiceImpl", "forgetSharedPlanningBook", "Forgetting shared PlanningBook -> pbID: $pbID")

        try {
            val existsPB = AppState.owner!!.planningBooks!!.find { it == pbID }
            Klog.line("OwnerServiceImpl", "forgetSharedPlanningBook", "existsPB: $existsPB")
            if(!existsPB.isNullOrEmpty()) {
                Klog.line("OwnerServiceImpl", "forgetSharedPlanningBook", "AppState.owner!!.planningBooks!!: ${ AppState.owner!!.planningBooks!! }")
                AppState.owner!!.planningBooks!!.remove(pbID)
                Klog.line("OwnerServiceImpl", "forgetSharedPlanningBook", "AppState.owner!!.planningBooks!!: ${ AppState.owner!!.planningBooks!! }")

                val resp = ownerRepository.updateOwnerPlanningBooks(AppState.owner!!.id, AppState.owner!!.planningBooks!!)
                if (resp.result && resp.code == 200) {
                    result = SimpleResponse(true, 200, "updated", "")
                }
                else {
                    result = SimpleResponse(false, resp.code, "not updated", resp.message)
                }
            }
            else {
                result = SimpleResponse(false, 404, "not updated", "Planning book doesn't exist")
            }
        }
        catch(e: Exception) {
            Klog.line("OwnerServiceImpl", "forgetSharedPlanningBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("OwnerServiceImpl", "forgetSharedPlanningBook", "updated owner -> result: $result")
        return result
    }

    override suspend fun listOwnersContainingPlanningBook(pbID: String): SimpleResponse {

        var result: SimpleResponse
        Klog.line("OwnerServiceImpl", "listOwnersContainingPlanningBook", "getting owners that containg this PB-> pbID: $pbID")

        try {
            val resp: SimpleDataResponse = ownerRepository.listOwnersByContainPB(pbID)
            if(resp.result && resp.code == 200) {
                result = SimpleResponse(true,200, "found", "")
                result.ownerList = resp.ownerList
            }
            else {
                result = SimpleResponse(false, resp.code, "not found", resp.message)
            }
        }
        catch(e: Exception) {
            Klog.line("OwnerServiceImpl", "listOwnersContainingPlanningBook", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleResponse(false, 500, e.localizedMessage, "" )
        }

        Klog.line("OwnerServiceImpl", "listOwnersContainingPlanningBook", "result: $result")
        return result
    }
}