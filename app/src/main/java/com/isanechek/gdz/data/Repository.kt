package com.isanechek.gdz.data

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.isanechek.gdz.data.database.GdzDao
import com.isanechek.gdz.data.database.entity.HomeValue
import com.isanechek.gdz.data.network.Parser
import com.isanechek.gdz.utils.extensions.async
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.info

/**
 * Created by isanechek on 11/30/17.
 */
class Repository(private val context: Context) : AnkoLogger {
    private val saveCategory: SharedPreferences by lazy {
        context.getSharedPreferences("gdz", MODE_PRIVATE)
    }
    private val _isUpdating = MutableLiveData<Boolean>()
    val isUpdating: LiveData<Boolean> = _isUpdating
    private val _homeData = MutableLiveData<List<String>>()
    val homeDate: LiveData<List<String>> = _homeData

    fun updateHome(dao: GdzDao) = async {
        info("Update Home")
        _isUpdating.value = true
        val response = await { Parser.parseHomePage() }
        debug("Home response ${response.size}")
        val saveList = StringBuilder()
        val cache = mutableListOf<String>()


        _homeData.value = cache
        saveCategory
                .edit()
                .putString(NAMES_KEY_PREF, saveList.toString())
                .apply()

        _isUpdating.value = false
    }

    companion object {
        const val NAMES_KEY_PREF = "names.key"
    }
}