package com.example.quizz.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quizz.model.Question
import com.example.quizz.model.User
import com.example.quizz.repositories.QuizzRepository
import kotlinx.coroutines.launch

class QuizzViewModel(private val repository: QuizzRepository) : ViewModel() {

    private val _questions = MutableLiveData<List<Question>>()
    private var currentQuestionIndex = 0

    private val _currentQuestion = MutableLiveData<Question?>()
    val currentQuestion: LiveData<Question?> = _currentQuestion

    private val _quizCompleted = MutableLiveData<Boolean>()
    val quizCompleted: LiveData<Boolean> = _quizCompleted

    private var score = 0

    private val _scoreLiveData = MutableLiveData<Int>()
    val scoreLiveData: LiveData<Int> = _scoreLiveData

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> = _user

    private var questionsAnswered = 0

    private val _questionsAnsweredLiveData = MutableLiveData<Int>()
    val questionsAnsweredLiveData: LiveData<Int> = _questionsAnsweredLiveData

    private val _hasAnswerBeenSubmitted = MutableLiveData<Boolean>(false)
    val hasAnswerBeenSubmitted: LiveData<Boolean> = _hasAnswerBeenSubmitted

    fun fetchQuestions(apiKey: String, limit: Int = 10) {
        viewModelScope.launch {
            val fetchedQuestions = repository.getQuestions(apiKey, limit) ?: emptyList()
            if (fetchedQuestions.isNotEmpty()) {
                _questions.value = fetchedQuestions
                currentQuestionIndex = 0
                _currentQuestion.value = fetchedQuestions[currentQuestionIndex]
                score = 0
                questionsAnswered = 0
            }
        }
    }

    fun submitAnswer(selectedAnswer: String) {
        _hasAnswerBeenSubmitted.postValue(true)
        _questions.value?.getOrNull(currentQuestionIndex)?.let { question ->
            if (isAnswerCorrect(question, selectedAnswer)) {
                score++
                _scoreLiveData.postValue(score)
            }
            questionsAnswered++
            _questionsAnsweredLiveData.postValue(questionsAnswered)

            if (currentQuestionIndex < (_questions.value?.size ?: 0) - 1) {
                currentQuestionIndex++
                _currentQuestion.value = _questions.value?.get(currentQuestionIndex)
            } else {
                finalizeQuiz()
                _quizCompleted.postValue(true)
            }
        }

    }

    private fun isAnswerCorrect(question: Question, selectedAnswer: String): Boolean {
        return when (selectedAnswer) {

            question.answers.answer_a -> question.correct_answers.answer_a_correct.toBoolean()
            question.answers.answer_b -> question.correct_answers.answer_b_correct.toBoolean()
            question.answers.answer_c -> question.correct_answers.answer_c_correct.toBoolean()
            question.answers.answer_d -> question.correct_answers.answer_d_correct.toBoolean()
            question.answers.answer_e -> question.correct_answers.answer_e_correct.toBoolean()
            question.answers.answer_f -> question.correct_answers.answer_f_correct.toBoolean()
            else -> false
        }
    }

    fun resetQuiz() {
        score = 0
        _scoreLiveData.value = score
        currentQuestionIndex = 0
        questionsAnswered = 0
        _questionsAnsweredLiveData.value = questionsAnswered
        _quizCompleted.value = false
        _hasAnswerBeenSubmitted.postValue(false)
    }

    fun fetchUserDetails(userId: Int) {
        viewModelScope.launch {
            val userDetails = repository.getUserByID(userId)
            _user.value = userDetails
        }
    }

    private fun finalizeQuiz() {
        viewModelScope.launch {
            val user = _user.value ?: return@launch
            val pointsToAdd = if (score >= 5) {
                (score - 5) * 10
            } else {
                (5 - score) * -10
            }
            var newBarProgress = user.barProgress + pointsToAdd
            var newLevel = user.level

            if (newBarProgress >= 100) {
                newLevel++
                newBarProgress -= 100
            } else if (newBarProgress < 0) {
                if (newLevel > 1) {
                    newLevel--
                    newBarProgress += 100
                } else {
                    newBarProgress = 0
                }
            }

            val updatedUser = user.copy(barProgress = newBarProgress, level = newLevel)

            repository.updateUser(updatedUser)

            _user.postValue(updatedUser)
            _hasAnswerBeenSubmitted.postValue(false)
        }
    }
}


