package com.alfred.myplanningbook.core.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

import org.junit.jupiter.api.Test

@DisplayName("Test TextValidator-Password")
class TextValidatorPasswordTest {

    @Test
    @DisplayName("validate -> Correct")
    fun validate_isCorrect() {
        val chainTxt = ChainTextValidator(
            TextValidatorPassword()
        )
        val valResult = chainTxt.validate("ABcde17")
        assertEquals(valResult, ValidatorResult.Success)

        val valResult2 = chainTxt.validate("309AAa")
        assertEquals(valResult2, ValidatorResult.Success)
    }

    @org.junit.jupiter.api.Test
    @DisplayName("validate -> InCorrect")
    fun validate_isNotCorrect() {
        val chainTxt = ChainTextValidator(
            TextValidatorPassword()
        )
        val valResult = chainTxt.validate("abcde@email")
        assertTrue(valResult is ValidatorResult.Error)

        val valResult2 = chainTxt.validate("abcDF")
        assertTrue(valResult2 is ValidatorResult.Error)

        val valResult3 = chainTxt.validate("abcemail.com")
        assertTrue(valResult3 is ValidatorResult.Error)

        val valResult4 = chainTxt.validate("309AA")
        assertTrue(valResult4 is ValidatorResult.Error)

        val valResult5 = chainTxt.validate("")
        assertTrue(valResult5 is ValidatorResult.Error)
    }
}