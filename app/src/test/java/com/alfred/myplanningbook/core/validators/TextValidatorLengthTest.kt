package com.alfred.myplanningbook.core.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test
@DisplayName("Test TextValidator-Length")
class TextValidatorLengthTest {

    @Test
    @DisplayName("validate -> Correct")
    fun validate_isCorrect() {
        val chainTxt = ChainTextValidator(
            TextValidatorLength(5, 10)
        )
        val valResult = chainTxt.validate("ABcd5")
        assertEquals(valResult, ValidatorResult.Success)

        val valResult2 = chainTxt.validate("123gjieRtp")
        assertEquals(valResult2, ValidatorResult.Success)
    }

    @org.junit.jupiter.api.Test
    @DisplayName("validate -> InCorrect")
    fun validate_isNotCorrect() {
        val chainTxt = ChainTextValidator(
            TextValidatorLength(5, 10)
        )
        val valResult = chainTxt.validate("abcde@email")
        assertTrue(valResult is ValidatorResult.Error)

        val valResult2 = chainTxt.validate("abcD")
        assertTrue(valResult2 is ValidatorResult.Error)

        val valResult3 = chainTxt.validate("a")
        assertTrue(valResult3 is ValidatorResult.Error)

        val valResult4 = chainTxt.validate("")
        assertTrue(valResult4 is ValidatorResult.Error)
    }
}