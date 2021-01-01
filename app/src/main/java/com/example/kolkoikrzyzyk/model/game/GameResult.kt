package com.example.kolkoikrzyzyk.model.game

sealed class GameResult {
    class Over(var winner: PlayerType) : GameResult()
    object Draw : GameResult()
    object Pending : GameResult()
}