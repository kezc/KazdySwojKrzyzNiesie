package com.example.kolkoikrzyzyk

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.kolkoikrzyzyk.viewModels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : Fragment() {
    private val TAG = "LoginFragment"

    private val viewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        registerButton.setOnClickListener {
            view.findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToRegisterFragment()
            )
        }

        confirmButton.setOnClickListener {
            viewModel.login(nameEditText.text.toString(), passwordEditText.text.toString())
        }

        viewModel.users.observe(viewLifecycleOwner) {
            Log.d(TAG, it.toString())
        }

        viewModel.loginSuccessful.observe(viewLifecycleOwner) { event ->
            Log.d(TAG, "${event.hasBeenHandled} ${event.peekContent()}")


            event.getContentIfNotHandled()?.let {
                if (it) {
                    nameEditText.setText("")
                    passwordEditText.setText("")
                    val imm = getSystemService(requireContext(), InputMethodManager::class.java)
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)

                    view.findNavController().navigate(
                        LoginFragmentDirections.actionLoginFragmentToMainFragment()
                    )
                } else {
                    errorMessage.text = "Log in was not successful"
                }
            }
        }

    }
}