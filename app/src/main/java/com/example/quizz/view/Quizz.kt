package com.example.quizz.view

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.quizz.BuildConfig
import com.example.quizz.R
import com.example.quizz.databinding.QuizzBinding
import com.example.quizz.model.Question
import com.example.quizz.model.User
import com.example.quizz.viewmodel.QuizzViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class Quizz : AppCompatActivity(),HeaderActions {

    private lateinit var binding: QuizzBinding
    private val quizzViewModel: QuizzViewModel by viewModel()
    private var selectedAnswer: String? = null
    private var currentUser:User? = null

    private var lastScore = 0
    private var lastQuestionsAnswered = 0

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = QuizzBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initData()

        quizzViewModel.user.observe(this) { user ->
            updateUserInfoDisplay(user)
        }

        quizzViewModel.currentQuestion.observe(this) { question ->
            displayQuestion(question)
        }
        binding.btnSubmit.setOnClickListener {
            onSubmitClicked()
        }
        quizzViewModel.quizCompleted.observe(this) { isCompleted ->
            if (isCompleted) showQuizCompletionDialog()
        }

        quizzViewModel.scoreLiveData.observe(this) { score ->
            lastScore = score
            updateScoreDisplay()
        }

        quizzViewModel.questionsAnsweredLiveData.observe(this) { questionsAnswered ->
            lastQuestionsAnswered = questionsAnswered
            updateScoreDisplay()
        }
        setupResponseSubmissionObservers()

    }
    private fun initData() {
        val userId = intent.getIntExtra("USER_ID", -1)
        if (userId != -1) {
            quizzViewModel.fetchUserDetails(userId)
        }
        quizzViewModel.fetchQuestions(BuildConfig.QUIZZ_API_KEY, 10)
    }

    private fun displayQuestion(question: Question?) {
        binding.tvQuestion.text = question?.question
        binding.llAnswers.removeAllViews()

        question?.answers?.let { answers ->
            val answersList = listOfNotNull(answers.answer_a, answers.answer_b, answers.answer_c, answers.answer_d, answers.answer_e, answers.answer_f)
            answersList.forEach { answerText ->

                val button = Button(this).apply {
                    text = answerText
                    setBackgroundResource(R.drawable.quizz_button)
                    setTextColor(ContextCompat.getColor(context, R.color.button_text_color))
                    setOnClickListener {
                        selectedAnswer = this.text.toString()
                        highlightSelectedButton(this)
                    }
                    val params = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )
                    params.setMargins(16, 16, 16, 16)
                    layoutParams = params
                }
                binding.llAnswers.addView(button)
            }
        }
    }

    private fun highlightSelectedButton(selectedButton: Button) {
        for (i in 0 until binding.llAnswers.childCount) {
            val button = binding.llAnswers.getChildAt(i) as Button
            if (button == selectedButton) {
                button.setBackgroundResource(R.drawable.quizz_button_selected)
                button.setTextColor(ContextCompat.getColor(this, R.color.lavender))
            } else {
                button.setBackgroundResource(R.drawable.quizz_button)
                button.setTextColor(ContextCompat.getColor(this, R.color.button_text_color))
            }
        }
    }
    private fun onSubmitClicked() {
        selectedAnswer?.let { answer ->
            quizzViewModel.submitAnswer(answer)
            selectedAnswer = null
        } ?: run {
            Toast.makeText(this, getString(R.string.prompt_select_answer), Toast.LENGTH_SHORT).show()
        }
    }
    private fun setupResponseSubmissionObservers() {
        quizzViewModel.hasAnswerBeenSubmitted.observe(this) { hasSubmitted ->
            if (hasSubmitted) {
                binding.header.btnLogOut.setOnClickListener {
                    Toast.makeText(this, getString(R.string.cannot_logout_after_submit), Toast.LENGTH_SHORT).show()
                }

                binding.header.leaderbord.setOnClickListener {
                    Toast.makeText(this, getString(R.string.cannot_access_leaderboard_after_submit), Toast.LENGTH_SHORT).show()
                }
            } else {
                binding.header.btnLogOut.setOnClickListener { logOut() }
                binding.header.leaderbord.setOnClickListener { leaderboard() }
            }
        }
    }


    private fun updateScoreDisplay() {
        binding.tvScore.text = getString(R.string.score_text, lastScore, lastQuestionsAnswered)
    }
    private fun showQuizCompletionDialog() {
        val score = quizzViewModel.scoreLiveData.value ?: 0
        val message = getString(R.string.quiz_completed_message, score)
        val builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.app_name))
            .setMessage(message)
            .setPositiveButton(R.string.replay) { _, _ ->
                quizzViewModel.resetQuiz()
                quizzViewModel.fetchQuestions(BuildConfig.QUIZZ_API_KEY, 10)
            }
            .setNeutralButton(R.string.leaderboard) { _, _ ->
                leaderboard()
            }
            .setNegativeButton(R.string.exit) { _, _ ->
                finish()
            }
            .setCancelable(false)
        val dialog = builder.create()
        dialog.show()
    }


    override fun updateUserInfoDisplay(user: User?) {
        this.currentUser = user
        user?.let {
            binding.header.tvUsername.text = user.username
            binding.header.progressBarUser.progress = user.barProgress
            binding.header.tvLevel.text = getString(R.string.level_prefix, user.level)
        }
    }

    override fun logOut() {
        AlertDialog.Builder(this).apply {
            setMessage(getString(R.string.logout_confirmation))

            setPositiveButton(R.string.yes) { _, _ ->
                val intent = Intent(this@Quizz, Login::class.java)
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
        val intent = Intent(this, Leaderboard::class.java).apply {
            putExtra("USER_ID", currentUser?.id)
            putExtra("USERNAME", currentUser?.username)
            putExtra("PROGRESS_BAR", currentUser?.barProgress)
            putExtra("LEVEL", currentUser?.level)
        }
        startActivity(intent)
        finish()
    }

    override fun quizz() {
        TODO("Not yet implemented")
    }

}


