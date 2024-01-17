package com.alfred.myplanningbook.core.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test
@DisplayName("Test TextValidator-Equals")
class TextValidatorEqualsFieldsTest {

    @Test
    @DisplayName("validate -> Correct")
    fun validate_isCorrect() {
        val chainTxt = ChainTextValidator(
            TextValidatorEqualsFields("Texto1")
        )
        val valResult = chainTxt.validate("Texto1")
        assertEquals(valResult, ValidatorResult.Success)

        val chainTxt2 = ChainTextValidator(
            TextValidatorEqualsFields("")
        )
        val valResult2 = chainTxt2.validate("")
        assertEquals(valResult2, ValidatorResult.Success)
    }

    @org.junit.jupiter.api.Test
    @DisplayName("validate -> InCorrect")
    fun validate_isNotCorrect() {
        val chainTxt = ChainTextValidator(
            TextValidatorEqualsFields("Texto1")
        )
        val valResult = chainTxt.validate("Texto2")
        assertTrue(valResult is ValidatorResult.Error)

        val valResult2 = chainTxt.validate("")
        assertTrue(valResult2 is ValidatorResult.Error)
    }
}