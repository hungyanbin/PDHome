package com.yanbin.pdhome.data

import com.yanbin.pdhome.data.model.LoggedInUser
import io.reactivex.rxjava3.core.Single

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {

    // in-memory cache of the loggedInUser object
    var user: LoggedInUser? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    fun login(username: String, password: String): Single<LoggedInUser> {
        return dataSource.login(username, password)
            .doOnSuccess { setLoggedInUser(it) }
    }

    private fun setLoggedInUser(loggedInUser: LoggedInUser) {
        this.user = loggedInUser
    }
}