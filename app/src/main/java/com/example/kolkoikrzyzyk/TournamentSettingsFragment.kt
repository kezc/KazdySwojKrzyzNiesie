package com.example.kolkoikrzyzyk

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.kolkoikrzyzyk.model.User
import com.example.kolkoikrzyzyk.model.game.PlayerType
import com.example.kolkoikrzyzyk.viewModels.TournamentViewModel
import com.example.kolkoikrzyzyk.viewModels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_tournament_settings.*
import kotlinx.android.synthetic.main.fragment_tournament_settings.createTournamentButton
import kotlinx.android.synthetic.main.fragment_tournament_settings.sizeRadioGroup
import kotlinx.android.synthetic.main.fragment_tournament_settings.switch3D

class TournamentSettingsFragment : Fragment() {
    private val usersViewModel: UsersViewModel by activityViewModels()
    private val tournamentViewModel: TournamentViewModel by activityViewModels()
    private var users = listOf<User>()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tournament_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        usersViewModel.users.observe(viewLifecycleOwner) {
            users = it.toList()
        }
        playerListButton.setOnClickListener {
            dialog()
        }
        createTournamentButton.setOnClickListener {
            if (tournamentViewModel.players.size < 2) {
                errorMessage.text = "You need to select at least 2 users"

            } else {
                tournamentViewModel.tournamentName = nameEditText.text.toString()
                tournamentViewModel.createTournament()

            }
        }

        tournamentViewModel.tournamentCreated.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(TournamentSettingsFragmentDirections.actionTournamentSettingsFragmentToTournamentDetailsFragment())
            }
        }

        tournamentViewModel.nameOccupied.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                errorMessage.text = "There's tournament with this name"
            }
        }

        sizeRadioGroup.setOnCheckedChangeListener { _, id ->
            tournamentViewModel.gameSize = when (id) {
                R.id.radioButtonSize3 -> 3
                R.id.radioButtonSize4 -> 4
                else -> 3
            }
        }
        switch3D.setOnCheckedChangeListener { _, isChecked -> tournamentViewModel.is3d = isChecked }
    }

    private fun dialog() {
        val textView = TextView(context).apply {
            text = "Select players"
            setPadding(20, 30, 20, 30)
            textSize = 20f
            setTextColor(Color.BLACK)
        }
        val usersNames = users.map { it.name }.toTypedArray()
        val checkedIndexes = users.mapIndexed { i, user ->
            tournamentViewModel.players.contains(user)
        }.toBooleanArray()
        val builder = AlertDialog.Builder(
            requireContext(),
            R.style.Base_ThemeOverlay_MaterialComponents_Dialog_Alert
        ).apply {
            setCustomTitle(textView)
            setMultiChoiceItems(
                usersNames, checkedIndexes
            ) { dialog, id, isChecked ->
                Log.d("TournamentFragment", "id $id")
                if (isChecked)
                    tournamentViewModel.addPlayer(users[id])
                else
                    tournamentViewModel.removePlayer(users[id])
            }
        }
        val alert = builder.create()
        alert.show()
    }

}