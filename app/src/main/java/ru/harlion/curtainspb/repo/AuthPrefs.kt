package ru.harlion.curtainspb.repo

import android.content.SharedPreferences

const val TOKEN = "token"
const val USER_ID = "userId"
const val USER_ROLE = "user_role"

class AuthPrefs(
    private val prefs: SharedPreferences
) {

    private var token = ""
    private var userId = -1
    private var userRole = -1

    fun hasToken(): Boolean {
        return prefs.contains(TOKEN)
    }

    fun getToken(): String {
        if (token.isEmpty()) {
            token = prefs.getString(TOKEN, "") ?: ""
        }
        return token
    }

    fun setToken(token: String) {
        this.token = token
        prefs.edit()
            .putString(TOKEN, token)
            .apply()
    }

    fun removeToken() {
        token = ""
        prefs.edit()
            .remove(TOKEN)
            .apply()
    }

    fun hasUserId(): Boolean {
        return prefs.contains(USER_ID)
    }

    fun getUserId(): Int {
        if (userId < 0) {
            userId = prefs.getInt(USER_ID, -1)
        }
        return userId
    }

    fun setUserId(userId: Int) {
        this.userId = userId
        prefs.edit()
            .putInt(USER_ID, userId)
            .apply()
    }

    fun removeUserId() {
        userId = -1
        prefs.edit()
            .remove(USER_ID)
            .apply()
    }


    fun hasUserRole(): Boolean {
        return prefs.contains(USER_ROLE)
    }

    fun getUserRole(): Int {
        if (userRole < 0) {
            userId = prefs.getInt(USER_ROLE, -1)
        }
        return userRole
    }

    fun setUserRole(userRole: Int) {
        this.userRole = userRole
        prefs.edit()
            .putInt(USER_ROLE, userId)
            .apply()
    }

    fun removeUserRole() {
        userId = -1
        prefs.edit()
            .remove(USER_ROLE)
            .apply()
    }
}