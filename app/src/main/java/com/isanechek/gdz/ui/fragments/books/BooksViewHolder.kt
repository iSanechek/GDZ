package com.isanechek.gdz.ui.fragments.books

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.isanechek.gdz.data.database.entity.Author
import com.isanechek.gdz.data.network.Parser
import com.isanechek.gdz.ui.fragments.base.ItemClickListener
import com.isanechek.gdz.utils._id
import com.isanechek.gdz.utils.extensions.getHtmlText
import com.isanechek.gdz.utils.extensions.loadImg
import org.jetbrains.anko.*

/**
 * Created by isanechek on 12/3/17.
 */
class BooksViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    private val cover = view.find<ImageView>(_id.books_holder_iv)
    private val title = view.find<TextView>(_id.books_holder_tv)
    private val root = view.find<LinearLayout>(_id.books_holder_root)
    private var item: Author? = null

    fun bindTo(item: Author?, listener: ItemClickListener?) {
        this.item = item
        if (item != null) {
            cover.loadImg("${Parser.HOME_PAGE_URL}/${item.img}")
            title.text = title.context.getHtmlText(item.title)
            root.setOnClickListener { view: View ->
                listener?.itemClick(view, item)
            }
        }
    }

    companion object BooksHolderFactory : AnkoComponent<ViewGroup> {
        override fun createView(ui: AnkoContext<ViewGroup>): View = with(ui) {
            linearLayout {
                id = _id.books_holder_root
                lparams(width = matchParent, height = dip(160))
                orientation = LinearLayout.HORIZONTAL

                imageView {
                    id = _id.books_holder_iv
                }.lparams(width = dip(100), height = dip(140)) {
                    topMargin = dip(8)
                    bottomMargin = dip(8)
                    leftMargin = dip(16)
                    rightMargin = dip(16)
                }

                textView {
                    id = _id.books_holder_tv
                }.lparams(width = wrapContent, height = wrapContent) {
                    topMargin = dip(8)
                    rightMargin = dip(16)
                }
            }
        }

        fun create(parent: ViewGroup) : BooksViewHolder = BooksViewHolder(BooksViewHolder.createView(AnkoContext.create(parent.context, parent)))
    }
}