package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameViewModel : ViewModel() {

    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()
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

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambledWord = pickRandomAndShuffle())
    }

}