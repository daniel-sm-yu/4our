package com.dsyu.connect4

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayout
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var isYellow = true
    private var gameOver = false
    private var discsUsed = 0
    private var holdingScreen = false

    private lateinit var sensorManager: SensorManager
    private lateinit var gyroscope: Sensor
    private lateinit var layout: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.layout = findViewById(R.id.constraintLayout)
        val winMessage: ImageView = findViewById(R.id.gameOverMessage)
        val grid: GridLayout = findViewById(R.id.board)
        val restartButton: ImageView = findViewById(R.id.restartButton)

        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let {
            this.gyroscope = it
        }

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
                            flipBackgroundColor()
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

        layout.setOnTouchListener { _, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    holdingScreen = true
                    true
                }
                MotionEvent.ACTION_UP -> {
                    holdingScreen = false
                    true
                }
                else ->  {false}
            }
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

    private fun endGame(winnerIsYellow: Boolean = isYellow) {
        gameOver = true
        restartButton.visibility = View.VISIBLE
        when (winnerIsYellow) {
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

    private fun flipBackgroundColor() {
        if (isYellow) {
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.bgRed))
        } else {
            layout.setBackgroundColor(ContextCompat.getColor(this, R.color.bgYellow))
        }
    }

    private fun rotateBoard(directionIsLeft: Boolean) {
        holdingScreen = false

        if (directionIsLeft) {
            Board.rotateLeft()
        } else {
            Board.rotateRight()
        }
        updateBoard()
        val yellowWon = Board.winCheck(YELLOW)
        val redWon = Board.winCheck(RED)

        if (yellowWon && redWon) {
            tieGame()
        } else if (yellowWon) {
            endGame(true)
        } else if (redWon) {
            endGame(false)
        }

        if (!(yellowWon && redWon) && ((isYellow && redWon) || (!isYellow && yellowWon))) {
            flipBackgroundColor()
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_GYROSCOPE) return

        if (event.values[2] > 2 && holdingScreen) {
            Toast.makeText(this, "left", Toast.LENGTH_SHORT).show()
            rotateBoard(true)
        } else if (event.values[2] < -2 && holdingScreen) {
            Toast.makeText(this, "right", Toast.LENGTH_SHORT).show()
            rotateBoard(false)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onResume() {
        super.onResume()
        sensorManager!!.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager!!.unregisterListener(this)
    }
}
