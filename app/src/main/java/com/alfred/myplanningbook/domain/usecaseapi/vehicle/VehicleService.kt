package com.alfred.myplanningbook.domain.usecaseapi.vehicle

import com.alfred.myplanningbook.domain.model.vehicle.SimpleVehicleResponse
import com.alfred.myplanningbook.domain.model.vehicle.Vehicle

/**
 * @author Alfredo Sanz
 * @time 2026
 */
interface VehicleService {

    suspend fun createVehicle(vehicle: Vehicle, userEmail: String): SimpleVehicleResponse

    suspend fun getVehicles(userEmail: String): SimpleVehicleResponse

    suspend fun deleteVehicle(userEmail: String, vehicleId: String): SimpleVehicleResponse


}