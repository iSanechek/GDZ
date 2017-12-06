package com.isanechek.gdz.ui.fragments.home

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.isanechek.gdz.ui.fragments.base.ItemClickListener

/**
 * Created by isanechek on 11/30/17.
 */
class HomeAdapter : RecyclerView.Adapter<HomeViewHolder>() {

    private var _data = ArrayList<String>()

    private var listener: ItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeViewHolder
            = parent?.let { HomeViewHolder.create(it) }?: throw IllegalArgumentException("Home Adapter Parent Null")

    override fun getItemCount(): Int = _data.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        with(holder) {
            bindTo(_data[position])
            itemView?.setOnClickListener { view: View ->
                listener?.itemClick(view, _data[position])
            }
        }
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    fun setList(items: List<String>) {
        _data.addAll(items)
        notifyDataSetChanged()
    }

}