package com.alfred.myplanningbook

import android.app.Application
import com.alfred.myplanningbook.core.di.appModule
import com.alfred.myplanningbook.core.firebase.FirebaseSession
import com.alfred.myplanningbook.core.log.Klog
import com.alfred.myplanningbook.core.log.KlogLevel
import com.alfred.myplanningbook.core.resources.TheResources
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

/**
 * @author Alfredo Sanz
 * @time 2023
 * @version 1.4.10
 */
class MyPlanningBookApp  : Application() {

    override fun onCreate() {
        super.onCreate()

        try {
            initApp()
        }
        catch(e: Exception){
            println("*** Error  initializating ${e.localizedMessage}")
            throw e
        }

        startKoin{
            androidLogger()
            androidContext(this@MyPlanningBookApp)
            modules(appModule)
        }
    }

    private fun initApp() {
        val logLevel: String = TheResources.getAppProp(baseContext)["log.level"] as String
        when(logLevel) {
            "DEBUG" -> Klog.updateLevel(KlogLevel.DEBUG)
            "PROD"  -> Klog.updateLevel(KlogLevel.PROD)
        }

        val firebaseEmu: String = TheResources.getAppProp(baseContext)["firebase.emulator"] as String
        val useEmulator: Boolean = firebaseEmu.toBoolean()
        FirebaseSession.useEmulator = useEmulator
        FirebaseSession.initFirebaseAuth()
        FirebaseSession.initFirebaseDB()
    }
}