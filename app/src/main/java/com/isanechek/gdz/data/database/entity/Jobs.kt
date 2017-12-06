package com.isanechek.gdz.data.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by isanechek on 12/3/17.
 */
@Entity(tableName = Jobs.TABLE_NAME)
class Jobs(@PrimaryKey val id: String,
           val number: String,
           val url: String,
           val bookTag: String,
           val jorq: String,
           var jobUrl: String,
           var localPath: String) {
    companion object {
        const val TABLE_NAME = "jobs_tabl"
    }
}