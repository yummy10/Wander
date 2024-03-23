package com.example.wander.ui

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wander.data.NetworkAuthRepository
import com.example.wander.model.User
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class LoginViewModel: ViewModel() {
    private val authRepository= NetworkAuthRepository()
    private val _loginState = mutableStateOf(LoginState())
    val loginState: MutableState<LoginState> = _loginState

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                val user = authRepository.login(username, password)
                _loginState.value = LoginState(isLoggedIn = user != null,loggedInFail = user == null,user=user)
            } catch (e: IOException) {
                loginState.value = LoginState(error = e.message ?: "IOException")
            } catch (e: HttpException) {
                loginState.value = LoginState(error = e.message ?: "HttpException")
            }
        }
    }
    fun create(user: User){
        viewModelScope.launch {
            if(authRepository.create(user)=="用户插入成功。"){
                _loginState.value = LoginState(isLoggedIn = true,user=user)
            }else{
                _loginState.value = LoginState(createFail = true)
            }
        }
    }

    fun dismissFailDialog() {
        _loginState.value =LoginState(loggedInFail=false)
    }
}
data class LoginState(
    val user: User? = null,
    val isLoggedIn: Boolean = false,
    val error: String? = null,
    val loggedInFail:Boolean=false,
    val createFail:Boolean=false
)
