package com.example.kolkoikrzyzyk.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tournament_user"
)
data class TournamentUser(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val uid: Long,
    @ColumnInfo(name = "tournament_id") val tournamentId: Long
)