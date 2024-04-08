package com.example.wander.ui

import android.content.SharedPreferences
import android.util.Base64
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wander.data.NetworkAuthRepository
import com.example.wander.model.User
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

class LoginViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    private val authRepository = NetworkAuthRepository()
    private val _loginState = mutableStateOf(LoginState())
    val loginState: MutableState<LoginState> = _loginState

    private val encryptionKey = "Rabin12345678910"
    private val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

    init {
        isLoggedIn()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            if (username.isBlank() || password.isBlank()) {
                _loginState.value = LoginState(emptyFail = true)
            } else {
                try {
                    val encryptedUsername = encrypt(username)
                    val encryptedPassword = encrypt(password)
                    val response = authRepository.login(encryptedUsername, encryptedPassword)
                    if (response == null) {
                        _loginState.value = LoginState(loggedInFail = true)
                    } else {
                        val decryptedUser = decryptUser(response)
                        _loginState.value = LoginState(isLoggedIn = true, user = decryptedUser)
                        sharedPreferences.edit().putString("username", username)
                            .putString("password", password).apply()
                    }
                } catch (e: IOException) {
                    loginState.value = LoginState(error = e.message ?: "IOException")
                } catch (e: HttpException) {
                    loginState.value = LoginState(error = e.message ?: "HttpException")
                }
            }
        }
    }



    fun changePassword(username: String, password: String,newPassword:String) {
        viewModelScope.launch {
            _loginState.value = LoginState(newEmptyFail = false,emptyFail= false,changeFail = false)
            if (newPassword.isBlank()) {
                _loginState.value = LoginState(newEmptyFail = true)
            }else if (username.isBlank() || password.isBlank()) {
                _loginState.value = LoginState(emptyFail = true)
            } else {
                try {
                    val encryptedUsername = encrypt(username)
                    val encryptedPassword = encrypt(password)
                    val encryptedNewPassword = encrypt(newPassword)
                    val response = authRepository.changePassword(encryptedUsername, encryptedPassword,encryptedNewPassword)
                    if (response == null) {
                        _loginState.value = LoginState(changeFail = true)
                    } else {
                        val decryptedUser = decryptUser(response)
                        _loginState.value = LoginState(isChange = true, user = decryptedUser)
                        sharedPreferences.edit().putString("username", username)
                            .putString("password", newPassword).apply()
                    }
                } catch (e: IOException) {
                    loginState.value = LoginState(error = e.message ?: "IOException")
                } catch (e: HttpException) {
                    loginState.value = LoginState(error = e.message ?: "HttpException")
                }
            }
        }
    }
    fun create(username: String, password: String) {
        viewModelScope.launch {
            if (username.isBlank() || password.isBlank()) {
                _loginState.value = LoginState(emptyFail = true)
            } else {
                val encryptedUsername = encrypt(username)
                val encryptedPassword = encrypt(password)
                val user = User(userName = encryptedUsername, userPassword = encryptedPassword)
                if (authRepository.create(user).message == "用户插入成功。") {
                    _loginState.value = LoginState(
                        isLoggedIn = true, user = User(userName = username, userPassword = password)
                    )
                    sharedPreferences.edit().putString("username", username)
                        .putString("password", password).apply()
                } else {
                    _loginState.value = LoginState(createFail = true)
                }
            }
        }
    }

    fun logout() {
        // 清除 SharedPreferences 中存储的用户信息
        sharedPreferences.edit().clear().apply()
        _loginState.value = LoginState(isLoggedIn = false)
    }

    private fun isLoggedIn() {
        val username = sharedPreferences.getString("username", null)
        val password = sharedPreferences.getString("password", null)
        if (username != null && password != null) {
            login(username, password)
        }
    }


    private fun encrypt(input: String): String {
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(encryptionKey.toByteArray(), "AES"))
        val encryptedBytes = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    private fun decryptUser(user: User): User {
        val decryptedUsername = decrypt(user.userName)
        val decryptedPassword = decrypt(user.userPassword)
        return User(user.userID, decryptedUsername, decryptedPassword, user.vip, user.icon)
    }

    private fun decrypt(input: String): String {
        val decodedBytes = Base64.decode(input, Base64.NO_WRAP)
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(encryptionKey.toByteArray(), "AES"))
        return String(cipher.doFinal(decodedBytes), Charsets.UTF_8)
    }

    fun dismissFailDialog() {
        _loginState.value = LoginState()
    }



}

data class LoginState(
    val user: User? = null,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val loggedInFail: Boolean = false,
    val createFail: Boolean = false,
    val emptyFail: Boolean = false,
    val changeFail: Boolean = false,
    val isChange: Boolean = false,
    val newEmptyFail: Boolean = false,
)
