package com.example.kolkoikrzyzyk

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_game_history.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class GameHistoryFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_game_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = GamesHistoryViewAdapter()
        historyList.adapter = adapter
        historyList.layoutManager = LinearLayoutManager(requireContext())
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            val lines = withContext(Dispatchers.IO) {
                try {
                    val file = File(requireContext().filesDir, "results.txt")
                    file.readLines().map { it.split(",") }.reversed()
                } catch (e: Exception) {
                    historyList.visibility = View.INVISIBLE
                    listOf()
                }
            }
            Log.d("GameHistoryFragment", lines.toString())
            if (lines.isEmpty()) {
                noResults.visibility = View.VISIBLE
            } else {
                noResults.visibility = View.INVISIBLE
            }
            withContext(Dispatchers.Main) {
                adapter.submitList(lines)
            }
        }
    }
}