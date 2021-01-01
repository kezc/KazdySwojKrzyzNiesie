package com.example.kolkoikrzyzyk.model.game

class Game(val size: Int, val is3D: Boolean) {
    var currentPlayer = PlayerType.Nought
        private set
    var moveCount = 0
        private set
    val height = if (is3D) size else 1
    val gameBoard = Array(height) {
        Array(size) {
            Array(size) {
                FieldType.Empty
            }
        }
    }

    // returns true if the move was correct
    // false otherwise
    fun makeMove(x: Int, y: Int, z: Int): Boolean {
        if (gameBoard[z][y][x] != FieldType.Empty) return false
        gameBoard[z][y][x] = getCurrentPlayerField()
        changeCurrentPlayer()
        return true
    }

    private fun changeCurrentPlayer() {
        currentPlayer = when (currentPlayer) {
            PlayerType.Cross -> PlayerType.Nought
            PlayerType.Nought -> PlayerType.Cross
        }
    }

    private fun getCurrentPlayerField() = when (currentPlayer) {
        PlayerType.Nought -> FieldType.Nought
        PlayerType.Cross -> FieldType.Cross
    }

    // returns the winner if the game has ended
    // otherwise returns null
    fun checkForWin(): GameResult {
        if (moveCount == size * size * height) return GameResult.Draw
        return checkRowsForWin()
            ?: checkColumnsForWin()
            ?: check2DDiagonalsForWin()
            ?: GameResult.Pending
    }

    // returns null if can't decide about the result
    fun checkColumnsForWin(): GameResult? {
        for (z in 0 until height) {
            for (x in 0 until size) {
                var sum = 0
                for (y in 0 until size) {
                    sum += gameBoard[z][y][x].value
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
        for (board in gameBoard) {
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
                sum += gameBoard[z][i][i].value
            }
            if (sum == size) {
                return GameResult.Over(PlayerType.Cross)
            } else if (sum == -size) {
                return GameResult.Over(PlayerType.Nought)
            }

            sum = 0
            for (i in 0 until size) {
                sum += gameBoard[z][i][size - i - 1].value
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
            sum += gameBoard[i][i][i].value
        }
        if (sum == size) {
            return GameResult.Over(PlayerType.Cross)
        } else if (sum == -size) {
            return GameResult.Over(PlayerType.Nought)
        }

        sum = 0
        for (i in 0 until size) {
            sum += gameBoard[i][size - i - 1][size - i - 1].value
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
        TODO("Not yet implemented")
    }

    fun checkWallDiagonalsForWin(): GameResult? {
        if (!is3D) return null
        TODO("Not yet implemented")
    }

}

