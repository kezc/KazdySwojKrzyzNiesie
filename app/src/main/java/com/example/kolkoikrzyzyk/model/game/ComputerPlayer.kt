package com.example.kolkoikrzyzyk.model.game

import kotlin.math.max
import kotlin.math.min

class ComputerPlayer(
    private val game: Game,
    private val type: PlayerType,
) {
    private val enemyType = type.getOther()
    private var maxDepth = 0

    init {
        updateMaxDepth()
    }

    private fun updateMaxDepth() {
        val leftFields = game.size * game.size * game.height - game.moveCount
        maxDepth = when {
            leftFields <= 9 -> 9
            leftFields <= 12 -> 6
            leftFields <= 14 -> 5
            leftFields <= 18 -> 4
            leftFields <= 27 -> 3
            else -> 2
        }
    }

    fun minMax(depth: Int, turn: PlayerType): Int {
        val result = game.checkForWin()
        if (result is GameResult.Draw || depth == maxDepth) return 0
        if (result is GameResult.Over) {
            return when (result.winner) {
                type -> 1
                enemyType -> -1
                else -> 0
            }
        }

        val availableFields = game.getAvailableFields()
        if (availableFields.isEmpty()) return 0

        return if (turn == type) {
            var max = Int.MIN_VALUE
            availableFields.forEach { field ->
                game.makeMove(field.x, field.y, field.z)
                max = max(minMax(depth + 1, enemyType), max)
                game.clearField(field.x, field.y, field.z)
            }
            max
        } else {
            var min = Int.MAX_VALUE
            availableFields.forEach { field ->
                game.makeMove(field.x, field.y, field.z)
                min = min(minMax(depth + 1, type), min)
                game.clearField(field.x, field.y, field.z)
            }
            min
        }
    }

    fun move(): Field? {
        updateMaxDepth()
        var max = Int.MIN_VALUE
        val availableFields = game.getAvailableFields()
        var field = availableFields.getOrNull(0)
        availableFields.forEach {
            game.makeMove(it.x, it.y, it.z)
            val value = minMax(0, enemyType)
            if (value > max) {
                max = value
                field = it
            }
            game.clearField(it.x, it.y, it.z)
        }
        field?.let {
            game.makeMove(it.x, it.y, it.z)
        }
        return field
    }
}