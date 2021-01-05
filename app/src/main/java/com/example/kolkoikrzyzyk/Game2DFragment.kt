package com.example.kolkoikrzyzyk

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import com.example.kolkoikrzyzyk.model.game.FieldType
import com.example.kolkoikrzyzyk.model.game.GameResult
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.roundToInt

class Game2DFragment : BaseGameFragment() {
    private val TAG = "Game2DFragment"

    private lateinit var buttons: List<List<ImageButton>>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val size = gameViewModel.size

        createBoard(size)
        gameViewModel.boardState.observe(viewLifecycleOwner, {
            it?.let {
                it[0].forEachIndexed { y, row ->
                    row.forEachIndexed { x, field ->
                        val button = buttons[y][x]
                        button.isClickable = field.type == FieldType.Empty
                        setButtonImage(button, field.type)
                    }
                }
            }
        })

        gameViewModel.computerThinking.observe(viewLifecycleOwner) {
            if (it) disableButtons()
            else enableButtons()
        }

        gameViewModel.startGame()
    }

    override fun onDraw() {
        Toast.makeText(requireContext(), "REMIS", Toast.LENGTH_LONG)
            .show()
        disableButtons()
    }

    override fun onWin(result: GameResult.Over) {
        Toast.makeText(
            requireContext(),
            "WYGRAL ${result.winner}",
            Toast.LENGTH_LONG
        ).show()
        disableButtons()
    }

    private fun disableButtons() {
        buttons.forEach { it.forEach { button -> button.isClickable = false } }
    }

    private fun enableButtons() {
        buttons.forEach { it.forEach { button -> button.isClickable = true } }
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
                val button = emptyButton(buttonSize, x, y, 0)
                list.add(button)
                row.addView(button)
            }
            column.addView(row)
        }
        gameContainer.addView(column)
        buttons = tempButtons
    }


}