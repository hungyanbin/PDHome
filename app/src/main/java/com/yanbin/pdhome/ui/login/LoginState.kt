package com.yanbin.pdhome.ui.login

enum class LoginUiState {
     Valid,
     UserNameError ,
     PasswordError
}

sealed class LoginProgress {
    object Loading : LoginProgress()
    class Success(val displayName: String): LoginProgress()
    class Failed(val errorMsg: Int): LoginProgress()
}