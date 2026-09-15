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
                   val day: Int,
                   var terminationDateInMillis: Long? = null,
                   var terminationYear: Int? = null,
                   var terminationMonth: Int? = null,
                   var terminationDay: Int? = null)
