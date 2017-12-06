package com.isanechek.gdz.data.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by isanechek on 12/2/17.
 */
@Entity(tableName = Author.TABLE_NAME)
class Author(@PrimaryKey val id: String,
             val category: String,
             val title: String,
             val url: String,
             val img: String,
             var jSize: Int,
             var qSize: Int,
             var childTag: String) {
    companion object {
        const val TABLE_NAME = "author"
    }
}