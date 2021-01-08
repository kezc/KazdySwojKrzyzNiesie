package com.example.kolkoikrzyzyk

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kolkoikrzyzyk.model.User
import com.example.kolkoikrzyzyk.viewModels.TournamentViewModel
import kotlinx.android.synthetic.main.fragment_tournament_details.*
import kotlinx.android.synthetic.main.result_card.player1
import kotlinx.android.synthetic.main.result_card.player2


class TournamentDetailsFragment : Fragment() {
    private val tournamentViewModel: TournamentViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tournament_details, container, false)
    }

    private var user1: User? = null
    private var user2: User? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("TournamentDetailsFragme", "${tournamentViewModel.tournament.toString()}")

        header.text = tournamentViewModel.tournamentName
        tournamentViewModel.nextMatch()?.observe(viewLifecycleOwner) { pair ->
            if (pair != null) {
                user1 = pair.first
                user2 = pair.second
                player1.text = pair.first.name
                player2.text = pair.second.name
            }
        }

        tournamentViewModel.isTournamentOver()?.observe(viewLifecycleOwner) {
            if (it) {
                nextMatchText.text = "Tournament is over"
                player1.text = ""
                player2.text = ""
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
                    tournamentViewModel.players.find { it.uid == gameResult.uidOne }?.name?.let { "$it won" }
                val player2 =
                    tournamentViewModel.players.find { it.uid == gameResult.uidTwo }?.name?.let { "$it won" }
                val result = when (gameResult.result) {
                    0 -> "draw"
                    1 -> player1
                    2 -> player2
                    else -> ""
                }
                listOf(player1?: "", player2?: "", result?: "")
            }
            adapter.submitList(list)
        }

        playButton.setOnClickListener {
            val user1Temp = user1
            val user2Temp = user2
            if (user1Temp != null && user2Temp != null) {
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

    }
}