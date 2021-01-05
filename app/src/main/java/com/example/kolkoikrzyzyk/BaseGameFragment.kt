package com.example.kolkoikrzyzyk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.kolkoikrzyzyk.model.game.FieldType
import com.example.kolkoikrzyzyk.model.game.GameResult
import com.example.kolkoikrzyzyk.viewModels.GameViewModel


abstract class BaseGameFragment : Fragment() {

    //    protected val usersViewModel: UsersViewModel by activityViewModels()
    protected val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

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
                is GameResult.Over -> onWin(result)
                GameResult.Draw -> onDraw()
                GameResult.Pending, null -> {
                }
            }
        })
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
                    gameViewModel.makeMove(x, y, z)
                }

            }

    protected fun setButtonImage(
        button: ImageButton,
        field: FieldType
    ) {
        button.setImageResource(
            when (field) {
                FieldType.Cross -> R.drawable.cross
                FieldType.Nought -> R.drawable.nought
                FieldType.Empty -> R.drawable.border
            }
        )
    }
}
