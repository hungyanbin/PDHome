package com.yanbin.pdhome.ui.login

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast

import com.yanbin.pdhome.R

class LoginActivity : AppCompatActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val login = findViewById<Button>(R.id.login)
        val loading = findViewById<ProgressBar>(R.id.loading)

        loginViewModel = ViewModelProviders.of(this, LoginViewModelFactory())
            .get(LoginViewModel::class.java)

        loginViewModel.loginState.observe(this@LoginActivity, Observer {
            val loginState = it ?: return@Observer

            when(loginState) {
                LoginUiState.Valid -> login.isEnabled = true
                LoginUiState.UserNameError -> {
                    login.isEnabled = false
                    username.error = getString(R.string.invalid_username)
                }
                LoginUiState.PasswordError -> {
                    login.isEnabled = false
                    password.error = getString(R.string.invalid_password)
                }
            }
        })

        loginViewModel.loginProgress.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            when (loginResult) {
                is LoginProgress.Success -> {
                    loading.visibility = View.GONE
                    updateUiWithUser(loginResult.displayName)
                }
                is LoginProgress.Failed -> {
                    loading.visibility = View.GONE
                    showLoginFailed(loginResult.errorMsg)
                }
                is LoginProgress.Loading -> {
                    loading.visibility = View.VISIBLE
                }
            }
            //Complete and destroy login activity once successful
//            finish()
        })

        username.afterTextChanged {
            loginViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }

        password.apply {
            afterTextChanged {
                loginViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }

            setOnEditorActionListener { _, actionId, _ ->
                when (actionId) {
                    EditorInfo.IME_ACTION_DONE ->
                        loginViewModel.login(
                            username.text.toString(),
                            password.text.toString()
                        )
                }
                false
            }

            login.setOnClickListener {
//                loading.visibility = View.VISIBLE
                loginViewModel.login(username.text.toString(), password.text.toString())
            }
        }
    }

    private fun updateUiWithUser(displayName: String) {
        val welcome = getString(R.string.welcome)
        // TODO : initiate successful logged in experience
        Toast.makeText(
            applicationContext,
            "$welcome $displayName",
            Toast.LENGTH_LONG
        ).show()
    }

    private fun showLoginFailed(@StringRes errorString: Int) {
        Toast.makeText(applicationContext, errorString, Toast.LENGTH_SHORT).show()
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}