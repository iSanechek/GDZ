package com.isanechek.gdz.ui.fragments.books

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.isanechek.gdz.Screens
import com.isanechek.gdz.data.database.entity.Author
import com.isanechek.gdz.ui.fragments.base.BaseFragment
import com.isanechek.gdz.ui.fragments.base.ItemClickListener
import com.isanechek.gdz.ui.fragments.base.ViewModelFactory
import com.isanechek.gdz.ui.navigation.BackButtonListener
import com.isanechek.gdz.utils._id
import com.isanechek.gdz.utils._text
import com.isanechek.gdz.utils.extensions.*
import org.jetbrains.anko.*
import org.jetbrains.anko.recyclerview.v7.recyclerView
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.swipeRefreshLayout

/**
 * Created by isanechek on 11/30/17.
 */
class BooksFragment : BaseFragment(),
        AnkoComponent<Context>,
        BackButtonListener,
        AnkoLogger {

    private val viewModel by nonSafeLazy {
        ViewModelFactory.create(BooksViewModel::class.java)
    }

    private lateinit var timeline: RecyclerView
    private lateinit var refresh: SwipeRefreshLayout
    private lateinit var adapter: BooksAdapter
    private lateinit var progress: ProgressBar
    private lateinit var errorText: TextView
    private var category = emptyString

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        category = extraWithKey(ARGS)
    }

    override fun fragmentView(): View = createView(AnkoContext.create(context!!))

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refresh = view.find(_id.books_fragment_refresh)
        timeline = view.find(_id.books_fragment_timeline)
        progress = view.find(_id.books_fragment_progress)
        errorText = view.find(_id.books_fragment_error_tv)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        refresh.onRefresh {
            viewModel.update(category)
        }

        adapter = BooksAdapter()
        adapter.setItemClickListener(object: ItemClickListener {
            override fun itemClick(view: View, value: Any) {
                if (value is Author) {
                    info("Click Item ${value.title}")
                    router.navigateTo(Screens.JOBS_SCREEN, value.id)
                } else error("Click Item value is not Author $value")
            }
        })

        timeline.layoutManager = LinearLayoutManager(activity)
        timeline.setHasFixedSize(true)
        timeline.itemAnimator = DefaultItemAnimator()
        timeline.adapter = adapter
        initObserver()
    }

    override fun createView(ui: AnkoContext<Context>): View = with(ui){
        relativeLayout {
            lparams(width = matchParent, height = matchParent)

            swipeRefreshLayout {
                id = _id.books_fragment_refresh
                visibility = View.GONE
                recyclerView {
                    id = _id.books_fragment_timeline
                }
            }.lparams(width = matchParent, height = matchParent)

            progressBar {
                id = _id.books_fragment_progress
                isIndeterminate = true
                visibility = View.GONE
            }.lparams {
                centerInParent()
            }

            textView(_text.books_fragment_error_text) {
                id = _id.books_fragment_error_tv
                textSize = 22F
                textColor = Color.RED
                visibility = View.GONE
            }.lparams {
                centerInParent()
                leftMargin = dip(16)
                rightMargin = dip(16)
            }

        }
    }

    override fun onBackPressed(): Boolean {
        router.backTo(Screens.HOME_SCREEN)
        return true
    }

    @SuppressLint("WrongConstant")
    private fun initObserver() {
        viewModel.isUpdating.observe(this, Observer { status ->
            if (status != null) {
                if (status) {
                    if (refresh.visibility == View.GONE) progress.visibility = View.VISIBLE
                    if (refresh.visibility == View.VISIBLE) refresh.isRefreshing = true
                } else {
                    if (progress.visibility == View.VISIBLE) progress.visibility = View.GONE
                    if (refresh.visibility == View.VISIBLE) refresh.isRefreshing = false
                }
            }
        })

        viewModel.data(category).observe(this, Observer { data ->
            if (data != null) {
                info("Data Size ${data.size}")
                when {
                    data.size > 0 -> {
                        if (refresh.visibility == View.GONE) {
                            if (errorText.visibility == View.VISIBLE) errorText.visibility = View.GONE
                            if (progress.visibility == View.VISIBLE) progress.visibility = View.GONE
                            refresh.visibility = View.VISIBLE
                        }
                        adapter.setList(data)

                    }
                    else -> viewModel.update(category)
                }
            }
        })

        viewModel.error.observe(this, Observer { error ->
            if (error != null) {
                if (error) {
                    if (refresh.visibility == View.GONE) {
                        // list hide - show error message
                        if (errorText.visibility == View.GONE) errorText.visibility = View.VISIBLE
                    } else {
                        // list visible - show toast
                    }
                }
            }
        })
    }

    companion object {
        private const val ARGS = "books.args"
        fun instance(value: Any?): BooksFragment {
            val fragment = BooksFragment()
            value?.let { fragment.withArgument(ARGS, it as String) }
            return fragment
        }
    }
}