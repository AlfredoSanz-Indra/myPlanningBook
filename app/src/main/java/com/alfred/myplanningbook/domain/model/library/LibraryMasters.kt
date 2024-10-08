package com.alfred.myplanningbook.domain.model.library

import kotlinx.serialization.Serializable

/**
 * @author Alfredo Sanz
 * @time 2024
 */
@Serializable
data class LibraryMasters(@Serializable val masters: List<LibraryMaster>)
