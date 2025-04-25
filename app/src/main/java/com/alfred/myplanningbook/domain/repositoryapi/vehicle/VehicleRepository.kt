package com.alfred.myplanningbook.domain.repositoryapi.vehicle

import com.alfred.myplanningbook.data.model.SimpleDataVehicleResponse
import com.alfred.myplanningbook.domain.model.vehicle.Vehicle

/**
 * @author Alfredo Sanz
 * @time 2025
 */
interface VehicleRepository {

    suspend fun insertVehicle(entity: Vehicle, userEmail: String): SimpleDataVehicleResponse

}