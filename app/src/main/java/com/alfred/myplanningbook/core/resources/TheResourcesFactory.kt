package com.alfred.myplanningbook.core.resources

import android.content.Context
import com.alfred.myplanningbook.domain.model.library.LibraryMasters
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream
import java.util.Properties


/**
 * @author Alfredo Sanz
 * @time 2023
 */

/**
 * Read a properties file from resources path.
 * File must be placed in the main/assets folder
 */
fun readResources(fileName: String, baseContext: Context): Properties {
    val properties = Properties()

    val assetsManager = baseContext.assets
    val inSt: InputStream = assetsManager.open(fileName)
    Properties().apply {
        properties.load(inSt)
    }

    return properties
}

/**
 * Parse library-master json file
 * File must be placed in the main/assets folder
 */
fun readJsonResources(fileName: String, baseContext: Context): LibraryMasters {
    val assetsManager = baseContext.assets
    val inputSt: InputStream = assetsManager.open(fileName)

    val result = Json.decodeFromStream<LibraryMasters>(inputSt)
    return result
}

