package com.alfred.myplanningbook.domain.usecase.vehicle

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.domain.model.vehicle.SimpleVehicleResponse
import com.alfred.myplanningbook.domain.model.vehicle.Vehicle
import com.alfred.myplanningbook.domain.repositoryapi.vehicle.VehicleRepository
import com.alfred.myplanningbook.domain.usecaseapi.vehicle.VehicleService

/**
 * @author Alfredo Sanz
 * @time 2025
 */
class VehicleServiceImpl(private val vehicleRepository: VehicleRepository): VehicleService {

    override suspend fun createVehicle(vehicle: Vehicle, userEmail: String): SimpleVehicleResponse {
        Klog.line("VehicleServiceImpl", "createVehicle", "adding Vehicle -> vehicle: ${vehicle.name}")

        return try {
            val resp = vehicleRepository.insertVehicle(vehicle, userEmail)

            if (resp.result) {
                SimpleVehicleResponse(true, resp.code, resp.message, "").apply {
                    this.vehicle = resp.vehicle
                }
            } else {
                SimpleVehicleResponse(false, resp.code, "error", resp.message)
            }
        }
        catch (e: Exception) {
            Klog.line("VehicleServiceImpl", "createVehicle", "Exception: ${e.message}")
            SimpleVehicleResponse(false, 500, e.message ?: "Unknown error", "")
        }.also {
            Klog.linedbg("VehicleServiceImpl", "createVehicle", "result: $it")
        }
    }

    override suspend fun getVehicles(userEmail: String): SimpleVehicleResponse {
        Klog.line("VehicleServiceImpl", "getVehicles", "getting Vehicles")

        return try {
            val resp = vehicleRepository.getVehicles(userEmail)

            if (resp.result) {
                SimpleVehicleResponse(true, resp.code, resp.message, "").apply {
                    this.vehicleList = resp.vehicleList
                }
            } else {
                SimpleVehicleResponse(false, resp.code, "error", resp.message)
            }
        }
        catch (e: Exception) {
            Klog.line("VehicleServiceImpl", "getVehicles", "Exception: ${e.message}")
            SimpleVehicleResponse(false, 500, e.message ?: "Unknown error", "")
        }.also {
            Klog.linedbg("VehicleServiceImpl", "getVehicles", "result: $it")
        }
    }

    override suspend fun deleteVehicle(userEmail: String, vehicleId: String): SimpleVehicleResponse {
        Klog.line("VehicleServiceImpl", "deleteVehicle", "deleting Vehicle -> vehicleId: $vehicleId")

        return try {
            val resp = vehicleRepository.deleteVehicle(userEmail, vehicleId)

            if (resp.result) {
                SimpleVehicleResponse(true, resp.code, resp.message, "")
            }
            else {
                SimpleVehicleResponse(false, resp.code, "error", resp.message)
            }
        }
        catch (e: Exception) {
            Klog.line("VehicleServiceImpl", "deleteVehicle", "Exception: ${e.message}")
            SimpleVehicleResponse(false, 500, e.message ?: "Unknown error", "")
        }.also {
            Klog.linedbg("VehicleServiceImpl", "deleteVehicle", "result: $it")
        }
    }
}
