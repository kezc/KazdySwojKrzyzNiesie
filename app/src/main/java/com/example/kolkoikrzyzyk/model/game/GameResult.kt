package com.example.kolkoikrzyzyk.model.game

sealed class GameResult {
    class Over(var winner: PlayerType, var winningFields: List<Field>) : GameResult()
    object Draw : GameResult()
    object Pending : GameResult()
}