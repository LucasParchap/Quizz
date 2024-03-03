package com.example.quizz.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quizz.R
import com.example.quizz.model.User


class LeaderboardAdapter(private val context: Context, private var users: List<User>) : RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvUsername: TextView = view.findViewById(R.id.tvUsername)
        val tvLevel: TextView = view.findViewById(R.id.tvLevel)
        val progressBar: ProgressBar = view.findViewById(R.id.progressBar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.tvUsername.text = user.username
        holder.tvLevel.text = context.getString(R.string.level_prefix, user.level)
        holder.progressBar.progress = user.barProgress
    }
    fun updatePlayers(newPlayers: List<User>) {
        this.users = newPlayers
        notifyDataSetChanged()
    }
    override fun getItemCount() = users.size
}