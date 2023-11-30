package com.alfred.myplanningbook.core.resources

import android.content.Context
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

