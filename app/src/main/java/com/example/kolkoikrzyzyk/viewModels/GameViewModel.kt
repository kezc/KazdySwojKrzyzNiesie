package com.example.kolkoikrzyzyk.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kolkoikrzyzyk.model.game.FieldType
import com.example.kolkoikrzyzyk.model.game.Game
import com.example.kolkoikrzyzyk.model.game.GameResult
import com.example.kolkoikrzyzyk.notifyObserver

class GameViewModel : ViewModel() {
    private var isGameWithComputer = false
    private var players = listOf("wojtek")
    private var size = 4
    private var is3D = false
    private lateinit var game: Game
    private val _boardState = MutableLiveData<Array<Array<Array<FieldType>>>?>()
    val boardState: LiveData<Array<Array<Array<FieldType>>>?>
        get() = _boardState
    private val _gameResult = MutableLiveData<GameResult?>()
    val gameResult: MutableLiveData<GameResult?>
        get() = _gameResult

    fun startGame() {
        game = Game(size, is3D)
        _boardState.value = game.gameBoard
    }

    fun makeMove(x: Int, y: Int, z: Int) {
        if (game.makeMove(x, y, z)) {
            _boardState.notifyObserver()
        }
        _gameResult.value = game.checkForWin()
    }
}