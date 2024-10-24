package com.alfred.myplanningbook.data.model

/**
 * @author Alfredo Sanz
 * @time 2024
 */
object Documents {

    const val OWNER_NAME: String = "NAME"
    const val OWNER_EMAIL: String = "EMAIL"
    const val OWNER_ACTIVEPLANNINGBOOK: String = "ACTIVEPLANNINGBOOK"
    const val OWNER_PLANNINGBOOKS: String = "PLANNINGBOOKS"

    const val PLANNINGBOOK_NAME: String = "NAME"
    const val PLANNINGBOOK_IDOWNER: String = "IDOWNER"

    const val TASKBOOK_NAME: String = "NAME"
    const val TASKBOOK_PLANNINGBOOK_ID: String = "PLANNINGBOOK_ID"
    const val TASKBOOK_DESC: String = "DESCRIPTION"
    const val TASKBOOK_DATE_MILLIS: String = "DATE_MILLIS"
    const val TASKBOOK_YEAR: String = "YEAR"
    const val TASKBOOK_MONTH: String = "MONTH"
    const val TASKBOOK_DAY: String = "DAY"
    const val TASKBOOK_HOUR: String = "HOUR"
    const val TASKBOOK_MINUTE: String = "MINUTE"
    const val TASKBOOK_END_HOUR: String = "END_HOUR"
    const val TASKBOOK_END_MINUTE: String = "END_MINUTE"
    const val TASKBOOK_NATURE: String = "NATURE"

    const val ACTIVITYBOOK_NAME: String = "NAME"
    const val ACTIVITYBOOK_PLANNINGBOOK_ID: String = "PLANNINGBOOK_ID"
    const val ACTIVITYBOOK_DESC: String = "DESCRIPTION"
    const val ACTIVITYBOOK_START_HOUR: String = "START_HOUR"
    const val ACTIVITYBOOK_START_MINUTE: String = "START_MINUTE"
    const val ACTIVITYBOOK_END_HOUR: String = "END_HOUR"
    const val ACTIVITYBOOK_END_MINUTE: String = "END_MINUTE"
    const val ACTIVITYBOOK_WEEKDAYS: String = "WEEK_DAYS"
    const val ACTIVITYBOOK_ISACTIVE: String = "IS_ACTIVE"

    const val LIBRARYBOOK_USEREMAIL: String = "USER_EMAIL"
    const val LIBRARYBOOK_TITLE: String = "TITLE"
    const val LIBRARYBOOK_AUTHOR: String = "AUTHOR"
    const val LIBRARYBOOK_SUBTITLE: String = "SUBTITLE"
    const val LIBRARYBOOK_NOTES: String = "NOTES"
    const val LIBRARYBOOK_CATEGORY: String = "CATEGORY"
    const val LIBRARYBOOK_SAGA: String = "SAGA"
    const val LIBRARYBOOK_SAGA_INDEX: String = "SAGA_INDEX"
    const val LIBRARYBOOK_PUBLISHER: String = "PUBLISHER"
    const val LIBRARYBOOK_FORMAT: String = "FORMAT"
    const val LIBRARYBOOK_LANGUAGE: String = "LANGUAGE"
    const val LIBRARYBOOK_READ: String = "READ"
    const val LIBRARYBOOK_HAVE: String = "HAVE"
    const val LIBRARYBOOK_READYEAR: String = "READYEAR"

    const val LIBRARYAUTHORS_NAME: String = "NAME"
    const val LIBRARYAUTHORS_USEREMAIL: String = "USER_EMAIL"

    const val LIBRARYCATEGORY_NAME: String = "NAME"
    const val LIBRARYCATEGORY_USEREMAIL: String = "USER_EMAIL"

    const val LIBRARYPUBLISHER_NAME: String = "NAME"
    const val LIBRARYPUBLISHER_USEREMAIL: String = "USER_EMAIL"

    const val LIBRARYSAGA_NAME: String = "NAME"
    const val LIBRARYSAGA_USEREMAIL: String = "USER_EMAIL"
}
