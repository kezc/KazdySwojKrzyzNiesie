package com.example.kolkoikrzyzyk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.login_fragment.*


class LoginFragment : Fragment() {
    private val TAG = "LoginFragment"

    private val viewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.login_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        confirmButton.setOnClickListener {
            viewModel.addUser(nameEditText.text.toString())
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment, GameFragment(), "tag")
            transaction.disallowAddToBackStack()
            transaction.commit()
        }

        viewModel.users.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, it.toString())
        })
    }
}