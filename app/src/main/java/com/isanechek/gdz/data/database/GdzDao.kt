package com.isanechek.gdz.data.database

import android.arch.lifecycle.LiveData
import android.arch.paging.LivePagedListProvider
import android.arch.persistence.room.*
import com.isanechek.gdz.data.database.entity.Author
import com.isanechek.gdz.data.database.entity.HomeValue
import com.isanechek.gdz.data.database.entity.Jobs

/**
 * Created by isanechek on 11/30/17.
 */
@Dao
interface GdzDao {

    @Query("SELECT * FROM ${HomeValue.TABLE_NAME} WHERE category = :arg0")
    fun loadClasses(arg0: String): List<HomeValue>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: List<HomeValue>)

    @Query("SELECT * FROM ${Author.TABLE_NAME} WHERE category = :arg0")
    fun loadAuthors(arg0: String) : LivePagedListProvider<Int, Author>

    @Query("SELECT * FROM ${Author.TABLE_NAME} WHERE id = :arg0 LIMIT 1")
    fun loadAuthorLD(arg0: String) : LiveData<Author>

    @Query("SELECT * FROM ${Author.TABLE_NAME} WHERE id = :arg0 LIMIT 1")
    fun loadAuthor(arg0: String) : Author

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAuthors(items: List<Author>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAuthor(item: Author)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertJobs(items: List<Jobs>)

    @Query("SELECT * FROM ${Jobs.TABLE_NAME} WHERE bookTag = :arg1 AND jorq = :arg2 AND number = :arg0 LIMIT 1")
    fun findJobsItem(arg0: String, arg1: String, arg2: String) : Jobs // arg0 - jobs number / arg1 - book tag / arg2 - jobs or query

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateJobs(item: Jobs)
}