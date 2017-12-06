package com.isanechek.gdz.ui.fragments.books

import android.arch.paging.PagedListAdapter
import android.support.v7.recyclerview.extensions.DiffCallback
import android.view.ViewGroup
import com.isanechek.gdz.data.database.entity.Author
import com.isanechek.gdz.ui.fragments.base.ItemClickListener

/**
 * Created by isanechek on 12/3/17.
 */
class BooksAdapter : PagedListAdapter<Author, BooksViewHolder>(diffCallback) {

    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): BooksViewHolder
            = parent?.let { BooksViewHolder.create(it) }?: throw RuntimeException("Parent is null!!")

    override fun onBindViewHolder(holder: BooksViewHolder, position: Int) {
        with(holder) {
            bindTo(getItem(position), listener)
        }
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    companion object {
        private val diffCallback = object : DiffCallback<Author>() {
            override fun areContentsTheSame(oldItem: Author, newItem: Author): Boolean = oldItem == newItem
            override fun areItemsTheSame(oldItem: Author, newItem: Author): Boolean = oldItem.id == newItem.id

        }
    }
}