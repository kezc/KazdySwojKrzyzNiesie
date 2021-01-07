package com.example.kolkoikrzyzyk.database

import androidx.room.ColumnInfo
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
    val isLogged: Boolean = true,
    @ColumnInfo(name = "won_games") var wonGames: Int = 0,
    @ColumnInfo(name = "drawn_games") var drawnGames: Int = 0,
    @ColumnInfo(name = "lost_games") var lostGames: Int = 0
) {
    fun toUser() = User(uid, password, name, wonGames, drawnGames, lostGames)
}