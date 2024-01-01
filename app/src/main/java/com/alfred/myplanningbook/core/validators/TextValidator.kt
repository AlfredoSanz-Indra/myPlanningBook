package com.alfred.myplanningbook.core.validators

/**
 * @author Alfredo Sanz
 * @time 2023
 */
interface TextValidator {
    fun validate(stringToValidate: String): ValidatorResult
    var validatorResult: ValidatorResult
}