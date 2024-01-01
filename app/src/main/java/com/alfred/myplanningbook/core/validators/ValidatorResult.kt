package com.alfred.myplanningbook.core.validators

/**
 * @author Alfredo Sanz
 * @time 2023
 */
sealed class ValidatorResult {
    class Error(val message: String) : ValidatorResult()
    object NoResult : ValidatorResult()
    object Success : ValidatorResult()
}