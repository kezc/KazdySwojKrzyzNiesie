package com.example.kolkoikrzyzyk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kolkoikrzyzyk.model.User
import kotlinx.android.synthetic.main.ranking_card.view.*

class RankingViewAdapter : RecyclerView.Adapter<RankingViewAdapter.ViewHolder>() {
    private var values: List<User> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.ranking_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = values.size

    fun submitList(list: List<User>) {
        values = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View) :
        RecyclerView.ViewHolder(view) {
        fun bind(user: User) {
            view.name.text = user.name
            view.player2.text = user.wonGames.toString()
            view.draws.text = user.drawnGames.toString()
            view.loses.text = user.lostGames.toString()
        }
    }

}