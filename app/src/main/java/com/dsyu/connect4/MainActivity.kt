package com.dsyu.connect4

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayout
import android.view.MotionEvent
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SensorEventListener {

    private var isYellow = true
    private var gameOver = false
    private var holdingScreen = false
    private var discsUsed = 0

    private lateinit var sensorManager: SensorManager
    private lateinit var gyroscope: Sensor
    private lateinit var layout: View
    private lateinit var grid: GridLayout
    private lateinit var clockWiseAnimation: Animation
    private lateinit var counterClockWiseAnimation: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        this.layout = findViewById(R.id.constraintLayout)
        val winMessage: ImageView = findViewById(R.id.gameOverMessage)
        this.grid = findViewById(R.id.board)
        val restartButton: ImageView = findViewById(R.id.restartButton)

        this.clockWiseAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_clock_wise)
        this.counterClockWiseAnimation = AnimationUtils.loadAnimation(applicationContext, R.anim.rotate_counter_clock_wise)

        this.sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)?.let {
            this.gyroscope = it
        }

        for (i in 0 until grid.childCount) {
            val slot = grid.getChildAt(i)
            slot.setOnClickListener {
                if (!Board.getColumn(i % 6).isFull() && !gameOver) {
                    Board.getColumn(i % 6).addDisc(getDiscColor(isYellow))
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
        }

        restartButton.setOnClickListener {
            gameOver = false
            discsUsed = 0
            hideImage(winMessage)
            hideImage(restartButton)
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

    private fun updateBoard(afterRotation: Boolean = false) {
        for (row in 0..5) {
            for (col in 0..5) {
                val slot: ImageView? = findViewById(resources.getIdentifier("slot$row$col", "id", packageName))
                val newDiscInt = Board.getColumn(col).getDisk(row)
                val newDisc = getDiscDrawable(newDiscInt)
                val oldDisc = slot?.drawable

                if (newDisc?.constantState != oldDisc?.constantState || afterRotation) {
                    slot?.setImageDrawable(newDisc)
                    if (newDiscInt != EMPTY)
                        slot?.let { revealImage(it) }
                }
            }
        }
    }

    private fun endGame(winnerIsYellow: Boolean = isYellow) {
        gameOver = true

        if (winnerIsYellow != isYellow) {
            flipBackgroundColor()
            isYellow = winnerIsYellow
        }

        when (winnerIsYellow) {
            true -> gameOverMessage.setImageDrawable(getDrawable(R.drawable.win_yellow))
            false -> gameOverMessage.setImageDrawable(getDrawable(R.drawable.win_red))
        }
        revealImage(gameOverMessage)
        revealImage(restartButton)
    }

    private fun tieGame() {
        gameOver = true
        gameOverMessage.setImageDrawable(getDrawable(R.drawable.tie))
        revealImage(gameOverMessage)
        revealImage(restartButton)
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

    private fun rotateBoard(directionIsLeft: Boolean) {
        holdingScreen = false

        if (directionIsLeft) {
            Board.rotateLeft()
            grid.startAnimation(counterClockWiseAnimation)
        } else {
            Board.rotateRight()
            grid.startAnimation(clockWiseAnimation)
        }

        val handler = Handler()
        handler.postDelayed({
            updateBoard(true)
            val yellowWon = Board.winCheck(YELLOW)
            val redWon = Board.winCheck(RED)

            if (yellowWon && redWon) {
                tieGame()
            } else if (yellowWon) {
                endGame(true)
            } else if (redWon) {
                endGame(false)
            } else {
                flipBackgroundColor()
                isYellow = !isYellow
            }
        }, 700)
    }

    private fun flipBackgroundColor() {
        var startColor = ContextCompat.getColor(this, R.color.bgRed)
        var endColor = ContextCompat.getColor(this, R.color.bgYellow)

        if (isYellow) {
            startColor = endColor.also { endColor = startColor }
        }

        val objectAnimator = ObjectAnimator.ofObject(layout, "backgroundColor", ArgbEvaluator(), startColor, endColor)
        objectAnimator.duration = 200
        objectAnimator.start()
    }

    private fun revealImage(image: ImageView) {
        val cx = image.width / 2
        val cy = image.height / 2
        val finalRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(image, cx, cy, 0f, finalRadius)
        image.visibility = View.VISIBLE
        anim.start()
    }

    private fun hideImage(image: ImageView) {
        val cx = image.width / 2
        val cy = image.height / 2
        val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

        val anim = ViewAnimationUtils.createCircularReveal(image, cx, cy, initialRadius, 0f)
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                image.visibility = View.INVISIBLE
            }
        })
        anim.start()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type != Sensor.TYPE_GYROSCOPE || gameOver) return

        if (event.values[2] > 3 && holdingScreen) {
            rotateBoard(true)
        } else if (event.values[2] < -3 && holdingScreen) {
            rotateBoard(false)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onResume() {
        super.onResume()
        sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME)
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}
