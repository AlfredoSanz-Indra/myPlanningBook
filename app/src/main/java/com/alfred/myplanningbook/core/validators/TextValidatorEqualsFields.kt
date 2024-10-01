package com.alfred.myplanningbook.core.validators

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class TextValidatorEqualsFields (private val theText2: String): TextValidator {

    override var validatorResult: ValidatorResult = ValidatorResult.NoResult

    override fun validate(theText: String): ValidatorResult {

        validatorResult = when {
            theText.isEmpty() || theText2.isEmpty() ->
                ValidatorResult.Error("Both fields must be filled")

            theText != theText2                 ->
                ValidatorResult.Error("Both values must be equal")

            else                                ->
                ValidatorResult.Success
        }

        return validatorResult
    }
}