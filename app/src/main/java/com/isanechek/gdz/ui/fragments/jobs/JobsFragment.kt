package com.isanechek.gdz.ui.fragments.jobs

import android.annotation.SuppressLint
import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.isanechek.gdz.Screens
import com.isanechek.gdz.data.RequestJob
import com.isanechek.gdz.data.network.Parser
import com.isanechek.gdz.ui.fragments.base.BaseFragment
import com.isanechek.gdz.ui.fragments.base.ViewModelFactory
import com.isanechek.gdz.ui.navigation.BackButtonListener
import com.isanechek.gdz.utils._id
import com.isanechek.gdz.utils._layout
import com.isanechek.gdz.utils._text
import com.isanechek.gdz.utils.extensions.*
import kotlinx.android.synthetic.main.jobs_fragment_layout.*
import org.jetbrains.anko.*
import org.jetbrains.anko.support.v4.toast

/**
 * Created by isanechek on 12/3/17.
 */
class JobsFragment : BaseFragment(), AnkoComponent<Context>, AnkoLogger, BackButtonListener {
    private val viewModel by nonSafeLazy {
        ViewModelFactory.create(JobsViewModel::class.java)
    }
    private var _id_ = emptyString

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        _id_ = extraWithKey(ARGS)
    }

    override fun fragmentView(): View = context?.let {
        ctx -> createView(AnkoContext.create(ctx))
    } ?: throw RuntimeException("Jobs Fragment Context Null!!")

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        frameLayout {
            include<ConstraintLayout>(_layout.jobs_fragment_layout)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var bookTag = emptyString
        viewModel.author(_id_).observe(this, Observer { author ->
            if (author != null) {
                info("Author id ${author.id}")
                info("Author category ${author.category}")
                info("Author img ${author.img}")

                titleTv.text = context?.getHtmlText(author.title)
                cover.loadImg("${Parser.HOME_PAGE_URL}/${author.img}")
                bookTag = author.childTag
                jobsSizeTv.text = getString(_text.j_size_text) + " ${author.jSize}"
                questionSizeTv.text = getString(_text.q_size_text) + " ${author.qSize}"

            }
        })

        jobChecker.isChecked = true
        showBtn.setOnClickListener { _: View? ->
            val n = editNumber.text.toString()
            if (n.isNotEmpty()) {
                val request = if (jobChecker.isChecked) "j" else "q"
                router.navigateTo(Screens.JOB_SCREEN, RequestJob(n, bookTag, request))
            } else toast("Пустой запрос")
        }

        editNumber.hint = getString(_text.edit_number_hint_j)
        editNumber.setOnEditorActionListener { _, p1, p2 ->
            var state = false
            if (p2 != null && (p2.keyCode == KeyEvent.KEYCODE_ENTER) || (p1 == EditorInfo.IME_ACTION_GO)) {
                val url = editNumber.text.toString()
                if (url.isNotEmpty()) {
                    val request = if (jobChecker.isChecked) "j" else "q"
                    viewModel.findJob(url, bookTag, request)
                    state = true
                }
            }
            state
        }


        viewModel.update(_id_)
    }

    override fun onBackPressed(): Boolean {
        router.backTo(Screens.BOOKS_SCREEN)
        return true
    }

    companion object {
        private const val ARGS = "jobs.args"
        fun instance(id: Any?) : JobsFragment {
            val fragment = JobsFragment()
            id?.let { fragment.withArgument(ARGS, it as String) }
            return fragment
        }
    }
}