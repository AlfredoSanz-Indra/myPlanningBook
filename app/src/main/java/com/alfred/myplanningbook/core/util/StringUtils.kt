package com.alfred.myplanningbook.core.util


/**
 * @author Alfredo Sanz
 * @time 2024
 */
class StringUtils {

    /**
     * Capitalize the first letter of the sentence and lowercase the rest.
     */
    companion object {
        fun capitalizeString(words: String): String {
            if(words.isNullOrBlank()) {
                return words
            }
            val lowWords = words.lowercase()
            val lowWordsList: MutableList<String> = lowWords.split(" ").toMutableList()
            val firstWord = lowWordsList[0].replaceFirstChar { char ->
                char.titlecase()
            }
            lowWordsList[0] = firstWord
            return lowWordsList.joinToString(" ")
        }
    }
}