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
import androidx.fragment.app.activityViewModels
import com.example.kolkoikrzyzyk.model.User
import com.example.kolkoikrzyzyk.model.game.PlayerType
import com.example.kolkoikrzyzyk.viewModels.TournamentViewModel
import com.example.kolkoikrzyzyk.viewModels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_tournament_settings.*

class TournamentSettingsFragment : Fragment() {
    private val computerUser = User(-1, "Computer", "")
    private val usersViewModel: UsersViewModel by activityViewModels()
    private val tournamentViewModel: TournamentViewModel by activityViewModels()
    private var users = listOf(computerUser)


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
            users = listOf(computerUser) + it.toList()
        }
        playerListButton.setOnClickListener{
            dialog()
        }
    }

    fun dialog() {
        val textView = TextView(context).apply {
            text = "Select players"
            setPadding(20, 30, 20, 30)
            textSize = 20f
            setTextColor(Color.BLACK)
        }
        val usersNames = users.map { it.name }.toTypedArray()
        val checkedIndexes = users.mapIndexed {i, user ->
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
                tournamentViewModel.players.add(users[id])
                dialog.dismiss()
            }
        }
        val alert = builder.create()
        alert.show()
    }

}