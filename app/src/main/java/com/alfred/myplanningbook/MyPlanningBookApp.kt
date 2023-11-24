package com.alfred.myplanningbook

import android.app.Application
import com.alfred.myplanningbook.core.di.appModule
import com.alfred.myplanningbook.core.firebase.FirebaseSession
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

/**
 * @author Alfredo Sanz
 * @time 2023
 */

class MyPlanningBookApp  : Application() {

    override fun onCreate() {
        super.onCreate()

        FirebaseSession.initFirebaseAuth()

        startKoin{

            androidLogger()
            androidContext(this@MyPlanningBookApp)
            modules(appModule)



        }
    }
}