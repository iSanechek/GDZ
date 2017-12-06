package com.isanechek.gdz.ui.fragments.jobs

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.isanechek.gdz.data.database.GdzDataBase
import com.isanechek.gdz.data.database.entity.Author
import com.isanechek.gdz.data.database.entity.Jobs
import com.isanechek.gdz.data.network.Parser
import com.isanechek.gdz.utils.Utils
import com.isanechek.gdz.utils.extensions.async
import com.isanechek.gdz.utils.extensions.emptyString
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by isanechek on 12/3/17.
 */
class JobsViewModel(app: Application) : AndroidViewModel(app), AnkoLogger {
    private val dao = GdzDataBase.get(app.applicationContext).dao()
    fun author(id: String): LiveData<Author> = dao.loadAuthorLD(id)
    private val _jobData = MutableLiveData<Jobs>()
    val job: LiveData<Jobs> = _jobData

    fun findJob(q: String, bookTag: String, jOrQ: String = "j") = async {
        await {
            val item = dao.findJobsItem(q.trim(), bookTag, jOrQ)
            val result = Parser.parseJob(item.url)
            item.jobUrl = result.url
            dao.updateJobs(item)
            _jobData.postValue(item)
        }
    }

    fun update(id: String, update: Boolean = false) = async {
        await {
            val book = dao.loadAuthor(id)
            when {
                update -> updateData(book)
                book.jSize == 0 && book.qSize == 0 -> updateData(book)
            }
        }

    }

    private fun updateData(book: Author) {
        val result = Parser.parseJobs(book.url)
        if (result.isNotEmpty()) {
            val size = result.size
            val sizeJobs = result.filter { i -> i.job }.toList()
            val tag = Utils.generationId(book.title.length)
            book.jSize = sizeJobs.size
            book.qSize = size - sizeJobs.size
            book.childTag = tag
            dao.updateAuthor(book)

            val values = result.map { item ->
                val jorq = if (item.job) "j" else "q"
                Jobs(Utils.generationId(book.url.length),
                        item.title,
                        item.url,
                        tag,
                        jorq,
                        emptyString,
                        emptyString)
            }

            dao.insertJobs(values)
        }
    }
}