package com.yanbin.pdhome.ui.login

sealed class LoginUiState {
    object Valid : LoginUiState()
    object UserNameError : LoginUiState()
    object PasswordError : LoginUiState()
}

sealed class LoginProgress {
    object Loading : LoginProgress()
    class Success(val displayName: String): LoginProgress()
    class Failed(val errorMsg: Int): LoginProgress()
}