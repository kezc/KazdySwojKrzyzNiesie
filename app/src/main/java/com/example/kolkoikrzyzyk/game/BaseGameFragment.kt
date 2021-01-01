package com.example.kolkoikrzyzyk.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.kolkoikrzyzyk.R
import com.example.kolkoikrzyzyk.model.game.FieldType
import com.example.kolkoikrzyzyk.viewModels.GameViewModel
import com.example.kolkoikrzyzyk.viewModels.UsersViewModel


open class BaseGameFragment : Fragment() {

    protected val usersViewModel: UsersViewModel by activityViewModels()
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

    protected fun linearLayoutContainer(layoutOrientation: Int): LinearLayout {
        return LinearLayout(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            orientation = layoutOrientation
        }
    }

    protected fun emptyButton(buttonSize: Int): ImageButton =
        ImageButton(
            requireContext()
        )
            .apply {
                setImageResource(R.drawable.ic_launcher_foreground)
                layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
                id = View.generateViewId()
            }

    protected fun setButtonImage(
        button: ImageButton,
        field: FieldType
    ) {
        button.setImageResource(
            when (field) {
                FieldType.Cross -> R.drawable.cross
                FieldType.Nought -> R.drawable.nought
                FieldType.Empty -> R.drawable.ic_launcher_background
            }
        )
    }
}
