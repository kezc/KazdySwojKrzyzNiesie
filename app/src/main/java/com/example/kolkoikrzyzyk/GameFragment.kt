package com.example.kolkoikrzyzyk

import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_game.*
import kotlin.math.roundToInt

class GameFragment : Fragment() {
    private val TAG = "GameFragment"

    private val viewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    private val buttons = mutableListOf<List<ImageButton>>()
    private val boards = mutableListOf<LinearLayout>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.users.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it.toString())
        })

        val size = 3
        val buttonSize = (if (size == 4) 48f else 64f).dpToPixels().roundToInt()
//        gameContainer.setVerticalGravity(if (size == 4) Gravity.TOP else Gravity.CENTER)
        for (i in 0 until size) {
            val column = LinearLayout(requireContext()).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                )
                orientation = LinearLayout.VERTICAL
                rotationX = 60f
            }
            for (j in 0 until size) {
                val row = LinearLayout(requireContext()).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    orientation = LinearLayout.HORIZONTAL
                }
                val list = mutableListOf<ImageButton>()
                buttons.add(list)
                for (k in 0 until size) {
                    val button =
                        ImageButton(
                            requireContext()
                        )
                            .apply {
//                                text = ""
                                setImageResource(R.drawable.ic_launcher_foreground)
                                layoutParams = LinearLayout.LayoutParams(buttonSize, buttonSize)
                                id = View.generateViewId()
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
                        Log.d("MainActivity", "Move")
                        val dx = event.rawX - previousX
                        boards.forEach {
                            it.rotation += dx / gameContainer.width * 360
                        }
                        previousX = event.rawX
                    }
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("MainActivity", "Down")
                        previousX = event.rawX
                    }
                }
                return true
            }
        })
    }

    private fun Float.dpToPixels(): Float {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            this,
            resources.displayMetrics
        )
    }
}
