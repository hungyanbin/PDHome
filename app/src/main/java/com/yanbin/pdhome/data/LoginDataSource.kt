package com.yanbin.pdhome.data

import com.yanbin.pdhome.data.model.LoggedInUser
import io.reactivex.rxjava3.core.Single
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {
    // User: yanbin, Password: 1234
    fun login(username: String, password: String): Single<LoggedInUser> {
        return Single.just(Pair(username, password))
            .delay(3, TimeUnit.SECONDS)
            .map { (username, password) ->
                when {
                    username == "yanbin" && password == "123456" -> {
                        LoggedInUser(java.util.UUID.randomUUID().toString(), "Yanbin")
                    }
                    else -> throw IllegalArgumentException("User not exist")
                }
            }
    }

    fun logout() {
        // TODO: revoke authentication
    }
}