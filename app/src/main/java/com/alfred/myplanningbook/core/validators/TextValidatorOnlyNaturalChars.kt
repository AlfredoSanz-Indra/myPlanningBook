package com.alfred.myplanningbook.core.validators

import com.alfred.myplanningbook.core.log.Klog

/**
 * @author Alfredo Sanz
 * @time 2024
 */
class TextValidatorOnlyNaturalChars: TextValidator {

    override var validatorResult: ValidatorResult = ValidatorResult.NoResult

    override fun validate(theText: String): ValidatorResult {

        validatorResult = when {
            theText.isEmpty() ->
                ValidatorResult.Error("The field value cannot be empty")

            !onlyHasNaturalChars(theText) ->
                ValidatorResult.Error("The field has not allowed chars")

            else                          ->
                ValidatorResult.Success
        }

        return validatorResult
    }

    private fun onlyHasNaturalChars(txt: String) : Boolean {

        var result = false
        if (TextValidatorCommon.matchPattern(txt, "^[a-zA-Z0-9_-]*\$")) {
            Klog.linedbg("TextValidatorOnlyNaturalChars", "onlyHasNaturalChars", "matchPattern natural ok")
            result = true
        }

        Klog.linedbg("TextValidatorOnlyNaturalChars", "onlyHasNaturalChars", "result $result")
        return result
    }
}