package com.dsyu.connect4

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayout
import android.text.Layout
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout

class MainActivity : AppCompatActivity() {

    private var isYellow: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout = findViewById<View>(R.id.constraintLayout)
        val winMessage: ImageView = findViewById(R.id.winMessage)
        val grid: GridLayout = findViewById(R.id.board)
        for (i in 0 until grid.childCount) {
            val slot = grid.getChildAt(i)
            slot.setOnClickListener {
                if (!Board.get(i % 6).isFull()) {
                    Board.get(i % 6).addDisc(getDiscColor(isYellow))
                    updateBoard()
                    when (isYellow) {
                        true -> layout.setBackgroundColor(ContextCompat.getColor(this, R.color.bgRed))
                        false -> layout.setBackgroundColor(ContextCompat.getColor(this, R.color.bgYellow))
                    }

                    // THIS WILL BE REMOVED AFTER WIN CHECKER
                    when (isYellow) {
                        true -> winMessage.setImageDrawable(getDrawable(R.drawable.win_red))
                        false -> winMessage.setImageDrawable(getDrawable(R.drawable.win_yellow))
                    }

                    isYellow = !isYellow
                }
            }
        }

        updateBoard()
    }

    private fun updateBoard() {
        for (row in 0..6) {
            for (col in 0..6) {
                val slotId = resources.getIdentifier("slot$row$col", "id", packageName)
                val slot: ImageView? = findViewById(slotId)
                slot?.setImageDrawable(getDiscDrawable(Board.get(col).getDisk(row)))
            }
        }
    }

    private fun getDiscColor(isYellow: Boolean): Int = when (isYellow) {
        true -> YELLOW
        false -> RED
    }


    private fun getDiscDrawable(color: Int) = when (color) {
        YELLOW -> getDrawable(R.drawable.yellow)
        RED -> getDrawable(R.drawable.red)
        else -> getDrawable(R.drawable.empty)
    }


}
