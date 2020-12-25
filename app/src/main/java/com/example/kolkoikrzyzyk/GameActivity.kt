package com.example.kolkoikrzyzyk

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.children

class GameActivity : Activity() {

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)
        val firstBoard = findViewById<ConstraintLayout>(R.id.firstBoard)
        val secondBoard = findViewById<ConstraintLayout>(R.id.secondBoard)
        val thirdBoard = findViewById<ConstraintLayout>(R.id.thirdBoard)
        val boards = listOf(firstBoard, secondBoard, thirdBoard)
        val background = findViewById<ConstraintLayout>(R.id.background)
        background.setOnTouchListener(object : View.OnTouchListener {
            var previousX = 0F
            var previousY = 0F
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_MOVE -> {
                        Log.d("MainActivity", "Move")
                        val dx = event.rawX - previousX
                        val dy = event.rawY - previousY
                        boards.forEach {
                            it.rotation += dx / background.width * 360
                        }
                        previousX = event.rawX
                        previousY = event.rawY
                    }
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("MainActivity", "Down")
                        previousX = event.rawX
                        previousY = event.rawY
                    }
                }
                return true
            }
        })
    }

}