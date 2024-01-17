package com.alfred.myplanningbook.core.validators

/**
 * @author Alfredo Sanz
 * @time 2024
 */
object TextValidatorCommon {

    fun matchPattern(password: String, passwordPattern: String): Boolean {
        password.let {
            val passwordMatcher = Regex(passwordPattern)

            return passwordMatcher.find(password) != null
        }
    }
}