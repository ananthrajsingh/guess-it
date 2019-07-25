/*
 * Copyright 2018, The Android Open Source Project
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

package com.example.android.guesstheword.screens.score

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.guesstheword.screens.game.GameViewModel

// TODO (01) Create the ScoreViewModel class and have it take in an integer constructor parameter
// called finalScore
// TODO (06) Add a LiveData for the score and the play again event, using the best practices for
// encapsulation and event handling that you've learned


class ScoreViewModel(val finalScore: Int): ViewModel() {


    private val _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    private val _eventPlayAgain = MutableLiveData<Boolean>()
    val eventPlayAgain: LiveData<Boolean>
        get() = _eventPlayAgain

    init {
        Log.i("ScoreViewModel", "Final score: $finalScore")
        _score.value = finalScore
        _eventPlayAgain.value = false
    }

    fun onPlayAgainClicked() {
        _eventPlayAgain.value = true
    }
    fun onPlayAgainFinished() {
        _eventPlayAgain.value = false
    }
}