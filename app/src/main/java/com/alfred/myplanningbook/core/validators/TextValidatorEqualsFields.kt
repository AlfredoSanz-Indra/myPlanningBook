package com.alfred.myplanningbook.core.validators

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class TextValidatorEqualsFields (private val thetext2: String): TextValidator {

    override var validatorResult: ValidatorResult = ValidatorResult.NoResult

    override fun validate(thetext: String): ValidatorResult {

        validatorResult = when {
            thetext == null || thetext2 == null ->
                ValidatorResult.Error("Both fields must be filled")

            thetext != thetext2 ->
                ValidatorResult.Error("Both values must be equal")

            else ->
                ValidatorResult.Success
        }

        return validatorResult
    }
}