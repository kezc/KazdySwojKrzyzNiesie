package com.example.kolkoikrzyzyk

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import com.example.kolkoikrzyzyk.viewModels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_register.*


class RegisterFragment : Fragment() {
    private val TAG = "RegisterFragment"

    private val viewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.users.observe(viewLifecycleOwner, {
            Log.d(TAG, it.toString())
        })

        viewModel.operationSuccessful.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let {
                if (it) {
                    nameEditText.setText("")
                    passwordEditText.setText("")
                    val imm = ContextCompat.getSystemService(
                        requireContext(),
                        InputMethodManager::class.java
                    )
                    imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    view.findNavController().navigate(
                        RegisterFragmentDirections.actionRegisterFragmentToMainFragment()
                    )
                } else {
                    errorMessage.text = viewModel.message
                }
            }
        }

        confirmButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val password = passwordEditText.text.toString()
            viewModel.register(name, password)
        }
    }
}