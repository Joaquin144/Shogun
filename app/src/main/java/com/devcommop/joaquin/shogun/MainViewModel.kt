package com.devcommop.joaquin.shogun

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainViewModel: ViewModel(){

    //All observables are lifecycle aware
    //Like liveData, sharedFlow, stateFlow we don't create a variable for flow rather we just return it inside emit block. Because flow doesn't holds a value.
    //FLow doesn't holds any value or state so if you rotate your screen your data would be gone
    //SharedFlow and StateFlow both are hot stream and are also one-time events. The difference is stateFlow emits again on screen rotation but sharedFlow won't emit again on Screen Rotation.

    //Use Cases: ---
    //LiveData : Mat use karo
    //FLow :  Koi cheez baar baar karni ho jaise stopwatch
    //SharedFlow : Sending Events to UI like navigating to new screen
    //StateFlow : To save a state
    private val _livedata = MutableLiveData("I am LiveData")
    val liveData: LiveData<String> = _livedata

    private val _stateFlow = MutableStateFlow("I am State FLow")
    val stateFLow: StateFlow<String> = _stateFlow.asStateFlow()//asStateFLow() --> makes it rad only

    private val _sharedFlow = MutableSharedFlow<String>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    fun triggerLiveData(){
        _livedata.value = "Ae dusht balak. Kyun pukara mujhe ?"
    }

    fun triggerStateFlow(){
        _stateFlow.value = "Beta yahi karte karte BTech complete ho jayegi. Rah jaogye berozgaar"
    }

    fun triggerSharedFlow(){
        viewModelScope.launch {
            _sharedFlow.emit("Oye bas kar. Nai Mnne tere chapet maar deni hai.")
        }
    }

    fun triggerFlow(): Flow<String>{
        return flow{
            repeat(30){
                emit("Item $it")
                delay(1500L)
            }
        }
    }
}