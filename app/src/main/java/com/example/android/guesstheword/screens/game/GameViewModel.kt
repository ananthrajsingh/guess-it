/*
 * Copyright 2018, The   Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import android.widget.Toast
import android.view.accessibility.AccessibilityNodeInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
private val CORRECT_BUZZ_PATTERN = longArrayOf(100, 100, 100, 100, 100, 100)
private val PANIC_BUZZ_PATTERN = longArrayOf(0, 200)
private val GAME_OVER_BUZZ_PATTERN = longArrayOf(0, 2000)
private val NO_BUZZ_PATTERN = longArrayOf(0)


/**
 * ViewModel containing all the logic needed to run the game
 */
class GameViewModel : ViewModel() {

    enum class BuzzType(val pattern: LongArray) {
        CORRECT(CORRECT_BUZZ_PATTERN),
        GAME_OVER(GAME_OVER_BUZZ_PATTERN),
        COUNTDOWN_PANIC(PANIC_BUZZ_PATTERN),
        NO_BUZZ(NO_BUZZ_PATTERN)
    }

    companion object {
        // These represent different important times in the game, such as game length.

        // This is when the game is over
        private const val DONE = 0L

        // This is the number of milliseconds in a second
        private const val ONE_SECOND = 1000L

        // This is the total time of the game
        private const val COUNTDOWN_TIME = 60000L

        private const val COUNTDOWN_BUZZ_TIME = 50000L

    }

    var timer: CountDownTimer

    private val _currentTime = MutableLiveData<Int>()
    val currentTime: LiveData<Int>
        get() = _currentTime
    val currentTimeString = Transformations.map(currentTime) {time ->
        DateUtils.formatElapsedTime(time)
    }
    // The current word
    private val _word = MutableLiveData<String>()
    val word: LiveData<String>
            get() = _word


    // The current score
    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    // boolean
    private val _eventGameFinished = MutableLiveData<Boolean>()
    val eventGameFinished: LiveData<Boolean>
        get() = _eventGameFinished

    private val _eventBuzz = MutableLiveData<BuzzType>()
    val eventBuzz: LiveData<BuzzType>
        get() = _eventBuzz

    init {
        resetList()
        nextWord()
        _score.value = 0

        // COMPLETED (06) Set the value of buzz event to the correct BuzzType when the buzzer should
        // fire. This should happen when the game is over, when the user gets a correct answer,
        // and on each tick when countdown buzzing starts

        // Creates a timer which triggers the end of the game when it finishes
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = (millisUntilFinished / ONE_SECOND)
                if (_currentTime.value == COUNTDOWN_BUZZ_TIME) {
                    _eventBuzz.value = BuzzType.COUNTDOWN_PANIC
                }
            }

            override fun onFinish() {
                _eventGameFinish.value = true
                _eventBuzz.value = BuzzType.GAME_OVER
            }

            override fun onTick(millisUntilFinished: Long) {
                _currentTime.value = _currentTime.value?.plus(1)
            }
        }
        timer.start()
        resetList()
        nextWord()
        _score.value = 0
    }


    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
            // gameFinished() should happen here
            _eventGameFinished.value = true
        } else {
            _word.value = wordList.removeAt(0)
        }
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _score.value = (_score.value)?.plus(1)
        _eventBuzz.value = BuzzType.CORRECT
        nextWord()
    }

    /** Methods for completed events **/

    fun onGameFinishComplete() {
        _eventGameFinish.value = false
    }


    fun onBuzzComplete() {
        _eventBuzz.value = BuzzType.NO_BUZZ
    }
    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
    public fun onGameFinishComplete() { _eventGameFinished.value = false }
}

