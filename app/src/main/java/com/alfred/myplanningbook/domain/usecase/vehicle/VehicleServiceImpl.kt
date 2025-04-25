package com.alfred.myplanningbook.domain.usecase.vehicle

import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.data.model.SimpleDataVehicleResponse
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
        var result: SimpleVehicleResponse
        Klog.line("VehicleServiceImpl", "createVehicle", "adding Vehicle -> vehicle: ${vehicle.name}")

        try {
            val resp: SimpleDataVehicleResponse = vehicleRepository.insertVehicle(vehicle, userEmail)
            Klog.linedbg("VehicleServiceImpl", "createVehicle", "resp: $resp")

            if(!resp.result) {
                result = SimpleVehicleResponse(false, resp.code, "error", resp.message)
            }
            else {
                result = SimpleVehicleResponse(true, resp.code, resp.message, "")
                result.vehicle = resp.vehicle
            }
        }
        catch(e: Exception) {
            Klog.line("VehicleServiceImpl", "createVehicle", " Exception localizedMessage: ${e.localizedMessage}")
            result = SimpleVehicleResponse(false, 500, e.localizedMessage, "")
        }

        Klog.linedbg("VehicleServiceImpl", "createVehicle", "result: $result")
        return result
    }
}