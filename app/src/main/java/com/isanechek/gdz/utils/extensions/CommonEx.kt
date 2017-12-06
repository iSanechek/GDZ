package com.isanechek.gdz.utils.extensions

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build.VERSION_CODES.N
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.v4.content.ContextCompat
import android.text.Html
import android.text.Spanned
import android.util.TypedValue
import android.view.ViewManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.isanechek.gdz.GdzApplication
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.custom.ankoView

/**
 * Created by isanechek on 11/29/17.
 */
infix fun Context.takeColor(colorId: Int) = ContextCompat.getColor(this, colorId)

val emptyString = ""

fun <T> nonSafeLazy(initializer: () -> T): Lazy<T> {
    return lazy(LazyThreadSafetyMode.NONE) {
        initializer()
    }
}

val Any?.isNull: Boolean
    get() = this == null

val Any?.isNotNull: Boolean
    get() = this != null

val customLog = AnkoLogger("TEST")

@ColorInt
fun Resources.Theme.getColor(@AttrRes attribute: Int): Int {
    val typedValue = TypedValue()
    if (resolveAttribute(attribute, typedValue, true)) {
        return typedValue.data
    }

    return 0
}

fun preference(): SharedPreferences = GdzApplication.instance.getPreferences()

fun ImageView.loadImg(url: String) = Glide.with(this.context).asBitmap().load(url).into(this)

fun Context.getHtmlText(text: String): Spanned {
    return if (android.os.Build.VERSION.SDK_INT >= N) {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(text)
    }
}

inline fun ViewManager.photoView(theme: Int = 0, init: PhotoView.() -> Unit) : PhotoView {
    return ankoView( { PhotoView(it) }, theme = theme, init = init)
}