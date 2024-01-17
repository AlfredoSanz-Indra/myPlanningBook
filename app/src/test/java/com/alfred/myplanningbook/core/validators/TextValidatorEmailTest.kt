package com.alfred.myplanningbook.core.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test

@DisplayName("Test TextValidator-Email")
class TextValidatorEmailTest {

    @org.junit.jupiter.api.Test
    @DisplayName("validate -> Correct")
    fun validate_isCorrect() {
        val chainTxt = ChainTextValidator(
            TextValidatorEmail()
        )
        val valResult = chainTxt.validate("abcde@email.com")
        assertEquals(valResult, ValidatorResult.Success)

        val valResult2 = chainTxt.validate("abc@em.es")
        assertEquals(valResult2, ValidatorResult.Success)
    }

    @org.junit.jupiter.api.Test
    @DisplayName("validate -> InCorrect")
    fun validate_isNotCorrect() {
        val chainTxt = ChainTextValidator(
            TextValidatorEmail()
        )
        val valResult = chainTxt.validate("abcde@email")
        assertTrue(valResult is ValidatorResult.Error)

        val valResult2 = chainTxt.validate("abc")
        assertTrue(valResult2 is ValidatorResult.Error)

        val valResult3 = chainTxt.validate("abcemail.com")
        assertTrue(valResult3 is ValidatorResult.Error)
    }
}