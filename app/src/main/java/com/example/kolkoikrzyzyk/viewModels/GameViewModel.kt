package com.example.kolkoikrzyzyk.viewModels

import android.app.Application
import androidx.lifecycle.*
import com.example.kolkoikrzyzyk.MainActivity
import com.example.kolkoikrzyzyk.UserRepository
import com.example.kolkoikrzyzyk.database.AppDatabase
import com.example.kolkoikrzyzyk.model.User
import com.example.kolkoikrzyzyk.model.game.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import kotlin.concurrent.thread


class GameViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "GameViewModel"
    private val repository = UserRepository(AppDatabase.getInstance(application).userDao())
    private var isGameWithComputer = false
    lateinit var noughtUser: User
    lateinit var crossUser: User
    var size = 3
    var is3D = false
    private lateinit var game: Game
    private var computer: ComputerPlayer? = null
    private val _lastSuccessfulMove = MutableLiveData<Field?>()
    val lastSuccessfulMove: LiveData<Field?>
        get() = _lastSuccessfulMove
    private val _gameResult = MutableLiveData<GameResult?>()
    val gameResult: MutableLiveData<GameResult?>
        get() = _gameResult
    private var _computerThinking = MutableLiveData(false)
    val computerThinking: LiveData<Boolean>
        get() = _computerThinking
    private var _currentPlayer = MutableLiveData<PlayerType>()
    val currentPlayer: LiveData<PlayerType>
        get() = _currentPlayer


    fun startGame() {
        game = Game(size, is3D)
        _currentPlayer.value = game.currentPlayer
        if (noughtUser.name == "Computer") {
            computer = ComputerPlayer(game, PlayerType.Nought)
        }
        if (crossUser.name == "Computer") {
            computer = ComputerPlayer(game, PlayerType.Cross)
            computerMove()
        }
    }

    fun makeMove(x: Int, y: Int, z: Int) {
        if (game.makeMove(x, y, z)) {
            _lastSuccessfulMove.value = game.gameBoards[z][y][x]
        }
        val result = game.checkForWin()
        _gameResult.value = result
        if (result !is GameResult.Pending) {
            onGameEnd(result)
            return
        } else {
            _currentPlayer.value = game.currentPlayer
        }
        if (crossUser.name == "Computer" || noughtUser.name == "Computer") {
            computerMove()
        }
    }

    //    private fun computerMove() = computer?.let {
//        viewModelScope.launch {
//            withContext(Dispatchers.Main) {
//                _computerThinking.value = true
//                val field = withContext(Dispatchers.Default) {
//                    it.move()
//                }
//                _lastSuccessfulMove.value = field
//                val result = game.checkForWin()
//                _gameResult.value = result
//                if (result is GameResult.Pending) {
//                    _computerThinking.value = false
//                    _currentPlayer.value = game.currentPlayer
//                } else {
//                    onGameEnd(result)
//                }
//            }
//        }
//    }

        private fun computerMove() = computer?.let {
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _computerThinking.value = true
                val field = withContext(Dispatchers.Default) {
                    val executor = Executors.newSingleThreadExecutor()
                    val callable: Callable<Field?> = Callable<Field?> { it.move() }
                    val future = executor.submit(callable)
                    future.get()
                }
                _lastSuccessfulMove.value = field
                val result = game.checkForWin()
                _gameResult.value = result
                if (result is GameResult.Pending) {
                    _computerThinking.value = false
                    _currentPlayer.value = game.currentPlayer
                } else {
                    onGameEnd(result)
                }
            }
        }
    }


    private fun onGameEnd(gameResult: GameResult) =
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                if (gameResult is GameResult.Over) {
                    if (gameResult.winner == PlayerType.Cross) {
                        if (crossUser.name != "Computer") {
                            crossUser.wonGames++
                            repository.updateUser(crossUser)
                        }
                        if (noughtUser.name != "Computer") {
                            noughtUser.lostGames++
                            repository.updateUser(noughtUser)
                        }
                    } else {
                        if (crossUser.name != "Computer") {
                            crossUser.lostGames++
                            repository.updateUser(crossUser)
                        }
                        if (noughtUser.name != "Computer") {
                            noughtUser.wonGames++
                            repository.updateUser(noughtUser)
                        }
                    }
                }
                if (gameResult is GameResult.Draw) {
                    if (crossUser.name != "Computer") {
                        crossUser.drawnGames++
                        repository.updateUser(crossUser)
                    }
                    if (noughtUser.name != "Computer") {
                        noughtUser.drawnGames++
                        repository.updateUser(noughtUser)
                    }
                }
            }
        }
}