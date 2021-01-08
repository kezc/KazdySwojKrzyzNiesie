package com.example.kolkoikrzyzyk

import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.kolkoikrzyzyk.model.User
import com.example.kolkoikrzyzyk.model.game.PlayerType
import com.example.kolkoikrzyzyk.viewModels.SingleGameSettingsViewModel
import com.example.kolkoikrzyzyk.viewModels.UsersViewModel
import kotlinx.android.synthetic.main.fragment_single_game_settings.*


class SingleGameSettingsFragment : Fragment() {
    private val TAG = "SingleGameSettingsFragment"

    private val computerUser = User(-1, "Computer", "")
    private val usersViewModel: UsersViewModel by activityViewModels()
    private val singleGameSettingsViewModel: SingleGameSettingsViewModel by viewModels()
    private var users = listOf(computerUser)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_single_game_settings, container, false)

        usersViewModel.users.observe(viewLifecycleOwner) {
            users = listOf(computerUser) + it.toList()
        }
        singleGameSettingsViewModel.crossPlayer.observe(viewLifecycleOwner) { user ->
            user?.let {
                selectPlayerCross.text = it.name
            }
        }
        singleGameSettingsViewModel.noughtPlayer.observe(viewLifecycleOwner) { user ->
            user?.let {
                selectPlayerNought.text = it.name
            }
        }
        singleGameSettingsViewModel.selectError.observe(viewLifecycleOwner) {
            errorMessage.text = it
            createTournamentButton.isEnabled = it.isBlank()
        }

        singleGameSettingsViewModel.settingsCompleted.observe(viewLifecycleOwner) { event ->
            event.getContentIfNotHandled()?.let { players ->
                val playerNought = players.first
                val playerCross = players.second
                val action = when (singleGameSettingsViewModel.is3d) {
                    true -> SingleGameSettingsFragmentDirections.actionSingleGameSettingsFragmentToGame3DFragment(
                        playerNought,
                        playerCross,
                        singleGameSettingsViewModel.gameSize
                    )
                    false -> SingleGameSettingsFragmentDirections.actionSingleGameSettingsFragmentToGame2DFragment(
                        playerNought,
                        playerCross,
                        singleGameSettingsViewModel.gameSize
                    )
                }
                view.findNavController().navigate(action)
            }
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        selectPlayerNought.setOnClickListener {
            dialog(PlayerType.Nought)
        }
        selectPlayerCross.setOnClickListener {
            dialog(PlayerType.Cross)
        }
        createTournamentButton.setOnClickListener {
            singleGameSettingsViewModel.onPlay()
        }
        sizeRadioGroup.setOnCheckedChangeListener { _, id ->
            singleGameSettingsViewModel.gameSize = when (id) {
                R.id.radioButtonSize3 -> 3
                R.id.radioButtonSize4 -> 4
                else -> 3
            }
        }
        switch3D.setOnCheckedChangeListener { _, isChecked -> singleGameSettingsViewModel.is3d = isChecked }
    }

    fun dialog(player: PlayerType) {
        val textView = TextView(context).apply {
            text = "Select an option"
            setPadding(20, 30, 20, 30)
            textSize = 20f
            setTextColor(Color.BLACK)
        }
        val usersNames = users.map { it.name }.toTypedArray()
        val selectedPlayer = when (player) {
            PlayerType.Nought -> singleGameSettingsViewModel.noughtPlayer
            PlayerType.Cross -> singleGameSettingsViewModel.crossPlayer
        }.value
        val builder = AlertDialog.Builder(
            requireContext(),
            R.style.Base_ThemeOverlay_MaterialComponents_Dialog_Alert
        ).apply {
            setCustomTitle(textView)

            setSingleChoiceItems(
                usersNames, users.indexOf(selectedPlayer)
            ) { dialog, item ->
                when (player) {
                    PlayerType.Nought -> singleGameSettingsViewModel.setNoughtPlayer(users[item])
                    PlayerType.Cross -> singleGameSettingsViewModel.setCrossPlayer(users[item])
                }
                dialog.dismiss()
            }
        }
        val alert = builder.create()
        alert.show()
    }
}