package com.example.kolkoikrzyzyk.database

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.kolkoikrzyzyk.model.User

@Entity(
    indices = [Index(
        value = ["name"],
        unique = true
    )],
    tableName = "user"
)
data class DbUser(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    val name: String,
    val password: String,
    val isLogged: Boolean = true
) {
    fun toUser() = User(uid, name)
}