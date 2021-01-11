package com.example.kolkoikrzyzyk

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.kolkoikrzyzyk.viewModels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_splash.*


class SplashFragment : Fragment() {
    private val usersViewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_splash, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val valueAnimator = ValueAnimator.ofFloat(1F, 1.2F, 1F, 0.8F).apply {
            repeatCount = 10
            duration = 400
            addUpdateListener {
                image.scaleX = it.animatedValue as Float
                image.scaleY = it.animatedValue as Float
            }
            start()
        }
        usersViewModel.users.observe(viewLifecycleOwner) { users ->
            Handler().postDelayed({
                valueAnimator.cancel()
                if (users.size == 0) {
                    findNavController().navigate(
                        SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                    )
                } else {
                    findNavController().navigate(
                        SplashFragmentDirections.actionSplashFragmentToMainFragment()
                    )
                }

            }, 500)

        }
    }
}