package com.isanechek.gdz.ui.fragments.jobviewer

import android.arch.lifecycle.Observer
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import com.github.chrisbanes.photoview.PhotoView
import com.isanechek.gdz.Screens
import com.isanechek.gdz.data.RequestJob
import com.isanechek.gdz.data.network.Parser
import com.isanechek.gdz.ui.fragments.base.BaseFragment
import com.isanechek.gdz.ui.fragments.base.ViewModelFactory
import com.isanechek.gdz.ui.navigation.BackButtonListener
import com.isanechek.gdz.utils._drawable
import com.isanechek.gdz.utils.extensions.*
import org.jetbrains.anko.*

/**
 * Created by isanechek on 12/4/17.
 */
class JobViewerFragment : BaseFragment(), AnkoComponent<Context>, BackButtonListener, AnkoLogger {
    private var requestJob: RequestJob? = null
    private lateinit var jobView: PhotoView
    private lateinit var progress: ProgressBar
    private val viewModel by nonSafeLazy {
        ViewModelFactory.create(JobViewerViewModel::class.java)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        requestJob = extraWithKey2(ARGS)
    }

    override fun fragmentView(): View = context?.let {
        ctx -> createView(AnkoContext.create(ctx))
    } ?: throw RuntimeException("Job Viewer Context Is Null!!!")

    override fun createView(ui: AnkoContext<Context>): View = with(ui) {
        frameLayout {
            lparams(width = matchParent, height = matchParent)
            jobView = photoView {}.lparams(width = matchParent, height = matchParent)

            progress = progressBar().lparams {
                gravity = Gravity.CENTER
            }
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        requestJob?.let { job ->
            viewModel.findJob(job.q, job.bookTag, job.jorq)
        }

        viewModel.job.observe(this, Observer { result ->
            if (result != null) {
                jobView.loadImg("${Parser.HOME_PAGE_URL}/${result.jobUrl}")
            }
        })
        viewModel.isUpdate.observe(this, Observer { update ->
            if (update != null) {
                info("Update $update")
                if (update) {
                    if (progress.visibility == View.GONE) progress.visibility = View.VISIBLE
                } else {
                    if (progress.visibility == View.VISIBLE) progress.visibility = View.GONE
                }
            }
        })
    }

    override fun onBackPressed(): Boolean {
        router.backTo(Screens.JOBS_SCREEN)
        return true
    }

    companion object {
        private const val ARGS = "job.viewer.args"

        fun instance(data: Any?) : JobViewerFragment {
            val fragment = JobViewerFragment()
            data?.let { fragment.withArgument(ARGS, it as RequestJob) }
            return fragment
        }
    }
}