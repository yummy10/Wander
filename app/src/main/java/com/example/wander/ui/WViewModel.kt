package com.example.wander.ui

import androidx.lifecycle.ViewModel
import com.example.wander.model.Name
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WViewModel: ViewModel() {
    private val _name = MutableStateFlow(Name())
    val name: StateFlow<Name> = _name.asStateFlow()

    fun updateUsername(uname: String){
        _name.value= Name(yourName=uname)
    }
    fun continuebutton(){

    }
}