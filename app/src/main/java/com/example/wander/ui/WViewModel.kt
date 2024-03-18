package com.example.wander.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wander.data.NetworkCitiesRepository
import com.example.wander.data.NetworkPlacesRepository
import com.example.wander.model.City
import com.example.wander.model.Name
import com.example.wander.model.Place
import com.example.wander.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

sealed interface NetsUiState {
    data class Success(val photos: String) : NetsUiState
    object Error : NetsUiState
    object Loading : NetsUiState
}

class WViewModel() : ViewModel() {
    private val _name = MutableStateFlow(Name())
    private val _uiState = MutableStateFlow(UiState())
    val name: StateFlow<Name> = _name.asStateFlow()
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    var netsUiState: NetsUiState by mutableStateOf(NetsUiState.Loading)
        private set

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities
    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places


    fun updateUsername(uname: String){
        _name.value= Name(yourName=uname)
    }

    init {
        getPlaces()
        getCities()
    }




    fun resetHomeScreenStates() {
        _uiState.update {
            it.copy(
                isShowingPlaceList = true
            )
        }
    }

    fun detailsScreenStates(placeList: Place) {
        _uiState.update {
            it.copy(
                currentSelectedPlace = placeList,
                isShowingPlaceList = false
            )
        }
    }

    fun setCity(cityname: String){
        _uiState.update {
            it.copy(
                currentPlace = cityname,
                isShowingPlaceList = true,
            )
        }
    }

//    fun updateSearchQuery(query: String) {
//        _search.update { currentSearch ->
//            val searchResults = if (query.isBlank()) {
//                currentSearch.allList
//            } else {
//                currentSearch.allList.filter {
//                    it.stringResourceId.toString().contains(query, ignoreCase = true)
//                }
//            }
//            currentSearch.copy(searchQuery = query, searchResults = searchResults)
//        }
//    }
//    fun onPlaceSelected(placeList: Place) {
//        _uiState.update {
//            it.copy(
//                currentSelectedPlace = placeList,
//                isShowingPlaceList = false
//            )
//        }
//    }
    fun getPlaces() {
        viewModelScope.launch {
            netsUiState = NetsUiState.Loading
            netsUiState = try {
                val repository  = NetworkPlacesRepository()
                _places.value = repository .getPlaces()
                NetsUiState.Success(
                    "Success:  "
                )
            } catch (e: IOException) {
                NetsUiState.Error
            } catch (e: HttpException) {
                NetsUiState.Error
            }
        }
    }

    fun getSearchPlaces(name: String?, city: String?) {
        viewModelScope.launch {
            netsUiState = NetsUiState.Loading
            netsUiState = try {
                val repository  = NetworkPlacesRepository()
                _places.value = repository .getSearchPlaces(name, city)
                NetsUiState.Success(
                    "Success:  "
                )
            } catch (e: IOException) {
                NetsUiState.Error
            } catch (e: HttpException) {
                NetsUiState.Error
            }
        }
    }


    fun getCities() {
        viewModelScope.launch {
            netsUiState = NetsUiState.Loading
            netsUiState = try {
                val repository  = NetworkCitiesRepository()
                _cities.value = repository .getCities()
                NetsUiState.Success(
                    "Success:  "
                )
            } catch (e: IOException) {
                NetsUiState.Error
            } catch (e: HttpException) {
                NetsUiState.Error
            }
        }
    }


}