package com.alfred.myplanningbook.core.validators

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.DisplayName

@DisplayName("Test TextValidator-OnlyNumbers")
class TextValidatorOnlyNumbers {
    @org.junit.jupiter.api.Test
    @DisplayName("validate -> Correct")
    fun validate_isCorrect() {

        val chainTxt = ChainTextValidator(
            TextValidatorOnlyNumber()
        )
        val valResult = chainTxt.validate("1892")
        assertEquals(valResult, ValidatorResult.Success)

        val valResult2 = chainTxt.validate("0")
        assertEquals(valResult2, ValidatorResult.Success)
    }

    @org.junit.jupiter.api.Test
    @DisplayName("validate -> InCorrect")
    fun validate_isNotCorrect() {

        val chainTxt = ChainTextValidator(
            TextValidatorOnlyNumber()
        )
        val valResult = chainTxt.validate("293=FGL")
        assertTrue(valResult is ValidatorResult.Error)

        val valResult2 = chainTxt.validate("dos345")
        assertTrue(valResult2 is ValidatorResult.Error)
    }
}