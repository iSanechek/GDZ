package com.isanechek.gdz.ui.fragments.home

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import com.isanechek.gdz.utils._id
import com.isanechek.gdz.utils.extensions.getColor
import org.jetbrains.anko.*

/**
 * Created by isanechek on 11/30/17.
 */
class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val tv = view.find<TextView>(_id.home_list_item_tv)

    fun bindTo(title: String) {
        tv.text = title
    }

    companion object HomeHolderFactory : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui){
             relativeLayout {
                textView {
                    id = _id.home_list_item_tv
                    textSize = 32f
                    textColor = Color.BLACK
                    this.gravity = Gravity.CENTER
                }.lparams(width = matchParent, height = wrapContent) {
                    centerInParent()
                    leftMargin = dip(16)
                    rightMargin = dip(16)
                    topMargin = dip(8)
                    bottomMargin = dip(8)
                }
            }
        }

        fun create(parent: ViewGroup) : HomeViewHolder = HomeViewHolder(createView(AnkoContext.create(parent.context, parent)))
    }
}