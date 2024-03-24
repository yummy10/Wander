package com.example.wander.ui

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
class LoginViewModel : ViewModel() {
    private val authRepository = NetworkAuthRepository()
    private val _loginState = mutableStateOf(LoginState())
    val loginState: MutableState<LoginState> = _loginState

    private val encryptionKey = "Rabin12345678910"
    private val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")

    fun login(username: String, password: String) {
        viewModelScope.launch {
            if (username == "" || password == "") {
                _loginState.value = LoginState(emptyFail = true)
            } else {
                try {
                    val encryptedUsername = encrypt(username)
                    val encryptedPassword = encrypt(password)
                    val user = authRepository.login(encryptedUsername, encryptedPassword)
                    _loginState.value = LoginState(isLoggedIn = user != null, loggedInFail = user == null, user = user)
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
            if (username == "" || password == "") {
                _loginState.value = LoginState(emptyFail = true)
            } else {
                val encryptedUsername = encrypt(username)
                val encryptedPassword = encrypt(password)
                val user = User(userName = encryptedUsername, userPassword = encryptedPassword)
                if (authRepository.create(user).message == "用户插入成功。") {
                    _loginState.value = LoginState(isLoggedIn = true, user = user)
                } else {
                    _loginState.value = LoginState(createFail = true)
                }
            }
        }
    }

    private fun encrypt(input: String): String {
        cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(encryptionKey.toByteArray(), "AES"))
        val encryptedBytes = cipher.doFinal(input.toByteArray(Charsets.UTF_8))
        return Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
    }

    private fun decrypt(input: String): String {
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(encryptionKey.toByteArray(), "AES"))
        return String(cipher.doFinal(input.toByteArray(Charsets.UTF_8)))
    }

    fun dismissFailDialog() {
        _loginState.value = LoginState(loggedInFail = false, createFail = false)
    }
}
data class LoginState(
    val user: User? = null,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val loggedInFail:Boolean=false,
    val createFail:Boolean=false,
    val emptyFail:Boolean=false,
)
