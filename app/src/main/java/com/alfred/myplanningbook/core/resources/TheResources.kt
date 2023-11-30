package com.alfred.myplanningbook.core.resources

import android.content.Context
import java.util.Properties

/**
 * @author Alfredo Sanz
 * @time 2023
 */
object TheResources {

    private lateinit var appProp: Properties

    fun getAppProp(baseContext: Context): Properties {
        if (!this::appProp.isInitialized) {
            this.appProp = readResources("app.properties", baseContext)
        }
        return this.appProp
    }
}