package com.epitech.extra.epicture

class Imgur {
    companion object {
        var loggedIn: Boolean = false
        var username: String? = null
        var accessToken: String? = null
        var refreshToken: String? = null
        var creationDate: String? = null
    }
}