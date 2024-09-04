package com.alfred.myplanningbook.core.validators

class TimeGreaterValidator {

    var validatorResult: ValidatorResult = ValidatorResult.NoResult

    fun validate(startHour: Int, endHour: Int, startMinutes: Int, endMinutes: Int): ValidatorResult {
        var valResult = 0
        valResult = checkout(startHour, endHour)
        if(valResult == 2) {
            valResult = checkout(startMinutes, endMinutes)
        }

        validatorResult = when {
            valResult == 3 -> {
                ValidatorResult.Error("The End time must be greater than Start time")
            }
            else ->
                ValidatorResult.Success
        }

        return validatorResult
    }

    private fun checkout(start: Int, end: Int): Int {
        return when {
            start < end -> 1
            start == end -> 2
            else -> 3
        }
    }


}