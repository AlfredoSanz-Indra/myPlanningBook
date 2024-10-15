package com.alfred.myplanningbook.core.resources

import android.content.Context
import com.alfred.myplanningbook.domain.model.library.LibraryMasters
import java.util.Properties

/**
 * @author Alfredo Sanz
 * @time 2024
 */
object TheResources {

    private lateinit var appProp: Properties
    private lateinit var libraryMasters: LibraryMasters

    fun getAppProp(baseContext: Context): Properties {
        if (!this::appProp.isInitialized) {
            this.appProp = readResources("app.properties", baseContext)
        }
        return this.appProp
    }

    fun getLibraryMasters(baseContext: Context): LibraryMasters {
        if (!this::libraryMasters.isInitialized) {
            this.libraryMasters = readJsonResources("library-master.json", baseContext)
        }
        return this.libraryMasters
    }
}