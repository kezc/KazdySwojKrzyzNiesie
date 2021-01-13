package com.example.kolkoikrzyzyk

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kolkoikrzyzyk.model.User
import kotlinx.android.synthetic.main.user_card.view.*


class UsersViewAdapter(val logoutCallback: (User)-> Unit) : RecyclerView.Adapter<UsersViewAdapter.ViewHolder>() {
    private var values: List<User> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.user_card,
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
            view.logout_button.setOnClickListener {
                logoutCallback(user)
            }
        }
    }
}