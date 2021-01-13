package com.example.kolkoikrzyzyk

import android.os.Bundle
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
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        singleGameButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToSingleGameSettingsFragment())
        }

        rankingButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToRankingFragment())
        }

        tournamentButton.setOnClickListener {
            findNavController().navigate(MainFragmentDirections.actionMainFragmentToTournamentSettingsFragment())
        }

        historyButton.setOnClickListener {
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