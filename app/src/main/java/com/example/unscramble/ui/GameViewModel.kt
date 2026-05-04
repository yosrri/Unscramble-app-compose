package com.example.unscramble.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameViewModel : ViewModel() {

    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()

    var userGuess by mutableStateOf("")
        private set
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState = _uiState.asStateFlow()


    init {
        resetGame()
    }

    fun pickRandomAndShuffle(): String {
        currentWord = allWords.random()

        return if (usedWords.contains(currentWord))
            pickRandomAndShuffle()
        else {
            usedWords.add(currentWord)
            shuffle(currentWord)
        }
    }

    private fun shuffle(word: String): String {
        val shuffledWord = word.toCharArray()
        shuffledWord.shuffle()
        while (shuffledWord.equals(currentWord)) {
            shuffledWord.shuffle()
        }
        return String(shuffledWord)
    }

    fun updateUserGuess(userGuessInput: String) {
        userGuess = userGuessInput
    }

    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            advanceToNextWord(uiState.value.score + SCORE_INCREASE)
        } else {
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong = true)
            }
        }

        updateUserGuess("")
    }

    fun advanceToNextWord(updatedScore: Int) {
        _uiState.update {
            it.copy(
                currentWordCount = it.currentWordCount.inc(),
                currentScrambledWord = pickRandomAndShuffle(),
                isGuessedWordWrong = false,
                score = updatedScore
            )
        }
    }



    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomAndShuffle())
    }

}