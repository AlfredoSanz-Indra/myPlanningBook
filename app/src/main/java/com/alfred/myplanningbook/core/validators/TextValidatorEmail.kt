package com.alfred.myplanningbook.core.validators

import android.util.Patterns
import androidx.core.util.PatternsCompat

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class TextValidatorEmail: TextValidator {

    override var validatorResult: ValidatorResult = ValidatorResult.NoResult

    override fun validate(theText: String): ValidatorResult {

        validatorResult = when {
            theText.isEmpty() ->
                ValidatorResult.Error("The field value cannot be empty")

            !PatternsCompat.EMAIL_ADDRESS.matcher(theText.trim()).matches() ->
                ValidatorResult.Error("The email is not valid")

            else ->
                ValidatorResult.Success
        }

        return validatorResult
    }
}