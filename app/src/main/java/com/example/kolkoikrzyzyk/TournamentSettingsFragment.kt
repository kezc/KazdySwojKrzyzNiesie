package com.example.kolkoikrzyzyk

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.kolkoikrzyzyk.database.Tournament
import com.example.kolkoikrzyzyk.model.User
import com.example.kolkoikrzyzyk.viewModels.TournamentViewModel
import com.example.kolkoikrzyzyk.viewModels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_tournament_settings.*

class TournamentSettingsFragment : Fragment() {
    private val usersViewModel: UsersViewModel by activityViewModels()
    private val tournamentViewModel: TournamentViewModel by activityViewModels()
    private var users = listOf<User>()
    private var allTournaments = listOf<Tournament>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tournament_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tournamentViewModel.players.clear()

        usersViewModel.users.observe(viewLifecycleOwner) {
            users = it.toList()
        }

        playerListButton.setOnClickListener {
            choosePlayers()
        }

        createTournamentButton.setOnClickListener {
            val imm =
                ContextCompat.getSystemService(requireContext(), InputMethodManager::class.java)
            imm?.hideSoftInputFromWindow(view.windowToken, 0)

            if (tournamentViewModel.players.size < 2) {
                errorMessage.text = "Należy wybrać przynajmniej 2 użytkowników"
            } else if (nameEditText.text.isBlank()) {
                errorMessage.text = "Nazwa nie może być pusta"
            } else {
                tournamentViewModel.tournamentName = nameEditText.text.toString()
                tournamentViewModel.createTournament()
            }
        }

        tournamentViewModel.tournaments.observe(viewLifecycleOwner) {
            allTournaments = it
            allTournamentsProgressBar.visibility = View.INVISIBLE
            loadTournamentButton.visibility = View.VISIBLE
            if (it.isNotEmpty()) {
                loadTournamentButton.text = "Wczytaj turniej"
                loadTournamentButton.isEnabled = true
            } else {
                loadTournamentButton.text = "Brak turniejów do wczytania"
                loadTournamentButton.isEnabled = false
            }
        }

        tournamentViewModel.tournamentCreated.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(TournamentSettingsFragmentDirections.actionTournamentSettingsFragmentToTournamentDetailsFragment())
            }
        }

        tournamentViewModel.nameOccupied.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let {
                errorMessage.text = "Istnieje już turniej o tej nazwie"
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

        loadTournamentButton.setOnClickListener { selectTournamentDialog() }
    }

    private fun choosePlayers() {
        val textView = TextView(context).apply {
            text = "Wybierz graczy"
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
            setNeutralButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            setMultiChoiceItems(
                usersNames, checkedIndexes
            ) { _, id, isChecked ->
                if (isChecked)
                    tournamentViewModel.addPlayer(users[id])
                else
                    tournamentViewModel.removePlayer(users[id])
            }
        }
        val alert = builder.create()
        alert.show()
    }

    private fun selectTournamentDialog() {
        val textView = TextView(context).apply {
            text = "Wybierz turniej"
            setPadding(20, 30, 20, 30)
            textSize = 20f
            setTextColor(Color.BLACK)
        }

        val tournamentsNames = allTournaments.map { it.name }.toTypedArray()
        val builder = AlertDialog.Builder(
            requireContext(),
            R.style.Base_ThemeOverlay_MaterialComponents_Dialog_Alert
        ).apply {
            setCustomTitle(textView)
            setSingleChoiceItems(
                tournamentsNames, -1
            ) { dialog, item ->
                tournamentViewModel.loadTournament(tournamentsNames[item])
                dialog.dismiss()
            }
        }
        val alert = builder.create()
        alert.show()
    }
}