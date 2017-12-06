package com.isanechek.gdz.ui.fragments.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.isanechek.gdz.GdzApplication
import com.isanechek.gdz.ui.fragments.books.BooksViewModel
import com.isanechek.gdz.ui.fragments.home.HomeViewModel
import com.isanechek.gdz.ui.fragments.jobs.JobsViewModel
import com.isanechek.gdz.ui.fragments.jobviewer.JobViewerViewModel
import com.isanechek.gdz.ui.navigation.MainViewModel

/**
 * Created by isanechek on 11/30/17.
 */
@Suppress("UNCHECKED_CAST")
object ViewModelFactory : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(GdzApplication.instance) as T
        modelClass.isAssignableFrom(BooksViewModel::class.java) -> BooksViewModel(GdzApplication.instance) as T
        modelClass.isAssignableFrom(MainViewModel::class.java) -> MainViewModel() as T
        modelClass.isAssignableFrom(JobsViewModel::class.java) -> JobsViewModel(GdzApplication.instance) as T
        modelClass.isAssignableFrom(JobViewerViewModel::class.java) -> JobViewerViewModel(GdzApplication.instance) as T
        else -> super.create(modelClass)
    }
}