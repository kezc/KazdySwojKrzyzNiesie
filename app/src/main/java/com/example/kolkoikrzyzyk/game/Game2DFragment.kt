package com.example.kolkoikrzyzyk.game

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import com.example.kolkoikrzyzyk.dpToPixels
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.roundToInt

class Game2DFragment : BaseGameFragment() {

    private val buttons = mutableListOf<List<ImageButton>>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val size = 3
        val buttonSize = (if (size == 4) 48f else 64f).dpToPixels(resources).roundToInt()

        val column = linearLayoutContainer(LinearLayout.VERTICAL).apply {
            rotationX = 60f
        }
        for (i in 0 until size) {
            val row = linearLayoutContainer(LinearLayout.HORIZONTAL)
            val list = mutableListOf<ImageButton>()
            buttons.add(list)
            for (j in 0 until size) {
                val button = emptyButton(buttonSize)
                list.add(button)
                row.addView(button)
            }
            column.addView(row)
        }
        gameContainer.addView(column)
    }

}