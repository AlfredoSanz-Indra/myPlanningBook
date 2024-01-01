package com.alfred.myplanningbook.core.validators

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class ChainTextValidator(private vararg val validators: TextValidator) : TextValidator {

    override var validatorResult: ValidatorResult = ValidatorResult.NoResult

    override fun validate(theText: String): ValidatorResult {

        validators.forEach { validator ->
            validatorResult = validator.validate(theText)

            if (validatorResult is ValidatorResult.Error) {
                return validatorResult
            }
        }
        return validatorResult
    }
}