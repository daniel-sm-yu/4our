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

}