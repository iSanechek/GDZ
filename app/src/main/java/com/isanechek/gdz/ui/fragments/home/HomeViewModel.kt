package com.isanechek.gdz.ui.fragments.home

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.isanechek.gdz.data.database.GdzDataBase
import com.isanechek.gdz.data.database.entity.HomeValue
import com.isanechek.gdz.data.network.Parser
import com.isanechek.gdz.utils.Utils
import com.isanechek.gdz.utils.extensions.async
import com.isanechek.gdz.utils.extensions.preference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*

/**
 * Created by isanechek on 11/30/17.
 */
class HomeViewModel(app: Application) : AndroidViewModel(app), AnkoLogger {
    private val dao = GdzDataBase.get(app.applicationContext).dao()
    private val _isUpdating = MutableLiveData<Boolean>()
    val isUpdating: LiveData<Boolean> = _isUpdating
    private val _data = MutableLiveData<List<String>>()
    val data: LiveData<List<String>> = _data

    fun load() {
        val saveData = preference().getStringSet("save.data", emptySet())
        if (saveData.isEmpty()) {
            info("Save Data Empty")
            loadData()
        } else if (_data.value!!.isEmpty()) {
            _data.value = saveData.toList()
        }
    }

    fun update() {
        info("Home Data Update")
        loadData()
    }

    private fun loadData() = async {
        _isUpdating.value = true
        val response = await { Parser.parseHomePage() }
        val cache = mutableListOf<String>()
        response.forEach { item ->
            val (title, value) = item
            val result = value.map { x -> HomeValue(
                    Utils.generationId(x.url.length + title.length),
                    x.number.toInt(),
                    title,
                    x.url,
                    x.disable)
            }.toList()
            await { dao.insert(result) }
            cache.add(title)
        }
        preference()
                .edit()
                .putStringSet("save.data", cache.toSet())
                .apply()

        _data.value = cache
        _isUpdating.value = false
    }

    init {
        _isUpdating.value = false
        _data.value = emptyList()
    }
}