package com.example.kolkoikrzyzyk.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "Tournament_result"
)
data class TournamentResult(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "tournament_id") val tournamentId: Long,
    @ColumnInfo(name = "uid_one") val uidOne: Long,
    @ColumnInfo(name = "uid_two") val uidTwo: Long,
    val result: Int
)