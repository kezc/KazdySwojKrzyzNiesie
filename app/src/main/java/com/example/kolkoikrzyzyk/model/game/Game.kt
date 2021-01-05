package com.example.kolkoikrzyzyk.model.game

import android.util.Log

class Game(val size: Int, val is3D: Boolean) {
    private val TAG = "Game"
    var currentPlayer = PlayerType.Nought
        private set
    var moveCount = 0
        private set
    val height = if (is3D) size else 1
    val gameBoards = Array(height) { z ->
        Array(size) { y ->
            Array(size) { x ->
                Field(x, y, z)
            }
        }
    }
    var hasEnded = false
        private set

    // returns true if the move was correct
    // false otherwise
    fun makeMove(x: Int, y: Int, z: Int): Boolean {
        if (gameBoards[z][y][x].type != FieldType.Empty || hasEnded) return false
        when (currentPlayer) {
            PlayerType.Nought -> gameBoards[z][y][x].placeNought()
            PlayerType.Cross -> gameBoards[z][y][x].placeCross()
        }
        moveCount++
        changeCurrentPlayer()
        return true
    }

    private fun changeCurrentPlayer() {
        currentPlayer = when (currentPlayer) {
            PlayerType.Cross -> PlayerType.Nought
            PlayerType.Nought -> PlayerType.Cross
        }
    }


    fun getAvailableFields(): List<Field> = gameBoards
        .map { singleBoard ->
            singleBoard.map { row ->
                row.filter { it.type == FieldType.Empty }
            }.flatten()
        }.flatten()


    // returns the winner if the game has ended
    // otherwise returns null
    fun checkForWin(): GameResult {
        if (moveCount == size * size * height) return GameResult.Draw
        val winResult = checkRowsForWin()
            ?: checkColumnsForWin()
            ?: check2DDiagonalsForWin()
            ?: check3DColumnsForWin()
        winResult?.let {
            hasEnded = true
        }
        return winResult ?: GameResult.Pending
    }

    // returns null if can't decide about the result
    fun checkColumnsForWin(): GameResult? {
        for (z in 0 until height) {
            for (x in 0 until size) {
                var sum = 0
                for (y in 0 until size) {
                    sum += gameBoards[z][y][x].value
                }
                if (sum == size) {
                    return GameResult.Over(PlayerType.Cross)
                } else if (sum == -size) {
                    return GameResult.Over(PlayerType.Nought)
                }
            }
        }
        return null
    }

    // returns null if can't decide about the result
    fun checkRowsForWin(): GameResult? {
        for (board in gameBoards) {
            for (row in board) {
                val sum = row.sumBy {
                    it.value
                }
                if (sum == size) {
                    return GameResult.Over(PlayerType.Cross)
                } else if (sum == -size) {
                    return GameResult.Over(PlayerType.Nought)
                }
            }
        }
        return null
    }

    // returns null if can't decide about the result
    fun check2DDiagonalsForWin(): GameResult? {
        for (z in 0 until height) {
            var sum = 0
            for (i in 0 until size) {
                sum += gameBoards[z][i][i].value
            }
            if (sum == size) {
                return GameResult.Over(PlayerType.Cross)
            } else if (sum == -size) {
                return GameResult.Over(PlayerType.Nought)
            }

            sum = 0
            for (i in 0 until size) {
                sum += gameBoards[z][i][size - i - 1].value
            }
            if (sum == size) {
                return GameResult.Over(PlayerType.Cross)
            } else if (sum == -size) {
                return GameResult.Over(PlayerType.Nought)
            }
        }
        return null
    }

    fun check3DDiagonalsForWin(): GameResult? {
        if (!is3D) return null

        var sum = 0
        for (i in 0 until size) {
            sum += gameBoards[i][i][i].value
        }
        if (sum == size) {
            return GameResult.Over(PlayerType.Cross)
        } else if (sum == -size) {
            return GameResult.Over(PlayerType.Nought)
        }

        sum = 0
        for (i in 0 until size) {
            sum += gameBoards[i][size - i - 1][size - i - 1].value
        }
        if (sum == size) {
            return GameResult.Over(PlayerType.Cross)
        } else if (sum == -size) {
            return GameResult.Over(PlayerType.Nought)
        }
        return null
    }

    fun check3DColumnsForWin(): GameResult? {
        if (!is3D) return null
        for (x in 0 until size) {
            for (y in 0 until size) {
                var sum = 0
                for (z in 0 until height) {
                    sum += gameBoards[z][y][x].value
                }
                if (sum == size) {
                    return GameResult.Over(PlayerType.Cross)
                } else if (sum == -size) {
                    return GameResult.Over(PlayerType.Nought)
                }
            }
        }
        return null
    }

    fun checkWallDiagonalsForWin(): GameResult? {
        if (!is3D) return null
        TODO("Not yet implemented")
    }

    internal fun clearField(x: Int, y: Int, z: Int) {
        gameBoards[z][y][x].placeEmpty()
        moveCount--
        currentPlayer = currentPlayer.getOther()
        hasEnded = false
    }
}

