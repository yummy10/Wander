package com.example.wander.ui

import androidx.lifecycle.ViewModel
import com.example.wander.data.Datasource
import com.example.wander.model.City
import com.example.wander.model.Name
import com.example.wander.model.PlaceList
import com.example.wander.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WViewModel: ViewModel() {
    private val _name = MutableStateFlow(Name())
    private val _uiState = MutableStateFlow(UiState())
    val name: StateFlow<Name> = _name.asStateFlow()
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateUsername(uname: String){
        _name.value= Name(yourName=uname)
    }
    init {
        initializeUIState()
    }
    private fun initializeUIState() {
        val placeDate: Map<City,List<PlaceList>> =
             Datasource().loadPlaceLists().groupBy{it.city}
        _uiState.value=UiState(
            placeDate=placeDate,

        )
    }

    fun resetHomeScreenStates() {
        _uiState.update {
            it.copy(
                isShowingPlaceList = true
            )
        }
    }

    fun detailsScreenStates(placeList: PlaceList) {
        _uiState.update {
            it.copy(
                currentSelectedPlace = placeList,
                isShowingPlaceList = false
            )
        }
    }

}