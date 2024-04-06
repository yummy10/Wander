package com.example.wander.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PhotoViewModel : ViewModel() {
    // LiveData 用于保存选中的图片 URI
    private val _selectedImageUri = MutableLiveData<Uri>()
    val selectedImageUri: LiveData<Uri> = _selectedImageUri

    fun handleSelectedImage(uri: Uri?) {
        // 使用新的 URI 更新 LiveData
        uri?.let {
            // 如果 URI 不为空，更新 LiveData
            _selectedImageUri.value = it
        }
    }
    fun getSelectedImageUri(): Uri? {
        return _selectedImageUri.value
    }

}