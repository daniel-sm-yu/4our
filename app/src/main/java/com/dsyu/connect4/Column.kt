package com.dsyu.connect4

val EMPTY = 0
val FULL = 6
val RED = 1
val BLUE = 2

class Column {

    private var column = Array(FULL) { EMPTY }
    private var numOfDisc = 0

    // Player 1 is red
    fun addDisc(color: Int) {
        if (numOfDisc == FULL || color == EMPTY) {
            return
        }
        column[numOfDisc] = color
        numOfDisc++
    }

    fun getDisk(row: Int): Int {
        return column[row]
    }

}