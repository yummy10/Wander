package com.example.wander.ui

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wander.data.NetworkAuthRepository
import com.example.wander.data.NetworkCitiesRepository
import com.example.wander.data.NetworkMessagesRepository
import com.example.wander.data.NetworkPlacesRepository
import com.example.wander.model.City
import com.example.wander.model.Comment
import com.example.wander.model.Place
import com.example.wander.model.UiState
import com.example.wander.model.User
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

class WViewModel : ViewModel() {

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

    init {
        clearPlaces()
        getCities()
    }

    fun updateUsername(user: User?) {
        _uiState.update {
            it.copy(
                user = user
            )
        }
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
                currentSelectedPlace = place, isShowingPlaceList = false
            )
        }
    }


    fun getSearchPlaces(name: String?, city: String?) {
        viewModelScope.launch {
            placenetsUiState = NetsUiState.Loading
            placenetsUiState = handleNetworkCall {
                val repository = NetworkPlacesRepository()
                _places.value = repository.getSearchPlaces(name, city)
            }
        }
    }

    fun clearPlaces() {
        _places.value = emptyList()
    }

    fun clearComments() {
        _comment.value = emptyList()
    }

    fun getCities() {
        viewModelScope.launch {
            netsUiState = NetsUiState.Loading
            netsUiState = handleNetworkCall {
                val repository = NetworkCitiesRepository()
                _cities.value = repository.getCities()
            }
        }
    }

    fun updateSearch(search: String): String {
        _uiState.update {
            it.copy(
                search = search,
            )
        }
        return search
    }

    fun setCity(city: City) {
        _uiState.update {
            it.copy(
                currentPlace = city.cityName,
                currentId = city.cityId,
                isShowingPlaceList = true,
            )
        }
    }

    fun addPlaceWithImage(context: Context, newPlace: Place, currentCityName: String, imageUri: Uri?) {
        viewModelScope.launch {
            handleNetworkCall {
                val repository = NetworkPlacesRepository()

                if (imageUri != null) {
                    repository.addPlaceWithImage(context,newPlace, currentCityName, imageUri)
                } else {
                    repository.addPlace(newPlace, currentCityName)
                }
                _uiState.update { it.copy(isAddPlace = true) }
            }
        }
    }
    fun addIcon(context: Context,name: String, imageUri: Uri?) {
        viewModelScope.launch {
            handleNetworkCall {
                val repository = NetworkAuthRepository()

                if (imageUri != null) {
                    repository.addIcon(context, name, imageUri)
                    _uiState.update { it.copy(isUploadingIconSuccess = true) }
                } else {
                    _uiState.update { it.copy(isUploadingIconFail = true) }
                }
            }
        }
    }
    fun dismissAddIcon() {
        _uiState.update { it.copy(isUploadingIconSuccess = false,isUploadingIconFail= false) }
    }
    fun getMessages() {
        viewModelScope.launch {
            messageBoardUiState = NetsUiState.Success("")
            messageBoardUiState = handleNetworkCall {
                val repository = NetworkMessagesRepository()
                _comment.value = repository.getMessages()
            }
        }
    }

    fun onLikeClicked(i: Int, c: Boolean) {
        viewModelScope.launch {
            try {
                val repository = NetworkMessagesRepository()
                if (c) {
                    repository.subLike(i)
                } else {
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
        newComment.userName = uiState.value.user?.userName ?: ""
        viewModelScope.launch {
            try {
                val repository = NetworkMessagesRepository()
                val isPlaceNameValid = repository.isPlaceNameValid(newComment.placeName)
                _uiState.update { it.copy(isPlaceNameValid = isPlaceNameValid) }
                if (isPlaceNameValid) {
                    repository.addMessage(newComment)
                    _uiState.update { it.copy(showSuccessDialog = true) }
                }
            } catch (e: IOException) {

            } catch (e: HttpException) {

            }

        }
    }
    fun addEmptyComment() {
        _uiState.update { it.copy(isPlaceNameValid = false) }
    }
    fun dismissAddEmptyComment() {
        _uiState.update { it.copy(isAddEmptyPlace = false) }
    }
    fun addEmptyPlace() {
        _uiState.update { it.copy(isAddEmptyPlace = true) }
    }
    fun dismissSuccessDialog() {
        _uiState.update { it.copy(showSuccessDialog = false) } // 隐藏提示框
    }
    fun dismissSuccessAddPlace() {
        _uiState.update { it.copy(isAddPlace = false) } // 隐藏提示框
    }
    fun getUserComments() {
        viewModelScope.launch {
            try {
                val repository = NetworkMessagesRepository()
                _comment.value = repository.showingComments(uiState.value.user?.userName ?: "")
                _uiState.update { it.copy(isShowingUserComments = true) }
            } catch (e: IOException) {
            } catch (e: HttpException) {
            }
        }
    }
    fun getPlaceComments(placeName:String){
        viewModelScope.launch {
            try {
                val repository = NetworkMessagesRepository()
                _comment.value = repository.showingPlaceComments(placeName)
                _uiState.update { it.copy(isCommentOK = true) }
            } catch (e: IOException) {
            } catch (e: HttpException) {
            }
        }
    }
    fun commentNotOK(){
        _uiState.update { it.copy(isCommentOK = false) }
    }

    fun initAccountScreen() {
        clearComments()
        _uiState.update { it.copy(isShowingUserComments = false) }
    }

    fun onBackPressed() {
        _uiState.update { it.copy(isShowingUserComments = false,isChangingPassword = false,isUploadingIcon = false) }
    }

    private suspend fun <T> handleNetworkCall(block: suspend () -> T): NetsUiState {
        return try {
            block()
            NetsUiState.Success("")
        } catch (e: IOException) {
            NetsUiState.Error
        } catch (e: HttpException) {
            NetsUiState.Error
        }
    }

    fun changePassword() {
        _uiState.update { it.copy(isChangingPassword = true) }
    }

    fun uploadIcon() {
        _uiState.update { it.copy(isUploadingIcon = true) }
    }

}
