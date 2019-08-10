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

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


/**
 * ViewModel containing all the logic needed to run the game
 */
class GameViewModel : ViewModel() {

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

    public fun onGameFinishComplete() { _eventGameFinished.value = false }

}

