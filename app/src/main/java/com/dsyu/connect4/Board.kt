package com.dsyu.connect4

object Board {

    private var board = Array(6) { Column() }

    fun rotateLeft() {
        val newBoard = Array(6) { Column() }

        for (row in 0..6) {
            for (col in 0..6) {
                newBoard[row].addDisc(board[col].getDisk(row))
            }
        }
        board = newBoard
    }

    fun rotateRight() {
        val newBoard = Array(6) { Column() }

        for (row in 5 downTo 0) {
            for (col in 5 downTo 0) {
                newBoard[row].addDisc(board[col].getDisk(row))
            }
        }
        board = newBoard
    }

    fun winCheck(color : Int): Boolean {
        // check all columns
        for (col in 5 downTo 0) {
            var counter = 0
            for (row in 5 downTo 0) {
                val slot = board[col].getDisk(row)

                if (slot == EMPTY) {
                    break
                } else if (slot == color) {
                    counter++
                } else {
                    counter = 0
                }

                if (counter == 4) {
                    return true
                }
            }
        }

        // check all rows
        for (row in 5 downTo 0) {
            var counter = 0
            for (col in 5 downTo 0) {
                val slot = board[col].getDisk(row)

                if (slot == color) {
                    counter++
                } else {
                    counter = 0
                }

                if (counter == 4) {
                    return true
                }
            }
        }

        // check diagonals from top-left to bottom-right
        for (i in 2 downTo 0) {
            if (checkRowFromTopLeft(i, 0, color) || checkRowFromTopLeft(0, i, color)) {
                    return true
            }
        }

        // check diagonals from top-right to bottom-left
        for (i in 3..5) {
            if (checkRowFromTopRight(0, i, color)) {
                return true
            }
        }

        if (checkRowFromTopRight(1,5,color) || checkRowFromTopRight(2,5, color)) {
            return true
        }
        return false
    }

    private fun checkRowFromTopLeft(row: Int, col: Int, color: Int): Boolean {
        var counter = 0
        var currentRow = row
        var currentCol = col

        for (i in 0 until FULL - row - col) {
            if (board[currentCol].getDisk(currentRow) == color) {
                counter++
            } else {
                counter = 0
            }

            if (counter == 4) {
                return true
            } else {
                currentRow++
                currentCol++
            }
        }
        return false
    }

    private fun checkRowFromTopRight(row: Int, col: Int, color: Int): Boolean {
        var counter = 0
        var currentRow = row
        var currentCol = col

        for (i in 0 until 1 - row + col) {
            if (board[currentCol].getDisk(currentRow) == color) {
                counter++
            } else {
                counter = 0
            }

            if (counter == 4) {
                return true
            } else {
                currentRow++
                currentCol--
            }
        }
        return false
    }

    fun get(i : Int) = board[i]

    fun resetBoard() {
        board = Array(6) { Column() }
    }

}