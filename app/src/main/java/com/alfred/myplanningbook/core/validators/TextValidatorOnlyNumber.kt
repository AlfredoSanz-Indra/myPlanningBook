package com.alfred.myplanningbook.core.validators

import com.alfred.myplanningbook.core.log.Klog

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class TextValidatorOnlyNumber : TextValidator {


    override var validatorResult: ValidatorResult = ValidatorResult.NoResult

    override fun validate(theText: String): ValidatorResult {

        validatorResult = when {
            theText.isEmpty() ->
                ValidatorResult.Error("The field value cannot be empty")

            !onlyHasNumberChars(theText) ->
                ValidatorResult.Error("The field only accept numbers")

            else                          ->
                ValidatorResult.Success
        }

        return validatorResult
    }

    private fun onlyHasNumberChars(txt: String) : Boolean {

        var result = false
        if (TextValidatorCommon.matchPattern(txt, "^[0-9]*\$")) {
            Klog.linedbg("TextValidatorOnlyNumber", "onlyHasNumberChars", "matchPattern natural ok")
            result = true
        }

        Klog.linedbg("TextValidatorOnlyNumber", "onlyHasNumberChars", "result $result")
        return result
    }
}