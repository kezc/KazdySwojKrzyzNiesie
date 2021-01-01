package com.example.kolkoikrzyzyk.game

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.kolkoikrzyzyk.R
import com.example.kolkoikrzyzyk.dpToPixels
import com.example.kolkoikrzyzyk.model.game.FieldType
import com.example.kolkoikrzyzyk.model.game.GameResult
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.roundToInt

class Game2DFragment : BaseGameFragment() {

    private lateinit var buttons: List<List<ImageButton>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val size = 4

        createBoard(size)
        gameViewModel.boardState.observe(viewLifecycleOwner, {
            it?.let {
                it[0].forEachIndexed { y, row ->
                    row.forEachIndexed { x, field ->
                        val button = buttons[y][x]
                        button.isClickable = field == FieldType.Empty
                        setButtonImage(button, field)
                    }
                }
            }
        })

        gameViewModel.gameResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is GameResult.Over -> onWin(result)
                GameResult.Draw -> onDraw()
                GameResult.Pending, null -> {
                }
            }
        })
        gameViewModel.startGame()
    }

    private fun onDraw() {
        Toast.makeText(requireContext(), "REMIS", Toast.LENGTH_LONG)
            .show()
        buttons.forEach { it.forEach { button -> button.isClickable = false } }
    }

    private fun onWin(result: GameResult.Over) {
        Toast.makeText(
            requireContext(),
            "WYGRAL ${result.winner}",
            Toast.LENGTH_LONG
        ).show()
        buttons.forEach { it.forEach { button -> button.isClickable = false } }
    }

    private fun createBoard(size: Int) {
        val buttonSize = (if (size == 4) 64f else 96f).dpToPixels(resources).roundToInt()

        val tempButtons = mutableListOf<MutableList<ImageButton>>()
        val column = linearLayoutContainer(LinearLayout.VERTICAL)
        for (y in 0 until size) {
            val row = linearLayoutContainer(LinearLayout.HORIZONTAL)
            val list = mutableListOf<ImageButton>()
            tempButtons.add(list)
            for (x in 0 until size) {
                val button = emptyButton(buttonSize)
                button.setOnClickListener {
                    gameViewModel.makeMove(x, y, 0)
                }
                list.add(button)
                row.addView(button)
            }
            column.addView(row)
        }
        gameContainer.addView(column)
        buttons = tempButtons
    }


}