package com.isanechek.gdz

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.isanechek.gdz.data.Repository
import com.isanechek.gdz.data.database.GdzDataBase
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import ru.terrakok.cicerone.Router

/**
 * Created by isanechek on 11/29/17.
 */
class GdzApplication : Application() {
    val repository: Repository = Repository(this)

    private lateinit var cicerone: Cicerone<Router>

    override fun onCreate() {
        super.onCreate()
        instance = this

        cicerone = Cicerone.create()
    }

    val navigatorHolder: NavigatorHolder
        get() = cicerone.navigatorHolder

    val router: Router
        get() = cicerone.router

    fun getPreferences() : SharedPreferences = applicationContext.getSharedPreferences("default", Context.MODE_PRIVATE) // FIX ME


    companion object{
        const val DEFAULT_PREF_KEY = "gdz.pref"
        lateinit var instance: GdzApplication
    }
}