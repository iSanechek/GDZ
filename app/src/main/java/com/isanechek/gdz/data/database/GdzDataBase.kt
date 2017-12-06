package com.isanechek.gdz.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.isanechek.gdz.data.database.entity.Author
import com.isanechek.gdz.data.database.entity.HomeValue
import com.isanechek.gdz.data.database.entity.Jobs

/**
 * Created by isanechek on 11/30/17.
 */
@Database(entities = [
    (HomeValue::class),
    (Author::class),
    (Jobs::class)], version = 2)
abstract class GdzDataBase : RoomDatabase() {
    abstract fun dao(): GdzDao

    companion object {
        private var instance: GdzDataBase? = null
        fun get(context: Context): GdzDataBase {
            if (instance == null) {
                instance = Room.databaseBuilder(context.applicationContext,
                        GdzDataBase::class.java, "Gdz.db")
                        .fallbackToDestructiveMigration()
                        .build()
            }
            return instance?: throw IllegalArgumentException("Data Base Instance Null")
        }
    }
}