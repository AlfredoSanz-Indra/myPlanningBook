package com.alfred.myplanningbook.core.validators

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName

@DisplayName("Test TimeGreaterValidator")
class TimeGreaterValidatorTest {
    @org.junit.jupiter.api.Test
    @DisplayName("validate -> Correct")
    fun validate_isCorrect() {
        val timeGreaterVal = TimeGreaterValidator()

        val valResult = timeGreaterVal.validate(18, 19, 20, 0)
        assertEquals(valResult, ValidatorResult.Success)

        val valResult2 = timeGreaterVal.validate(10, 10, 20, 20)
        assertEquals(valResult2, ValidatorResult.Success)
    }

    @org.junit.jupiter.api.Test
    @DisplayName("validate -> InCorrect")
    fun validate_isNotCorrect() {
        val timeGreaterVal = TimeGreaterValidator()

        val valResult = timeGreaterVal.validate(10, 9, 20, 20)
        assertTrue(valResult is ValidatorResult.Error)

        val valResult2 = timeGreaterVal.validate(10, 10, 20, 19)
        assertTrue(valResult2 is ValidatorResult.Error)

        val valResult3 = timeGreaterVal.validate(10, 8, 20, 30)
        assertTrue(valResult3 is ValidatorResult.Error)
    }
}