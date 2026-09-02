package com.alfred.myplanningbook.domain.repositoryapi.vehicle

import com.alfred.myplanningbook.data.model.SimpleDataVehicleResponse
import com.alfred.myplanningbook.domain.model.vehicle.Vehicle

/**
 * @author Alfredo Sanz
 * @time 2026
 */
interface VehicleRepository {

    suspend fun insertVehicle(entity: Vehicle, userEmail: String): SimpleDataVehicleResponse

    suspend fun getVehicles(userEmail: String): SimpleDataVehicleResponse

    suspend fun deleteVehicle(userEmail: String, vehicleId: String): SimpleDataVehicleResponse

}