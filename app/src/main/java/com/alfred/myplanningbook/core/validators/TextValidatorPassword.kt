package com.alfred.myplanningbook.core.validators

import com.alfred.myplanningbook.core.log.Klog

/**
 * @author Alfredo Sanz
 * @time 2023
 */
class TextValidatorPassword: TextValidator {

    override var validatorResult: ValidatorResult = ValidatorResult.NoResult

    override fun validate(thetext: String): ValidatorResult {

        validatorResult = when {
            thetext == null ->
                ValidatorResult.Error("The field value cannot be empty")

            !isValidPassword(thetext) ->
                ValidatorResult.Error("The password is not well formed")

            else ->
                ValidatorResult.Success
        }

        return validatorResult
    }

    /**
        Must meet at least three constraints
     */
    private fun isValidPassword(password: String) : Boolean {
        var count = 0
        if (TextValidatorCommon.matchPattern(password, ".*\\d.*")) {
            Klog.linedbg("TextValidatorPassword", "isValidPassword", "matchPattern \\d ok")
            count++;
        }
        if (TextValidatorCommon.matchPattern(password, ".*[a-z].*")) {
            Klog.linedbg("TextValidatorPassword", "isValidPassword", "matchPattern [a-z] ok")
            count++;
        }
        if (TextValidatorCommon.matchPattern(password, ".*[A-Z].*")) {
            Klog.linedbg("TextValidatorPassword", "isValidPassword", "matchPattern [A-Z] ok")
            count++;
        }

        Klog.linedbg("TextValidatorPassword", "isValidPassword", "count $count")
        return count >= 3
    }
}