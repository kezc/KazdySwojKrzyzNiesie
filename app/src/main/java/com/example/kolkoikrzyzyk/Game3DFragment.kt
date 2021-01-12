package com.example.kolkoikrzyzyk

import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.kolkoikrzyzyk.model.game.GameResult
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.roundToInt

class Game3DFragment : BaseGameFragment() {
    private val TAG = "Game3DFragment"

    private val args: Game3DFragmentArgs by navArgs()
    private lateinit var buttons: MutableList<List<List<ImageButton>>>
    private val boards = mutableListOf<LinearLayout>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel.is3D = true
        gameViewModel.size = args.size
        gameViewModel.noughtUser = args.noughtUser
        gameViewModel.crossUser = args.crossUser
        isTournament = args.isTournament
        Log.d(TAG, "nought ${gameViewModel.noughtUser} cross ${gameViewModel.crossUser}")

        createBoard(gameViewModel.size)

        gameViewModel.lastSuccessfulMove.observe(viewLifecycleOwner, {
            it?.let { field ->
                val button = buttons[field.z][field.y][field.x]
                onMarkPlaced(button, field)
            }
        })

        endGameButton.setOnClickListener {
//            if (isTournament) {
//                findNavController().navigate(Game3DFragmentDirections.actionGame3DFragmentToTournamentDetailsFragment())
//            } else {
//                findNavController().navigate(Game3DFragmentDirections.actionGame3DFragmentToMainFragment())
//            }
            findNavController().popBackStack()
        }

        gameViewModel.startGame()
    }

    private fun createBoard(size: Int) {
        val buttonSize = (if (size == 4) 44f else 64f).dpToPixels(resources).roundToInt()
        val tempButtons = mutableListOf<List<List<ImageButton>>>()

        for (z in 0 until size) {
            val column = linearLayoutContainer(LinearLayout.VERTICAL).apply {
                rotationX = 60f
            }
            val buttonBoard = mutableListOf<List<ImageButton>>()
            tempButtons.add(buttonBoard)
            for (y in 0 until size) {
                val row = linearLayoutContainer(LinearLayout.HORIZONTAL)
                val list = mutableListOf<ImageButton>()
                buttonBoard.add(list)
                for (x in 0 until size) {
                    val button = emptyButton(buttonSize, x, y, z)
                    list.add(button)
                    row.addView(button)
                }
                column.addView(row)
            }
            gameContainer.addView(column)
            boards.add(column)
        }
        buttons = tempButtons
        setRotation()
    }

    override fun onDraw() {
        disableButtons()
    }

    override fun onWin(result: GameResult.Over) {
        disableButtons()
        result.winningFields.forEach { field ->
            onWinAnimation(buttons[field.z][field.y][field.x], field)
        }
    }

    fun disableButtons() {
        buttons.forEach { board ->
            board.forEach { row ->
                row.forEach { button ->
                    button.isClickable = false
                }
            }
        }
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
