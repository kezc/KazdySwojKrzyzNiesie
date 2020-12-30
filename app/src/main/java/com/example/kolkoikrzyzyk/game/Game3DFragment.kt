package com.example.kolkoikrzyzyk.game

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import com.example.kolkoikrzyzyk.R
import com.example.kolkoikrzyzyk.dpToPixels
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.roundToInt

class Game3DFragment : BaseGameFragment() {
    private val TAG = "Game3DFragment"

    private val buttons = mutableListOf<List<ImageButton>>() // add one more dimension
    private val boards = mutableListOf<LinearLayout>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.users.observe(viewLifecycleOwner, {
            Log.d(TAG, it.toString())
        })

        val size = 4
        val buttonSize = (if (size == 4) 36f else 64f).dpToPixels(resources).roundToInt()
        Log.d(TAG, "button size: $buttonSize")


        for (i in 0 until size) {
            val column = linearLayoutContainer(LinearLayout.VERTICAL).apply {
                rotationX = 60f
            }
            for (j in 0 until size) {
                val row = linearLayoutContainer(LinearLayout.HORIZONTAL)
                val list = mutableListOf<ImageButton>()
                buttons.add(list)
                for (k in 0 until size) {
                    val button = emptyButton(buttonSize).apply {
                        setImageResource(if (k%2 == 0) R.drawable.nought else R.drawable.cross)
                    }
                    list.add(button)
                    row.addView(button)
                }
                column.addView(row)
            }
            gameContainer.addView(column)
            boards.add(column)
        }
        setRotation()
    }


    private fun setRotation() {
        gameContainer.setOnTouchListener(object : View.OnTouchListener {
            var previousX = 0F
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                when (event.action) {
                    MotionEvent.ACTION_MOVE -> {
                        Log.d(TAG, "Move")
                        val dx = event.rawX - previousX
                        boards.forEach {
                            it.rotation += dx / gameContainer.width * 360
                        }
                        previousX = event.rawX
                    }
                    MotionEvent.ACTION_DOWN -> {
                        Log.d(TAG, "Down")
                        previousX = event.rawX
                    }
                }
                return true
            }
        })
    }
}
