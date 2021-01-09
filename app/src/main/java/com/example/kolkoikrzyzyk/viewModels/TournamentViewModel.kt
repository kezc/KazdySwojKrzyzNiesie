package com.example.kolkoikrzyzyk.viewModels

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.kolkoikrzyzyk.Event
import com.example.kolkoikrzyzyk.database.AppDatabase
import com.example.kolkoikrzyzyk.database.Tournament
import com.example.kolkoikrzyzyk.model.User
import com.example.kolkoikrzyzyk.model.game.GameResult
import com.example.kolkoikrzyzyk.repositories.TournamentRepository
import com.example.kolkoikrzyzyk.repositories.UserRepository
import kotlinx.coroutines.*

class TournamentViewModel(application: Application) : AndroidViewModel(application) {
    var is3d = false
    var gameSize = 3
    private val userRepository = UserRepository(AppDatabase.getInstance(application).userDao())
    private val tournamentRepository =
        TournamentRepository(AppDatabase.getInstance(application).tournamentDao())
    var players = mutableListOf<User>()
    var tournamentName: String = ""
    var tournament: Tournament? = null
    var tournaments = tournamentRepository.getAllTournaments()
    private val _nameOccupied = MutableLiveData<Event<Boolean>>()
    val nameOccupied: LiveData<Event<Boolean>>
        get() = _nameOccupied
    private val _tournamentCreated = MutableLiveData<Event<Unit>>()
    val tournamentCreated: LiveData<Event<Unit>>
        get() = _tournamentCreated

    fun getTournamentMatches() = tournament?.let { tournamentRepository.getTournamentMatches(it) }

    fun nextMatch(): LiveData<Pair<User, User>?>? {
        return tournament?.let {
            Transformations.map(tournamentRepository.getTournamentMatchesCount(it)) { count ->
                var sum = 0
                for (i in players.indices) {
                    for (j in i + 1 until players.size) {
                        if (sum == count) {
                            return@map players[i] to players[j]
                        }
                        sum++
                    }
                }
                null
            }
        }
    }

    fun isTournamentOver(): LiveData<Boolean>? {
        return tournament?.let {
            Transformations.map(tournamentRepository.getTournamentMatchesCount(it)) { count ->
                return@map count == players.size * (players.size - 1) / 2
            }
        }
    }

    fun matchesCount(): LiveData<Int>? {
        return tournament?.let {
            tournamentRepository.getTournamentMatchesCount(it)
        }
    }

    fun addPlayer(user: User) {
        players.add(user)
        players.sortBy {
            it.uid
        }
    }

    fun removePlayer(user: User) {
        players.remove(user)
        players.sortBy {
            it.uid
        }
    }

    fun createTournament() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            tournament =
                tournamentRepository.createNewTournament(tournamentName, players, is3d, gameSize)
            withContext(Dispatchers.Main) {
                if (tournament == null) {
                    _nameOccupied.value = Event(true)
                } else {
                    _tournamentCreated.value = Event(Unit)
                }
            }
            Log.d("TournamentViewModel", tournament.toString())
        }
    }

    fun addResult(crossPlayer: User, noughtPlayer: User, result: GameResult) =
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                tournament?.let {
                    tournamentRepository.addTournamentResult(
                        it,
                        crossPlayer,
                        noughtPlayer,
                        result
                    )
                    val totalMatches = players.size * (players.size - 1) / 2
                    if (tournamentRepository.getCurrentTournamentMatchesCount(it) == totalMatches) {
                        tournamentRepository.endTournament(it)
                        it.isOver = true
                    }
                }
            }
        }

    fun loadTournament(name: String) = viewModelScope.launch {
        tournamentName = name
        withContext(Dispatchers.IO) {
            tournament = tournamentRepository.getTournamentByName(name)
            tournament?.let {
                is3d = it.is3D
                gameSize = it.size
                players = tournamentRepository.getTournamentPlayers(it)?.map { tournamentUser ->
                    async {
                        userRepository.getUserById(tournamentUser.uid)?.toUser()
                    }
                }?.awaitAll()?.filterNotNull()?.toMutableList() ?: mutableListOf()
            }
        }
        _tournamentCreated.value = Event(Unit)
    }
}