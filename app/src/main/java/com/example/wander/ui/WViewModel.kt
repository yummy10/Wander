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


    fun updateUsername(uname: String) {
        _name.value = Name(yourName = uname)
    }

    init {
        clearPlaces()
        getCities()
    }


    fun resetHomeScreenStates() {
        _uiState.update {
            it.copy(
                isShowingPlaceList = true
            )
        }
    }

    fun detailsScreenStates(place: Place) {
        _uiState.update {
            it.copy(
                currentSelectedPlace = place,
                isShowingPlaceList = false
            )
        }
    }

    fun getPlaces() {
        viewModelScope.launch {
            netsUiState = NetsUiState.Loading
            netsUiState = try {
                val repository = NetworkPlacesRepository()
                _places.value = repository.getPlaces()
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
                val repository = NetworkPlacesRepository()
                _places.value = repository.getSearchPlaces(name, city)
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

    fun clearPlaces() {
        _places.value = emptyList()
    }

    fun getCities() {
        viewModelScope.launch {
            netsUiState = NetsUiState.Loading
            netsUiState = try {
                val repository = NetworkCitiesRepository()
                _cities.value = repository.getCities()
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

    fun updateSearch(search: String):String {
        _uiState.update {
            it.copy(
                search = search,
            )
        }
        return search
    }

    fun setCity(city: City){
        _uiState.update {
            it.copy(
                currentPlace = city.cityName,
                currentId = city.cityId,
                isShowingPlaceList = true,
            )
        }
    }

    fun addPlace(newPlace: Place) {
        viewModelScope.launch {
            netsUiState = NetsUiState.Loading
            netsUiState = try {
                    val repository = NetworkPlacesRepository()
                    repository.addPlace(newPlace)
                NetsUiState.Success(
                    "Success: "
                )
                } catch (e: IOException) {
                    NetsUiState.Error
                } catch (e: HttpException) {
                    NetsUiState.Error
                }

        }
    }
}