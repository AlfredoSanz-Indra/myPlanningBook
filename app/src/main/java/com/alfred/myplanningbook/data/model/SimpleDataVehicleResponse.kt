package com.alfred.myplanningbook.data.model

import com.alfred.myplanningbook.domain.model.vehicle.Vehicle

/**
 * @author Alfredo Sanz
 * @time 2025
 */
data class SimpleDataVehicleResponse(val result: Boolean,
                                     val code: Int,
                                     var message: String) {
    var vehicle: Vehicle? = null
}
