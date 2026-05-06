package com.example.unscramble.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue

import org.junit.Test

class GameViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val correctPlayerWord = getUnscrambledWord(currentGameUiState.currentScrambledWord)

        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value

        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER, currentGameUiState.score)
    }

    @Test
    fun gameViewModel_WrongWordGuess_ScoreUnchangedAndErrorFlagSet() {
        var currentGameUiState = viewModel.uiState.value
        val wrongUserGuess = "and"
        val currentScore = 0

        viewModel.updateUserGuess(wrongUserGuess)
        viewModel.checkUserGuess()
        currentGameUiState = viewModel.uiState.value

        assertEquals(currentScore, currentGameUiState.score)
        assertTrue(currentGameUiState.isGuessedWordWrong)

    }

    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        viewModel.resetGame()
        val currentGamUiState = viewModel.uiState.value
        val unscrambledWord = getUnscrambledWord(currentGamUiState.currentScrambledWord)


        assertFalse(currentGamUiState.isGuessedWordWrong)
        assertFalse(currentGamUiState.isGameOver)
        assertTrue(currentGamUiState.currentWordCount == 1)
        assertNotEquals(unscrambledWord, currentGamUiState.currentScrambledWord)
        assertEquals(0, currentGamUiState.score)
    }

    @Test
    fun gameViewModel_AllWordsGuessedCorrectly_UiStateUpdatedCorrectly() {
        var currentGameUiState = viewModel.uiState.value
        var currentExpectedScore = 0

        repeat(MAX_NO_OF_WORDS) { iteration ->
            assertEquals(iteration + 1, currentGameUiState.currentWordCount)

            val correctGuess = getUnscrambledWord(currentGameUiState.currentScrambledWord)
            currentExpectedScore += SCORE_AFTER_FIRST_CORRECT_ANSWER

            viewModel.updateUserGuess(correctGuess)
            viewModel.checkUserGuess()
            currentGameUiState = viewModel.uiState.value
            assertEquals(currentExpectedScore, currentGameUiState.score)
        }
        assertEquals(MAX_NO_OF_WORDS, currentGameUiState.currentWordCount)
        assertTrue(currentGameUiState.isGameOver)
    }

    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}