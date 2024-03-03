package com.example.quizz.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.quizz.R
import com.example.quizz.databinding.LeaderboardBinding
import com.example.quizz.model.User
import com.example.quizz.repositories.UserRepository
import com.example.quizz.view.adapters.LeaderboardAdapter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class Leaderboard : AppCompatActivity(),HeaderActions {
    private lateinit var binding: LeaderboardBinding
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    private val userRepository: UserRepository by inject()
    private var userId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LeaderboardBinding.inflate(layoutInflater)
        setContentView(binding.root)


        userId = intent.getIntExtra("USER_ID", -1)
        val username = intent.getStringExtra("USERNAME") ?: "Unknown"
        val level = intent.getIntExtra("LEVEL", -1)
        val progressBar = intent.getIntExtra("PROGRESS_BAR", 0)


        binding.header.tvUsername.text = username
        binding.header.progressBarUser.progress = progressBar
        binding.header.tvLevel.text = getString(R.string.level_prefix, level)

        setupLeaderboardRecyclerView()
        fetchPlayersAndSetupRecyclerView()

        binding.header.quizz.setOnClickListener{
            quizz()
        }
        binding.header.btnLogOut.setOnClickListener{
            logOut()
        }
    }

    private fun fetchPlayersAndSetupRecyclerView() {
        lifecycleScope.launch {
            val fetchedPlayers = userRepository.getAllUsersOrderedByLevelAndProgress()
            leaderboardAdapter.updatePlayers(fetchedPlayers)
        }
    }

    private fun setupLeaderboardRecyclerView() {
        leaderboardAdapter = LeaderboardAdapter(this,mutableListOf())
        binding.leaderboardRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.leaderboardRecyclerView.adapter = leaderboardAdapter
    }
    override fun updateUserInfoDisplay(user: User?) {
        TODO("Not yet implemented")
    }

    override fun logOut() {
        AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.logout_confirmation))

            setPositiveButton(R.string.yes) { _, _ ->
                val intent = Intent(this@Leaderboard, Login::class.java)
                startActivity(intent)
                finish()
            }
            setNegativeButton(R.string.no) { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }

    override fun leaderboard() {
        TODO("Not yet implemented")
    }

    override fun quizz() {
        val intent = Intent(this, Quizz::class.java)
        intent.putExtra("USER_ID", userId)
        startActivity(intent)
        finish()
    }
}
