package com.dsyu.connect4

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayout
import android.view.View
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var isYellow = true
    private var gameOver = false
    private var discsUsed = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layout: View = findViewById(R.id.constraintLayout)
        val winMessage: ImageView = findViewById(R.id.gameOverMessage)
        val grid: GridLayout = findViewById(R.id.board)
        val restartButton: ImageView = findViewById(R.id.restartButton)

        winMessage.visibility = View.GONE
        restartButton.visibility = View.GONE

        for (i in 0 until grid.childCount) {
            val slot = grid.getChildAt(i)
            slot.setOnClickListener {
                if (!Board.get(i % 6).isFull() && !gameOver) {
                    Board.get(i % 6).addDisc(getDiscColor(isYellow))
                    discsUsed++
                    updateBoard()

                    when {
                        Board.winCheck(getDiscColor(isYellow)) -> endGame()
                        discsUsed == 36 -> tieGame()
                        else -> {
                            when (isYellow) {
                                true -> layout.setBackgroundColor(ContextCompat.getColor(this, R.color.bgRed))
                                false -> layout.setBackgroundColor(ContextCompat.getColor(this, R.color.bgYellow))
                            }
                            isYellow = !isYellow
                        }
                    }
                }
            }
            updateBoard()
        }

        restartButton.setOnClickListener {
            gameOver = false
            discsUsed = 0
            winMessage.visibility = View.GONE
            restartButton.visibility = View.GONE
            Board.resetBoard()
            updateBoard()
        }

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

    private fun endGame() {
        gameOver = true
        restartButton.visibility = View.VISIBLE
        when (isYellow) {
            true -> gameOverMessage.setImageDrawable(getDrawable(R.drawable.win_yellow))
            false -> gameOverMessage.setImageDrawable(getDrawable(R.drawable.win_red))
        }
        gameOverMessage.visibility = View.VISIBLE
    }

    private fun tieGame() {
        restartButton.visibility = View.VISIBLE
        gameOverMessage.setImageDrawable(getDrawable(R.drawable.tie))
        gameOverMessage.visibility = View.VISIBLE
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
