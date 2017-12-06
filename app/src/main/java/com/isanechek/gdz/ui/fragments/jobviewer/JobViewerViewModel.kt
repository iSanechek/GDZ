package com.isanechek.gdz.ui.fragments.jobviewer

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.isanechek.gdz.data.database.GdzDataBase
import com.isanechek.gdz.data.database.entity.Jobs
import com.isanechek.gdz.data.network.Parser
import com.isanechek.gdz.utils.extensions.async

/**
 * Created by isanechek on 12/5/17.
 */
class JobViewerViewModel(app: Application) : AndroidViewModel(app) {
    private val dao = GdzDataBase.get(app.applicationContext).dao()
    private val _jobData = MutableLiveData<Jobs>()
    val job: LiveData<Jobs> = _jobData
    private val _isUpdate = MutableLiveData<Boolean>()
    val isUpdate: LiveData<Boolean> = _isUpdate

    fun findJob(q: String, bookTag: String, jOrQ: String = "j") = async {
        _isUpdate.value = true
        await {
            val item = dao.findJobsItem(q.trim(), bookTag, jOrQ)
            val result = Parser.parseJob(item.url)
            item.jobUrl = result.url
            dao.updateJobs(item)
            _jobData.postValue(item)
        }
        _isUpdate.value = false
    }

    init {
        _isUpdate.value = false
    }
}