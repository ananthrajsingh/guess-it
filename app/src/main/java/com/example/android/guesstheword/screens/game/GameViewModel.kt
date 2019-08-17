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
import android.view.accessibility.AccessibilityNodeInfo
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel


/**
 * ViewModel containing all the logic needed to run the game
 */
class GameViewModel : ViewModel() {

    companion object {
        const val DONE = 0L
        const val ONE_SECOND = 1000L
        const val COUNTDOWN_TIME = 10000L
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


    init {
        _currentTime.value = 0
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onFinish() {
                _eventGameFinish.value = true
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
        _score.value = (score.value)?.plus(1)

        nextWord()

    override fun onCleared() {
        super.onCleared()
        timer.cancel()
    }
    public fun onGameFinishComplete() { _eventGameFinished.value = false }
}

