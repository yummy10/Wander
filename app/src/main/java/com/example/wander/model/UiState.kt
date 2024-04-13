package com.example.wander.model

data class UiState(
    val isShowingPlaceList: Boolean = true,
    val currentPlace: String = "",
    val currentId: Int = -1,
    val currentSelectedPlace: Place = Place(-1, "", "", "", -1, "", ""),
    val search: String = "",
    val user: User? = null,
    val isPlaceNameValid: Boolean = true,
    val showSuccessDialog: Boolean = false,
    val isShowingUserComments: Boolean = false,
    val isChangingPassword: Boolean = false,
    val isAddPlace: Boolean = false,
    val isAddEmptyPlace: Boolean = false,
    val isUploadingIcon: Boolean = false,
    val isUploadingIconSuccess: Boolean = false,
    val isUploadingIconFail: Boolean = false,
    val commentPlace: String = "",
)