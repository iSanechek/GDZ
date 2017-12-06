package com.isanechek.gdz.ui.fragments.books

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PagedList
import com.isanechek.gdz.data.database.GdzDataBase
import com.isanechek.gdz.data.database.entity.Author
import com.isanechek.gdz.data.network.Parser
import com.isanechek.gdz.utils.Utils
import com.isanechek.gdz.utils.extensions.async
import com.isanechek.gdz.utils.extensions.emptyString
import com.isanechek.gdz.utils.extensions.preference
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

/**
 * Created by isanechek on 11/30/17.
 */
class BooksViewModel(app: Application) : AndroidViewModel(app), AnkoLogger {
    private val dao = GdzDataBase.get(app.applicationContext).dao()
    private val _isUpdating = MutableLiveData<Boolean>()
    val isUpdating: LiveData<Boolean> = _isUpdating
    private val _errorState = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = _errorState

    fun data(category: String) = dao.loadAuthors(category).create(0,
            PagedList
                    .Config
                    .Builder()
                    .setPageSize(10)
                    .setEnablePlaceholders(false)
                    .build())

    fun update(category: String) = async {
        info("category $category")
        val items = await { dao.loadClasses(category) }
        info("From db ${items.size}")
        if (items.isNotEmpty()) {
            val number = preference().getInt("class.number", 5)
            val item = items.find { item -> item.number == number }
            if (item != null) {
                if (item.disable) {
                    // hide
                    info("Item Disable ${item.number}")
                    _errorState.value = true
                } else {
                    // request
                    val (title, response) = await { Parser.parseAuthor(item.url) }
                    if (response.isNotEmpty()) {
                        val content = response.map { x ->
                            Author(
                                    id = Utils.generationId(x.title.length),
                                    category = category,
                                    title = x.title,
                                    url = x.url,
                                    img =x.coverUrl,
                                    jSize = 0,
                                    qSize = 0,
                                    childTag = emptyString
                            )
                        }
                        await { dao.insertAuthors(content) }
                    } else _errorState.value = true
                }
            } else {
                // class number not found
                info("update class number not found")
                _errorState.value = true
            }
        }
    }

    fun updateItem(item: Author) = async {
        await { dao.updateAuthor(item) }
    }

    init {
        _errorState.value = false
        _isUpdating.value = false
    }
}