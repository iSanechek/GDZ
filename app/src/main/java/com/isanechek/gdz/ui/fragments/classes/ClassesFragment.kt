package com.isanechek.gdz.ui.fragments.classes

import android.content.Context
import android.view.View
import com.isanechek.gdz.Screens
import com.isanechek.gdz.ui.fragments.base.BaseFragment
import com.isanechek.gdz.ui.navigation.BackButtonListener
import com.isanechek.gdz.utils.extensions.emptyString
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.relativeLayout

/**
 * Created by isanechek on 11/29/17.
 */
class ClassesFragment : BaseFragment(),
        AnkoComponent<Context>,
        AnkoLogger,
        BackButtonListener {

    private var classNumber = 0

    override fun fragmentView(): View = createView(AnkoContext.create(context!!))

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        relativeLayout {

        }
    }

    override fun onBackPressed(): Boolean {
        // Ну вдруг
        if (classNumber > 0) {
            router.navigateTo(Screens.BOOKS_SCREEN, classNumber)
            return true
        }
        router.navigateTo(Screens.HOME_SCREEN, emptyString)
        return true
    }

    companion object {
        fun instance() : ClassesFragment = ClassesFragment()
    }

}