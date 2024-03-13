package com.example.wander.ui

import androidx.lifecycle.ViewModel
import com.example.wander.data.Datasource
import com.example.wander.model.City
import com.example.wander.model.Name
import com.example.wander.model.PlaceList
import com.example.wander.model.Search
import com.example.wander.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WViewModel() : ViewModel() {
    private val _search = MutableStateFlow(Search())
    private val _name = MutableStateFlow(Name())
    private val _uiState = MutableStateFlow(UiState())
    val search: StateFlow<Search> = _search.asStateFlow()
    val name: StateFlow<Name> = _name.asStateFlow()
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun updateUsername(uname: String){
        _name.value= Name(yourName=uname)
    }

    init {
        val datasource = Datasource()
        initializeUIState(datasource)
    }

    private fun initializeUIState(datasource: Datasource) {
        val placeDate: Map<City,List<PlaceList>> =
            datasource.loadPlaceLists().groupBy { it.city }
        _uiState.value = UiState(
            placeDate = placeDate
        )
        _search.value = Search(
            allList = datasource.loadPlaceLists()
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

    fun setCity(city: City){
        _uiState.update {
            it.copy(
                currentPlace = city,
                isShowingPlaceList = true,
            )
        }
    }

    fun updateSearchQuery(query: String) {
        _search.update { currentSearch ->
            val searchResults = if (query.isBlank()) {
                currentSearch.allList
            } else {
                currentSearch.allList.filter {
                    it.stringResourceId.toString().contains(query, ignoreCase = true)
                }
            }
            currentSearch.copy(searchQuery = query, searchResults = searchResults)
        }
    }
    fun onPlaceSelected(placeList: PlaceList) {
        _uiState.update {
            it.copy(
                currentSelectedPlace = placeList,
                isShowingPlaceList = false
            )
        }
    }

}