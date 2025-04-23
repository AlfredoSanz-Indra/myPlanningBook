package com.alfred.myplanningbook.domain.model.vehicle

/**
 * @author Alfredo Sanz
 * @time 2023
 */
data class Vehicle(var id: String?,
                   val name: String,
                   var model: String?,
                   var notes: String?,
                   var dateInMillis: Long,
                   val year: Int,
                   val month: Int,
                   val day: Int, )
