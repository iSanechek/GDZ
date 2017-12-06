package com.isanechek.gdz.data.database.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

/**
 * Created by isanechek on 11/30/17.
 */
@Entity(tableName = HomeValue.TABLE_NAME)
class HomeValue(@PrimaryKey val id: String,
                val number: Int,
                val category: String,
                val url: String,
                val disable: Boolean) {
    companion object {
        const val TABLE_NAME = "home_value"
    }
}