package com.alfred.myplanningbook.core.validators

import com.alfred.myplanningbook.core.log.Klog

class TimeGreaterValidator {

    var validatorResult: ValidatorResult = ValidatorResult.NoResult

    fun validate(startHour: Int, endHour: Int, startMinutes: Int, endMinutes: Int): ValidatorResult {
        var count = 0
        if(checkout(startHour, endHour)) {
            count ++;
        }
        if(checkout(startMinutes, endMinutes)) {
            count ++;
        }
        validatorResult = when {
            count < 2 -> {
                ValidatorResult.Error("The End hour must be greater than Start hour")
            }

            else ->
                ValidatorResult.Success
        }

        return validatorResult
    }

    private fun checkout(start: Int, end: Int): Boolean {
        return when {
            start <= end -> true
            else -> false
        }
    }


}