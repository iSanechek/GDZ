package com.isanechek.gdz.ui.navigation

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

/**
 * Created by isanechek on 12/2/17.
 */
class MainViewModel : ViewModel() {
    private val _data = MutableLiveData<Any>()
    val data: LiveData<Any> = _data

    fun sendData(value: Any) {
        _data.value = value
    }
}