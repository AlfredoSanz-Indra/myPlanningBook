package com.alfred.myplanningbook.domain.model.library

import kotlinx.serialization.Serializable

/**
 * @author Alfredo Sanz
 * @time 2024
 */
@Serializable
class LibraryMaster(val name: String, @Serializable val values: List<LMaster>)

