package com.example.kolkoikrzyzyk.repositories

import com.example.kolkoikrzyzyk.database.Tournament
import com.example.kolkoikrzyzyk.database.TournamentDao
import com.example.kolkoikrzyzyk.database.TournamentResult
import com.example.kolkoikrzyzyk.database.TournamentUser
import com.example.kolkoikrzyzyk.model.User
import com.example.kolkoikrzyzyk.model.game.GameResult
import com.example.kolkoikrzyzyk.model.game.PlayerType

class TournamentRepository(private val tournamentDao: TournamentDao) {
    suspend fun createNewTournament(name: String, players: List<User>, is3D: Boolean, size: Int): Tournament? {
        val id = tournamentDao.insert(Tournament(name = name, is3D = is3D, size = size))
        if (id == -1L) {
            return null
        }
        players.toSet().forEach {
            tournamentDao.insert(TournamentUser(uid = it.uid, tournamentId = id))
        }
        return tournamentDao.getTournamentById(id)
    }

    suspend fun addTournamentResult(
        tournament: Tournament,
        crossPlayer: User,
        noughtPlayer: User,
        gameResult: GameResult
    ) {
        val result = when (gameResult) {
            is GameResult.Over -> if (gameResult.winner == PlayerType.Cross) 1 else 2
            GameResult.Draw -> 0
            GameResult.Pending -> 0
        }
        val tournamentResult =
            TournamentResult(
                tournamentId = tournament.id,
                uidOne = crossPlayer.uid,
                uidTwo = noughtPlayer.uid,
                result = result
            )
        tournamentDao.insert(tournamentResult)
    }

    suspend fun getTournamentByName(name: String) = tournamentDao.getTournamentByName(name)

    fun getAllTournaments() = tournamentDao.getAllTournaments()

    fun getTournamentMatchesCount(tournament: Tournament) = tournamentDao.getMatchesCount(tournament.id)

    fun getCurrentTournamentMatchesCount(tournament: Tournament) = tournamentDao.getCurrentTournamentMatchesCountById(tournament.id)

    fun getTournamentMatches(tournament: Tournament) = tournamentDao.getTournamentMatchesById(tournament.id)

    suspend fun endTournament(tournament: Tournament) = tournamentDao.update(tournament.apply { isOver = true })

    suspend fun getTournamentPlayers(tournament: Tournament) = tournamentDao.findUsersByTournament(tournament.id)
}