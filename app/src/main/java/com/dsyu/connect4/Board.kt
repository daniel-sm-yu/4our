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
                }

                if (counter == 4) {
                    return true
                }
            }
        }

        return false
    }


    fun get(i : Int) = board[i]

    fun resetBoard() {
        board = Array(6) { Column() }
    }

}