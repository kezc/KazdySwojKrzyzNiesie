package com.example.kolkoikrzyzyk.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    indices = [Index(
        value = ["name"],
        unique = true
    )],
    tableName = "tournament"
)
data class Tournament(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "is_3d") val is3D: Boolean,
    val size: Int,
    var isOver: Boolean = false
)