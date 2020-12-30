package com.example.kolkoikrzyzyk.model

class Game(val size: Int) {
    var currentPlayer = PlayerType.Nought
    val gameBoard = Array(size) {
        Array(size) {
            FieldType.Empty
        }
    }

    // returns true if the move was correct
    fun place(x: Int, y: Int): Boolean {
        if (gameBoard[x][y] == FieldType.Empty) return false
        gameBoard[x][y] = when (currentPlayer) {
            PlayerType.Nought -> FieldType.Nought
            PlayerType.Cross -> FieldType.Cross
        }
        return true
    }

    fun checkForWin(): PlayerType? =
        checkRowsForWin() ?: checkColumnForWin() ?: checkDiagonalsForWin()


    private fun checkRowsForWin(): PlayerType? {
        for (y in 0 until size) {
            val sum = gameBoard.sumBy {
                it[y].value
            }
            if (sum == size) {
                return PlayerType.Cross
            } else if (sum == -size) {
                return PlayerType.Nought
            }
        }
        return null
    }

    private fun checkColumnForWin(): PlayerType? {
        for (column in gameBoard) {
            val sum = column.sumBy {
                it.value
            }
            if (sum == size) {
                return PlayerType.Cross
            } else if (sum == -size) {
                return PlayerType.Nought
            }
        }
        return null
    }

    private fun checkDiagonalsForWin(): PlayerType? {
        var sum = 0
        for (i in 0 until size) {
            sum += gameBoard[i][i].value
        }
        if (sum == size) {
            return PlayerType.Cross
        } else if (sum == -size) {
            return PlayerType.Nought
        }

        sum = 0
        for (i in 0 until size) {
            sum += gameBoard[i][size - i - 1].value
        }
        if (sum == size) {
            return PlayerType.Cross
        } else if (sum == -size) {
            return PlayerType.Nought
        }
        return null
    }
}

