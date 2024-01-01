package com.alfred.myplanningbook.core.validators

import android.util.Patterns

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class TextValidatorEmail: TextValidator {

    override var validatorResult: ValidatorResult = ValidatorResult.NoResult

    override fun validate(thetext: String): ValidatorResult {

        validatorResult = when {
            thetext == null ->
                ValidatorResult.Error("The field value cannot be empty")

            !Patterns.EMAIL_ADDRESS.matcher(thetext.trim()).matches() ->
                ValidatorResult.Error("The email is not valid")

            else ->
                ValidatorResult.Success
        }

        return validatorResult
    }
}