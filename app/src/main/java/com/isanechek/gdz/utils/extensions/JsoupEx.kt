package com.isanechek.gdz.utils.extensions

import org.jsoup.nodes.Element

/**
 * Created by isanechek on 11/30/17.
 */
fun Element.selectUrl() : String = this.select("a").attr("href")

fun disable(url: String) : Boolean = url.contains("#", true)