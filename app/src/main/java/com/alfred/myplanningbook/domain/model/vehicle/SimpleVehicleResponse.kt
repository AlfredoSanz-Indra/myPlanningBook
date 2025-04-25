package com.alfred.myplanningbook.domain.model.vehicle

/**
 * @author Alfredo Sanz
 * @time 2025
 */
data class SimpleVehicleResponse(val result: Boolean,
                                 var code: Int,
                                 var message: String,
                                 val errorcode: String) {

    var vehicle: Vehicle? = null
}