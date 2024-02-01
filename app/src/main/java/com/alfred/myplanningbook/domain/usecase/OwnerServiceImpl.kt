package com.alfred.myplanningbook.domain.usecase

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataResponse
import com.alfred.myplanningbook.domain.AppState
import com.alfred.myplanningbook.domain.model.Owner
import com.alfred.myplanningbook.domain.model.SimpleResponse
import com.alfred.myplanningbook.domain.repositoryapi.OwnerRepository
import com.alfred.myplanningbook.domain.usecaseapi.OwnerService
import com.alfred.myplanningbook.domain.usecaseapi.PlanningBookService

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
        Klog.line("OwnerServiceImpl", "updateOwnerPlanningBooks", "Updating Owner PBs -> owner.id: $owner.id")

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
}