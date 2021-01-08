package com.example.kolkoikrzyzyk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kolkoikrzyzyk.model.User
import kotlinx.android.synthetic.main.result_card.view.*

class GamesHistoryViewAdapter() : RecyclerView.Adapter<GamesHistoryViewAdapter.ViewHolder>() {
    private var values: List<List<String>> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.result_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item[0], item[1], item[2])
    }

    override fun getItemCount(): Int = values.size

    fun submitList(list: List<List<String>>) {
        values = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        fun bind(name1: String, name2: String, result: String) {
            view.player1.text = name1
            view.player2.text = name2
            view.result.text = result
        }
    }

}