package com.example.wander.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wander.data.NetworkCitiesRepository
import com.example.wander.data.NetworkMessagesRepository
import com.example.wander.data.NetworkPlacesRepository
import com.example.wander.model.City
import com.example.wander.model.Comment
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
    data class Success(val a: String) : NetsUiState
    object Error : NetsUiState
    object Loading : NetsUiState
}

class WViewModel() : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    var netsUiState: NetsUiState by mutableStateOf(NetsUiState.Loading)
        private set
    var placenetsUiState: NetsUiState by mutableStateOf(NetsUiState.Loading)
        private set
    var messageBoardUiState: NetsUiState by mutableStateOf(NetsUiState.Loading)
        private set

    private val _cities = MutableStateFlow<List<City>>(emptyList())
    val cities: StateFlow<List<City>> = _cities
    private val _places = MutableStateFlow<List<Place>>(emptyList())
    val places: StateFlow<List<Place>> = _places
    private val _comment = MutableStateFlow<List<Comment>>(emptyList())
    val comment: StateFlow<List<Comment>> = _comment


    fun updateUsername(uname: String) {
        _uiState.update {
            it.copy(
                userName = uname
            )
        }
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

//    fun getPlaces() {
//        viewModelScope.launch {
//            netsUiState = NetsUiState.Loading
//            netsUiState = try {
//                val repository = NetworkPlacesRepository()
//                _places.value = repository.getPlaces()
//                NetsUiState.Success(
//                    "Success:  "
//                )
//            } catch (e: IOException) {
//                NetsUiState.Error
//            } catch (e: HttpException) {
//                NetsUiState.Error
//            }
//        }
//    }

    fun getSearchPlaces(name: String?, city: String?) {
        viewModelScope.launch {
            placenetsUiState = NetsUiState.Loading
            placenetsUiState = try {
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

    fun addPlace(newPlace: Place,placeName:String) {
        viewModelScope.launch {
            try { val repository = NetworkPlacesRepository()
                repository.addPlace(newPlace,placeName)
                } catch (e: IOException) {
                } catch (e: HttpException) {
                }

        }
    }

    fun getMessages() {
        viewModelScope.launch {
            messageBoardUiState = NetsUiState.Success("")
            messageBoardUiState = try {
                val repository = NetworkMessagesRepository()
                _comment.value = repository.getMessages()
                NetsUiState.Success(
                    "Success:"
                )
            } catch (e: IOException) {
                NetsUiState.Error
            } catch (e: HttpException) {
                NetsUiState.Error
            }
        }
    }

    fun onLikeClicked(i: Int,c:Boolean) {
        viewModelScope.launch {
            try {
                val repository = NetworkMessagesRepository()
                if(c){
                    repository.subLike(i)
                }else{
                    repository.addLike(i)
                }
                getMessages()
            } catch (e: IOException) {
                NetsUiState.Error
            } catch (e: HttpException) {
                NetsUiState.Error
            }
        }
   }

    fun addComment(newComment: Comment) {
        viewModelScope.launch {
            try {
                val repository = NetworkMessagesRepository()
                repository.addMessage(newComment)
            } catch (e: IOException) {

            } catch (e: HttpException) {

            }

        }
    }

}