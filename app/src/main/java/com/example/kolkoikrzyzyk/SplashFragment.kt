package com.example.kolkoikrzyzyk

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.animation.doOnStart
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
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val valueAnimator = ValueAnimator.ofFloat(0f, 360f).apply {
            repeatCount = 10
            duration = 500
            addUpdateListener {
                image.rotation = it.animatedValue as Float
            }
            start()
        }
        usersViewModel.users.observe(viewLifecycleOwner) {users ->
            Handler().postDelayed({
                valueAnimator.cancel()
                    findNavController().navigate(
                        if (users.size == 0) {
                            SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                        } else {
                            SplashFragmentDirections.actionSplashFragmentToMainFragment()
                        })
            }, 500)

        }
    }
}