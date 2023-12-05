package com.example.Login_DB

import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class Pomodoro_timer : AppCompatActivity() {
    private lateinit var binding : Pomodoro_timer
    private lateinit var timerTextView: TextView
    private lateinit var statusTextView: TextView
    private lateinit var startButton: Button
    private lateinit var pauseButton: Button
    private lateinit var resetButton: Button
    private lateinit var countDownTimer: CountDownTimer

    private var isTimerRunning = false
    private var timeLeftInMillis: Long = 1500000 // 25 minutes in milliseconds
    private val workDuration: Long = 1500000 // 25 minutes in milliseconds
    private val breakDuration: Long = 300000 // 5 minutes in milliseconds
    private var cycleCount = 0
    private val totalCycles = 4
    private val countdownInterval: Long = 1000 // 1 second

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.timer_pomodoro)

        timerTextView = findViewById(R.id.timerTextView)
        statusTextView = findViewById(R.id.statusTextView)
        startButton = findViewById(R.id.startButton)
        pauseButton = findViewById(R.id.pauseButton)
        resetButton = findViewById(R.id.resetButton)

        startButton.setOnClickListener {
            startTimer()
        }

        pauseButton.setOnClickListener {
            pauseTimer()
        }

        resetButton.setOnClickListener {
            resetTimer()
        }
    }

    private fun startTimer() {
        if (!isTimerRunning) {
            countDownTimer = object : CountDownTimer(timeLeftInMillis, countdownInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeftInMillis = millisUntilFinished
                    updateTimer()
                    updateStatusText()
                }

                override fun onFinish() {
                    isTimerRunning = false
                    startButton.isEnabled = true
                    pauseButton.isEnabled = false
                    resetButton.isEnabled = true

                    if (cycleCount < totalCycles) {
                        cycleCount++
                        timeLeftInMillis = breakDuration
                        startTimer()
                    } else {
                        resetTimer()
                    }
                }
            }.start()

            isTimerRunning = true
            startButton.isEnabled = false
            pauseButton.isEnabled = true
            resetButton.isEnabled = false
        }
    }

    private fun pauseTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
        startButton.isEnabled = true
        pauseButton.isEnabled = false
        resetButton.isEnabled = true
    }

    private fun resetTimer() {
        countDownTimer.cancel()
        isTimerRunning = false
        cycleCount = 0
        timeLeftInMillis = workDuration
        updateTimer()
        startButton.isEnabled = true
        pauseButton.isEnabled = false
        resetButton.isEnabled = false
        statusTextView.text = ""
        statusTextView.visibility = TextView.INVISIBLE
    }

    private fun updateTimer() {
        val minutes = (timeLeftInMillis / 1000) / 60
        val seconds = (timeLeftInMillis / 1000) % 60

        val timeLeftFormatted = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = timeLeftFormatted
    }

    private fun updateStatusText() {
        if (timeLeftInMillis <= workDuration) {
            statusTextView.text = "Do your task!"
            statusTextView.visibility = TextView.VISIBLE
        } else {
            statusTextView.text = "Take a break!"
            statusTextView.visibility = TextView.VISIBLE
        }
    }
}
