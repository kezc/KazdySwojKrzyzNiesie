package com.example.kolkoikrzyzyk.database

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE name = :name LIMIT 1")
    suspend fun findByName(name: String): DbUser?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: DbUser): Long

    @Update
//    suspend fun update(user: DbUser)
    fun update(user: DbUser)

    @Query("SELECT * FROM user")
    fun getAllUsers(): LiveData<List<DbUser>>

    @Query("SELECT * FROM user WHERE isLogged = 1")
    fun getLoggedUsers(): LiveData<List<DbUser>>
}