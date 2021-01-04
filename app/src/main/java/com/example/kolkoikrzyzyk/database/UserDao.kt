package com.example.kolkoikrzyzyk.database

import androidx.room.*

//TODO dodaj dobre funkcje
@Dao
interface UserDao {
    @Query("SELECT * FROM user WHERE name = :name LIMIT 1")
    suspend fun findByName(name: String): DbUser?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: DbUser): Long

    @Query("UPDATE user SET isLogged = :isLogged WHERE uid = :uid")
    suspend fun updateLoginStatus(uid: Long, isLogged: Boolean)
}