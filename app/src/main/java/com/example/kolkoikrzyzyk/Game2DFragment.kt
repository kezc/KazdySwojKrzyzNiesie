package com.example.kolkoikrzyzyk

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kolkoikrzyzyk.model.game.FieldType
import com.example.kolkoikrzyzyk.model.game.GameResult
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.roundToInt

class Game2DFragment : BaseGameFragment() {
    private val TAG = "Game2DFragment"

    private lateinit var buttons: List<List<ImageButton>>
    private val args: Game2DFragmentArgs by navArgs()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel.is3D = false
        gameViewModel.size = args.size
        gameViewModel.noughtUser = args.noughtUser
        gameViewModel.crossUser = args.crossUser
        isTournament = args.isTournament

        createBoard(gameViewModel.size)
        gameViewModel.lastSuccessfulMove.observe(viewLifecycleOwner, {
            it?.let { field ->
                val button = buttons[field.y][field.x]
                button.isClickable = field.type == FieldType.Empty
                button.setFieldImage(field.type)
            }
        })

        endGameButton.setOnClickListener {
            if (isTournament) {
                findNavController().navigate(Game2DFragmentDirections.actionGame2DFragmentToTournamentDetailsFragment())
            } else {
                findNavController().navigate(Game2DFragmentDirections.actionGame2DFragmentToMainFragment())
            }
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
        result.winningFields.forEach { field ->
            onWinAnimation(buttons[field.y][field.x], field)
        }
    }

    override fun disableButtons() {
        buttons.forEach { it.forEach { button -> button.isClickable = false } }
    }

    override fun enableButtons() {
        buttons.forEach { it.forEach { button -> button.isClickable = true } }
    }

    private fun createBoard(size: Int) {
        val buttonSize = (if (size == 4) 84f else 96f).dpToPixels(resources).roundToInt()

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