package com.yanbin.pdhome.ui.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import com.yanbin.pdhome.data.LoginRepository

import com.yanbin.pdhome.R
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.kotlin.addTo

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {

    val loginState: MutableLiveData<LoginUiState> = MutableLiveData<LoginUiState>()
    val loginProgress: MutableLiveData<LoginProgress> = MutableLiveData<LoginProgress>()

    private val disposables = CompositeDisposable()

    fun login(username: String, password: String) {
        loginProgress.postValue(LoginProgress.Loading)
        loginRepository.login(username, password)
            .subscribe( { user ->
                loginProgress.postValue(LoginProgress.Success(user.displayName))
            }, {
                loginProgress.postValue(LoginProgress.Failed(R.string.login_failed))
            })
            .addTo(disposables)
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            loginState.postValue(LoginUiState.UserNameError)
        } else if (!isPasswordValid(password)) {
            loginState.postValue(LoginUiState.PasswordError)
        } else {
            loginState.postValue(LoginUiState.Valid)
        }
    }

    // A placeholder username validation check
    val isUserNameValid = { username: String ->
        if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank() && !username.contains(" ")
        }
    }

    // A placeholder password validation check
    val isPasswordValid = { password: String ->
        password.length > 5
    }

    override fun onCleared() {
        disposables.clear()
    }
}