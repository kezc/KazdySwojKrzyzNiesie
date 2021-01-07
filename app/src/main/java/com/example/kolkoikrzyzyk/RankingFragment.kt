package com.example.kolkoikrzyzyk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kolkoikrzyzyk.viewModels.RankingViewModel
import kotlinx.android.synthetic.main.fragment_ranking.*

class RankingFragment : Fragment() {
    private val rankingViewModel: RankingViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_ranking, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rankingList.layoutManager = LinearLayoutManager(requireContext())
        val adapter = RankingViewAdapter()
        rankingList.adapter = adapter
        rankingViewModel.users.observe(viewLifecycleOwner) {
            adapter.submitList(it)
            rankingList.visibility = View.VISIBLE
            progressBar.visibility = View.INVISIBLE
        }
    }



}