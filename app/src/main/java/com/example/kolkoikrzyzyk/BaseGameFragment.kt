package com.example.kolkoikrzyzyk

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.kolkoikrzyzyk.model.game.Field
import com.example.kolkoikrzyzyk.model.game.FieldType
import com.example.kolkoikrzyzyk.model.game.GameResult
import com.example.kolkoikrzyzyk.model.game.PlayerType
import com.example.kolkoikrzyzyk.viewModels.GameViewModel
import com.example.kolkoikrzyzyk.viewModels.TournamentViewModel
import kotlinx.android.synthetic.main.fragment_game.*


abstract class BaseGameFragment : Fragment() {
    protected val gameViewModel: GameViewModel by viewModels()
    private val tournamentViewModel: TournamentViewModel by activityViewModels()
    private var startingClick = true
    var timerHandler: Handler? = null
    var timePassed = 1
    protected var isTournament = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        gameViewModel.gameResult.observe(viewLifecycleOwner, { result ->
            when (result) {
                is GameResult.Over -> {
                    if (isTournament) {
                        tournamentViewModel.addResult(
                            gameViewModel.crossUser,
                            gameViewModel.noughtUser,
                            result
                        )
                    }
                    onWin(result)
                    val winnerName = when (result.winner) {
                        PlayerType.Nought -> gameViewModel.noughtUser
                        PlayerType.Cross -> gameViewModel.crossUser
                    }.name
                    endGameButton.text = "$winnerName wygrał"
                    endGameButton.visibility = View.VISIBLE
                    timerHandler?.removeCallbacksAndMessages(null)
                    timerHandler = null
                }
                GameResult.Draw -> {
                    if (isTournament) {
                        tournamentViewModel.addResult(
                            gameViewModel.crossUser,
                            gameViewModel.noughtUser,
                            result
                        )
                    }
                    onDraw()
                    endGameButton.text = "Remis"
                    endGameButton.visibility = View.VISIBLE
                    timerHandler?.removeCallbacksAndMessages(null)
                    timerHandler = null
                }
                GameResult.Pending, null -> {
                    if (startingClick) {
                        startTimer()
                    }
                }
            }
        })

        gameViewModel.currentPlayer.observe(viewLifecycleOwner) {
            val image =
                if (it == PlayerType.Cross) R.drawable.white_cross else R.drawable.white_nought
            val name =
                if (it == PlayerType.Cross) gameViewModel.crossUser.name else gameViewModel.noughtUser.name
            currentPlayerImageView.setImageResource(image)
            currentPlayerName.text = name
        }

    }

    protected abstract fun onDraw()

    protected abstract fun onWin(result: GameResult.Over)

    protected fun linearLayoutContainer(layoutOrientation: Int): LinearLayout {
        return LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = layoutOrientation
        }
    }

    protected fun emptyButton(buttonSize: Int, x: Int, y: Int, z: Int): ImageButton =
        ImageButton(
            requireContext()
        )
            .apply {
                layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize).apply {
                    setMargins(0, 0, 0, 0)
                }
                setPadding(0, 0, 0, 0)
                id = View.generateViewId()
                setOnClickListener {
                    Log.d("BaseGameFragment", "$x, $y, $z")
                    gameViewModel.makeMove(x, y, z)
                }
                setBackgroundColor(resources.getColor(R.color.secondaryLightColor))
                setFieldImage(FieldType.Empty)
            }

    protected fun ImageButton.setFieldImage(
        field: FieldType
    ) {
        setImageResource(
            when (field) {
                FieldType.Cross -> R.drawable.cross
                FieldType.Nought -> R.drawable.nought
                FieldType.Empty -> R.drawable.border
            }
        )
    }

    protected fun onMarkPlaced(
        button: ImageButton,
        field: Field
    ) {
        button.isClickable = field.type == FieldType.Empty
        button.setFieldImage(field.type)
        ValueAnimator.ofArgb(
            resources.getColor(R.color.secondaryLightColor),
            resources.getColor(R.color.warningColor),
            resources.getColor(R.color.secondaryLightColor)
        ).apply {
            duration = 400
            addUpdateListener { valueAnimator ->
                button.setBackgroundColor(valueAnimator.animatedValue as Int)
            }
            start()
        }
    }

    private fun startTimer() {
        startingClick = false
        timerHandler = Handler()
        timerHandler?.postDelayed(object : Runnable {
            override fun run() {
                val minutes = (timePassed / 60).toString().padStart(2, '0')
                val seconds = (timePassed % 60).toString().padStart(2, '0')
                timer?.let {
                    it.text = "$minutes:$seconds"
                    timerHandler?.postDelayed(this, 1000)
                    timePassed++
                }
            }

        }, 1000)
    }

    protected fun onWinAnimation(
        button: ImageButton,
        field: Field
    ) {
        button.isClickable = field.type == FieldType.Empty
        button.setFieldImage(field.type)
        ValueAnimator.ofArgb(
            resources.getColor(R.color.secondaryLightColor),
            resources.getColor(R.color.warningColor),
        ).apply {
            duration = 400
            addUpdateListener { valueAnimator ->
                button.setBackgroundColor(valueAnimator.animatedValue as Int)
            }
            start()
        }
    }
}
