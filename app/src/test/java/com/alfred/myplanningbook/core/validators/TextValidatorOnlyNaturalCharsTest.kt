package com.alfred.myplanningbook.core.validators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName

@DisplayName("Test TextValidator-NaturalChars")
class TextValidatorOnlyNaturalCharsTest {

    @org.junit.jupiter.api.Test
    @DisplayName("validate -> Correct")
    fun validate_isCorrect() {

        val chainTxt = ChainTextValidator(
            TextValidatorOnlyNaturalChars()
        )
        val valResult = chainTxt.validate("alfASFGL")
        assertEquals(valResult, ValidatorResult.Success)

        val valResult2 = chainTxt.validate("alfASFGL32-_")
        assertEquals(valResult2, ValidatorResult.Success)
    }

    @org.junit.jupiter.api.Test
    @DisplayName("validate -> InCorrect")
    fun validate_isNotCorrect() {

        val chainTxt = ChainTextValidator(
            TextValidatorOnlyNaturalChars()
        )
        val valResult = chainTxt.validate("alfAS%%&3=FGL")
        assertTrue(valResult is ValidatorResult.Error)

        val valResult2 = chainTxt.validate("alfAS.3FGL")
        assertTrue(valResult2 is ValidatorResult.Error)
    }
}