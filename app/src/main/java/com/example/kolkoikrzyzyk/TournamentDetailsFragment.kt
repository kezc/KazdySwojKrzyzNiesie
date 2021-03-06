package com.example.kolkoikrzyzyk

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kolkoikrzyzyk.model.User
import com.example.kolkoikrzyzyk.viewModels.TournamentViewModel
import com.example.kolkoikrzyzyk.viewModels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_tournament_details.*
import kotlinx.android.synthetic.main.result_card.player1
import kotlinx.android.synthetic.main.result_card.player2


class TournamentDetailsFragment : Fragment() {
    private val tournamentViewModel: TournamentViewModel by activityViewModels()
    private val usersViewModel: UsersViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tournament_details, container, false)
    }

    private var user1: User? = null
    private var user2: User? = null
    private var usersSet = mutableSetOf<User>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        usersViewModel.users.observe(viewLifecycleOwner) {
            usersSet = it
            checkIfUsersLoggedIn()
        }

        header.text = tournamentViewModel.tournamentName
        tournamentViewModel.nextMatch()?.observe(viewLifecycleOwner) { pair ->
            if (pair != null) {
                user1 = pair.first
                user2 = pair.second
                player1.text = pair.first.name
                player2.text = pair.second.name
            }
            checkIfUsersLoggedIn()
        }

        tournamentViewModel.getTournamentWinnerUid()?.observe(viewLifecycleOwner) { users ->
            users?.let {
                val winners = users.map { it?.name }.joinToString()
                nextMatchText.text = "Koniec turnieju.\nZwyciestwo: $winners"
                player1.visibility = View.GONE
                player2.visibility = View.GONE
                playButton.visibility = View.GONE
            }
        }

        val adapter = GamesHistoryViewAdapter()
        historyList.adapter = adapter
        historyList.layoutManager = LinearLayoutManager(requireContext())
        tournamentViewModel.getTournamentMatches()?.observe(viewLifecycleOwner) { matches ->
            if (matches.isNotEmpty()) {
                noGamesText.visibility = View.INVISIBLE
            } else {
                noGamesText.visibility = View.VISIBLE
            }
            val list = matches.map { gameResult ->
                val player1 =
                    tournamentViewModel.players.find { it.uid == gameResult.uidOne }?.name ?: ""
                val player2 =
                    tournamentViewModel.players.find { it.uid == gameResult.uidTwo }?.name ?: ""
                val result = when (gameResult.result) {
                    0 -> "Remis"
                    1 -> "Wygrany: $player1"
                    2 -> "Wygrany: $player2"
                    else -> ""
                }
                listOf(player1, player2, result)
            }.reversed()
            adapter.submitList(list)
        }

        playButton.setOnClickListener {
            val imm =
                ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
            handleButtonClick()
        }
    }

    private fun handleButtonClick() {
        val user1Temp = user1
        val user2Temp = user2
        if (user1Temp != null && user2Temp != null && tournamentViewModel.tournament?.isOver == false) {
            if (!usersSet.containsAll(listOf(user1, user2))) return
            when (tournamentViewModel.is3d) {
                true -> findNavController()
                    .navigate(
                        TournamentDetailsFragmentDirections.actionTournamentDetailsFragmentToGame3DFragment(
                            user1Temp,
                            user2Temp,
                            tournamentViewModel.gameSize,
                            true
                        )
                    )
                false -> findNavController()
                    .navigate(
                        TournamentDetailsFragmentDirections.actionTournamentDetailsFragmentToGame2DFragment(
                            user1Temp,
                            user2Temp,
                            tournamentViewModel.gameSize,
                            true
                        )
                    )
            }
        } else {
            player1.text = ""
            player2.text = ""
        }
    }

    private fun checkIfUsersLoggedIn() {
        val tempUser1 = user1
        val tempUser2 = user2
        if (tempUser1 != null && tempUser2 != null) {
            if (usersSet.containsAll(listOf(tempUser1, tempUser2))) {
                playButton.text = "Graj"
                playButton.setOnClickListener {
                    handleButtonClick()
                }
            } else {
                playButton.text = "Obaj gracze muszą być zalogowani"
                val missingName = if (!usersSet.contains(user1)) {
                    tempUser1.name
                } else {
                    tempUser2.name
                }
                playButton.setOnClickListener {
                    findNavController()
                        .navigate(
                            TournamentDetailsFragmentDirections.actionTournamentDetailsFragmentToLoginFragment(
                                missingName
                            )
                        )
                }
            }
        }
    }
}