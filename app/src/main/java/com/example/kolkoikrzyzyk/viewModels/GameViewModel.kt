package com.example.kolkoikrzyzyk.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kolkoikrzyzyk.model.User
import com.example.kolkoikrzyzyk.model.game.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class GameViewModel : ViewModel() {
    private val TAG = "GameViewModel"
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


    fun startGame() {
        game = Game(size, is3D)
        computer = ComputerPlayer(game, PlayerType.Cross)
    }

    fun makeMove(x: Int, y: Int, z: Int) {
        if (game.makeMove(x, y, z)) {
            _lastSuccessfulMove.value = game.gameBoards[z][y][x]
        }
        val result = game.checkForWin()
        _gameResult.value = result
        if (result !is GameResult.Pending) {
            return
        }
        computer?.let {
            computerMove(it)
        }
    }

    private fun computerMove(it: ComputerPlayer) =
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _computerThinking.value = true
                val field = withContext(Dispatchers.Default) {
                    it.move()
                }
                _lastSuccessfulMove.value = field
                val result = game.checkForWin()
                _gameResult.value = result
                if (result is GameResult.Pending) {
                    _computerThinking.value = false
                }
            }
        }
}