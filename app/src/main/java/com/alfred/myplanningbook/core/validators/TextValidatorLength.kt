package com.alfred.myplanningbook.core.validators

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class TextValidatorLength(val minLength: Int? = null,
                          val maxLength: Int? = null): TextValidator {

    override var validatorResult: ValidatorResult = ValidatorResult.NoResult

    override fun validate(theText: String): ValidatorResult {

        validatorResult = when {
            theText.isEmpty() || theText.trim().isEmpty()            ->
                ValidatorResult.Error("The field value cannot be empty")

            minLength != null && theText.trim().length < minLength ->
                ValidatorResult.Error("The field value must have $minLength chars at least")

            maxLength != null && theText.trim().length > maxLength ->
                ValidatorResult.Error("The field value cannot have more than $maxLength chars")

            else ->
                ValidatorResult.Success
        }

        return validatorResult
    }
}