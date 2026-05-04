package com.example.unscramble.ui

data class GameUiState(
    val currentWordCount: Int = 0,
    val currentScrambledWord: String = "",
    val isGuessedWordWrong: Boolean = false,
    val score: Int = 0
)
