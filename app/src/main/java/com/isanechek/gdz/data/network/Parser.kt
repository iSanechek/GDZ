package com.isanechek.gdz.data.network

import android.util.Log
import com.isanechek.gdz.data.network.pojo.AuthorItem
import com.isanechek.gdz.data.network.pojo.HomeItem
import com.isanechek.gdz.data.network.pojo.JobItem
import com.isanechek.gdz.data.network.pojo.JobsItem
import com.isanechek.gdz.utils.extensions.disable
import com.isanechek.gdz.utils.extensions.selectUrl
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

/**
 * Created by isanechek on 11/30/17.
 */
object Parser : AnkoLogger {
    private const val USER_AGENT = "Mozilla/5.0 (X11; Ubuntu; Linux i686; rv:55.0) Gecko/20100101 Firefox/55.0"
    const val HOME_PAGE_URL = "http://gdz.name"

    fun parseHomePage() : List<Pair<String, List<HomeItem>>> {
        val content = getContent(HOME_PAGE_URL)
        val element = content.selectFirst("div.yetMainWrapper div.mainWrapperLeft div.tableLessons")
        val elements= element.selectFirst("ul").children()
        val cache = mutableListOf<Pair<String, List<HomeItem>>>()
        for (i in 1 until elements.size) {
            val items = elements[i].children()
            val title = items[0].text()
            val children = items[1].selectFirst("ul li").children()
            val values = children.map { x -> HomeItem(x.text(), x.selectUrl(), disable(x.selectUrl())) }.toList()
            cache.add(Pair(title, values))
        }
        return cache
    }

    fun parseAuthor(url: String) : Pair<String, List<AuthorItem>> {
        val content = getContent("$HOME_PAGE_URL/$url")

        if (content.tagName() == "empty") return Pair("empty", emptyList())

        val element = content.selectFirst("div.yetMainWrapper div.mainWrapperLeft")
        val pageTitle = element.selectFirst("h1").text()

        val authors = element.selectFirst("div.listBook").select("div.oneBook")
        val cache = mutableListOf<AuthorItem>()
        authors.forEach { book ->
            try {
                val items = book.children()
                val img = items[0]
                val jobsUrl = img.select("a").attr("href")
                val coverUrl = img.select("img").attr("src")

                val text = items[1].getElementsByTag("p")
                val builder = StringBuilder()
                text.forEach { t ->
                    builder.append(t)
                    builder.append(" ")
                }
                val title = builder.toString()
                cache.add(AuthorItem(title, jobsUrl, coverUrl))
            } catch (ignore: Exception) { }
        }

        return Pair(pageTitle, cache)
    }

    fun parseJobs(url: String) : List<JobsItem> {
        val content = getContent("$HOME_PAGE_URL/$url")
        val element = content.selectFirst("div.yetMainWrapper div.mainWrapperLeft div.listJobs");
        val jobs = element.selectFirst("li").children()
        val cache = mutableListOf<JobsItem>()
        jobs.forEach { job ->
            val item = job.selectFirst("a")
            val link = item.attr("href")
            val title = item.text()
            var _job = false
            if (!title.contains("Вопросы")) _job = true

            cache.add(JobsItem(
                    title = title,
                    url = link,
                    job = _job
            ))
        }
        return cache
    }

    fun parseJob(url: String) : JobItem {
        val content = getContent("$HOME_PAGE_URL/$url")
        info("Debug $content")
        val element = content.selectFirst("div.yetMainWrapper div.mainWrapperLeft div.boxSolution").children()
        val title = element[0].text()
        val link = element[1].selectFirst("img").attr("src")
        return JobItem(title, link)
    }

    private fun getContent(url: String) : Element = try {
        Jsoup
                .connect(url)
                .timeout(10000)
                .userAgent(USER_AGENT)
                .get()
                .body()
                .selectFirst("div.content")
    } catch (ex: Exception) {
        Log.e("Parser", "Get Content Error ${ex.message}")
        Element("empty")
    }
}