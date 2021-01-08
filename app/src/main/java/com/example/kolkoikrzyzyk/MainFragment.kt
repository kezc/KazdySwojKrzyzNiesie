package com.example.kolkoikrzyzyk

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kolkoikrzyzyk.viewModels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    private val usersViewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)
        view.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSingleGameSettingsFragment())
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRankingFragment())
        }
        button2.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToTournamentSettingsFragment())
        }
        button3.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToGameHistoryFragment())
        }
        val adapter = UsersViewAdapter { user ->
            usersViewModel.logout(user)
        }
        usersList.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        usersList.adapter = adapter

        usersViewModel.users.observe(viewLifecycleOwner) {
            adapter.submitList(it.toList())
        }

        usersViewModel.noUsersLeft.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(MainFragmentDirections.actionMainFragmentToLoginFragmentWithoutStack())
            }
        }

    }

}