package com.example.kolkoikrzyzyk.database

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface TournamentDao {
    @Query("SELECT * FROM TOURNAMENT_USER WHERE tournament_id = :id")
    suspend fun findUsersByTournament(id: Long): List<TournamentUser>?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(tournament: Tournament): Long

    @Insert
    suspend fun insert(tournamentResult: TournamentResult): Long

    @Insert
    suspend fun insert(tournamentUser: TournamentUser): Long

    @Update
    suspend fun update(tournament: Tournament)

    @Query("SELECT * FROM tournament")
    fun getAllTournaments(): LiveData<List<Tournament>>

    @Query("SELECT * FROM tournament WHERE name = :name LIMIT 1")
    suspend fun getTournamentByName(name: String): Tournament?

    @Query("SELECT * FROM tournament WHERE id = :id LIMIT 1")
    suspend fun getTournamentById(id: Long): Tournament?

    @Query("SELECT COUNT(*) FROM TOURNAMENT_RESULT WHERE tournament_id = :id")
    fun getMatchesCount(id: Long): LiveData<Int>

    @Query("SELECT COUNT(*) FROM TOURNAMENT_RESULT WHERE tournament_id = :id")
    fun getCurrentTournamentMatchesCountById(id: Long): Int

    @Query("SELECT * FROM TOURNAMENT_RESULT WHERE tournament_id = :id")
    fun getTournamentMatchesById(id: Long): LiveData<List<TournamentResult>>
}