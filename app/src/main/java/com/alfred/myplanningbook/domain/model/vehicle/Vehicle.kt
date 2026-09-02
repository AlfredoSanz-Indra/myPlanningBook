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
                   var saleDateInMillis: Long? = null,
                   var saleYear: Int? = null,
                   var saleMonth: Int? = null,
                   var saleDay: Int? = null)
