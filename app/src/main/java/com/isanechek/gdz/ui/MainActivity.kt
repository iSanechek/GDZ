package com.isanechek.gdz.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.isanechek.gdz.GdzApplication
import com.isanechek.gdz.Screens.BOOKS_SCREEN
import com.isanechek.gdz.Screens.CLASSES_SCREEN
import com.isanechek.gdz.Screens.HOME_SCREEN
import com.isanechek.gdz.Screens.JOBS_SCREEN
import com.isanechek.gdz.Screens.JOB_SCREEN
import com.isanechek.gdz.ui.fragments.base.ViewModelFactory
import com.isanechek.gdz.ui.fragments.books.BooksFragment
import com.isanechek.gdz.ui.fragments.classes.ClassesFragment
import com.isanechek.gdz.ui.fragments.home.HomeFragment
import com.isanechek.gdz.ui.fragments.jobs.JobsFragment
import com.isanechek.gdz.ui.fragments.jobviewer.JobViewerFragment
import com.isanechek.gdz.ui.navigation.BackButtonListener
import com.isanechek.gdz.ui.navigation.MainViewModel
import com.isanechek.gdz.utils._id
import com.isanechek.gdz.utils.extensions.emptyString
import com.isanechek.gdz.utils._text
import org.jetbrains.anko.*
import ru.terrakok.cicerone.android.SupportFragmentNavigator
import ru.terrakok.cicerone.commands.*

class MainActivity : AppCompatActivity(), AnkoComponent<Context>, AnkoLogger {

    private val screens = mapOf(HOME_SCREEN to _text.app_name,
            CLASSES_SCREEN to _text.classe_fragment_name,
            BOOKS_SCREEN to _text.books_fragment_name,
            JOBS_SCREEN to _text.jobs_fragment_name,
            JOB_SCREEN to _text.job_fragment_name)

    private var keyScreen = emptyString
    private lateinit var viewModel: MainViewModel

    private val navigator = object: SupportFragmentNavigator(supportFragmentManager, _id.fragment_container) {
        override fun createFragment(screenKey: String?, data: Any?): Fragment = when (screenKey) {
            HOME_SCREEN -> HomeFragment.instance(data)
            CLASSES_SCREEN -> ClassesFragment.instance()
            BOOKS_SCREEN -> BooksFragment.instance(data)
            JOBS_SCREEN -> JobsFragment.instance(data)
            JOB_SCREEN -> JobViewerFragment.instance(data)
            else -> HomeFragment.instance(data)
        }

        override fun exit() {
            finish()
        }

        override fun applyCommand(command: Command?) {
            super.applyCommand(command)
            when (command) {
                is Replace -> keyScreen = command.screenKey
                is Forward -> keyScreen = command.screenKey
                is BackTo -> keyScreen = command.screenKey
            }
        }

        override fun showSystemMessage(message: String?) {
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(createView(AnkoContext.create(this)))
        viewModel = ViewModelFactory.create(MainViewModel::class.java)
        // for test
        if (savedInstanceState == null) {
            navigator.applyCommand(Replace(HOME_SCREEN, screens[HOME_SCREEN]))
        }

//        if (savedInstanceState == null) {
//            when {
//                firstStart() -> navigator.applyCommand(Replace(CLASSES_SCREEN, screens[CLASSES_SCREEN]))
//                else -> navigator.applyCommand(Replace(HOME_SCREEN, screens[HOME_SCREEN]))
//            }
//        } else {
//            val screen = savedInstanceState.getString(SAVE_FRAGMENT)
//            navigator.applyCommand(Replace(screen, screens[screen]))
//        }
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        frameLayout {
            id = _id.fragment_container
        }
    }

    override fun onResume() {
        super.onResume()
        GdzApplication.instance.navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        GdzApplication.instance.navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.apply { putString(SAVE_FRAGMENT, keyScreen) }
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(_id.fragment_container)
        if (fragment != null
                && fragment is BackButtonListener
                && (fragment as BackButtonListener).onBackPressed()) {
            return
        } else super.onBackPressed()
    }

    private fun firstStart(): Boolean {
        val value = defaultSharedPreferences.getBoolean(FIRST_START, true)
        if (value) {
            defaultSharedPreferences
                    .edit()
                    .putBoolean(FIRST_START, false)
                    .apply()
            return true
        }
        return false
    }


    companion object {
        private const val FIRST_START = "first.start"
        private const val SAVE_FRAGMENT = "save.fragment"
    }
}
