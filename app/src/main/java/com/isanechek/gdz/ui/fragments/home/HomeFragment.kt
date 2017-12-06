package com.isanechek.gdz.ui.fragments.home

import android.arch.lifecycle.Observer
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.isanechek.gdz.Screens
import com.isanechek.gdz.ui.fragments.base.BaseFragment
import com.isanechek.gdz.ui.fragments.base.ItemClickListener
import com.isanechek.gdz.ui.fragments.base.ViewModelFactory
import com.isanechek.gdz.ui.navigation.BackButtonListener
import com.isanechek.gdz.utils._text
import com.isanechek.gdz.utils.extensions.customLog
import com.isanechek.gdz.utils.extensions.emptyString
import com.isanechek.gdz.utils.extensions.extraWithKey
import com.isanechek.gdz.utils.extensions.nonSafeLazy
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView

/**
 * Created by isanechek on 11/29/17.
 */
class HomeFragment : BaseFragment(), AnkoComponent<Context>, AnkoLogger, BackButtonListener {

    private val viewModel by nonSafeLazy {
        ViewModelFactory.create(HomeViewModel::class.java)
    }

//    private var argumentsValue = emptyString
    private var argumentsValue = "5"
    private lateinit var timeline: RecyclerView
    private lateinit var adapter: HomeAdapter

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        argumentsValue = extraWithKey(ARGS)
        info("Args $argumentsValue")
    }

    override fun fragmentView(): View
            = context?.let { createView(AnkoContext.create(it)) }?: throw IllegalArgumentException("Home Fragment Context Null")

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        adapter = HomeAdapter()
        adapter.setItemClickListener(object: ItemClickListener {
            override fun itemClick(view: View, value: Any) {
                if (value is String) {
                    router.navigateTo(Screens.BOOKS_SCREEN, value)
                } else error("Item Click Value Is Not String")
            }

        })

        timeline.layoutManager = LinearLayoutManager(activity)
        timeline.setHasFixedSize(true)
        timeline.itemAnimator = DefaultItemAnimator()
        timeline.adapter = adapter
        initObserver()
        viewModel.load()
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        relativeLayout {
            backgroundColor = Color.WHITE
            lparams(width = matchParent, height = matchParent)
            when {
                !argumentsValue.isEmpty() -> // implementation empty layout
                    verticalLayout {
                        textView(_text.home_empty_text) {
                            textSize = 24f
                            textColor = Color.BLACK
                        }.lparams(width = wrapContent, height = wrapContent) {
                            setHorizontalGravity(1)
                        }
                        button(_text.home_empty_choose_button_text) {
                            textSize = 18f
                            textColor = Color.BLACK
                        }
                    }.lparams(width = wrapContent, height = wrapContent) {
                        centerInParent()
                    }
                else -> timeline = recyclerView()
            }
        }
    }

    override fun onBackPressed(): Boolean {
        router.exit()
        return true
    }

    private fun initObserver() {
        viewModel.isUpdating.observe(this, Observer { update ->
            if (update != null) {
                info("Update $update")
            }
        })
        viewModel.data.observe(this, Observer { data ->
            data?.let { adapter.setList(it) }
        })
    }

    companion object {
        private const val ARGS = "home.args"

        fun instance(value: Any?) : HomeFragment {
            val fragment = HomeFragment()
            if (value != null) {
                customLog.info("Home args $value")
//                fragment.withArgument(ARGS, value)
            }
            return fragment
        }
    }
}