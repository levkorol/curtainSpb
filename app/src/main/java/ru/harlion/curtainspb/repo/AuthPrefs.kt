package ru.harlion.curtainspb.repo

import android.content.SharedPreferences

const val TOKEN = "accessToken"
const val USER_ID = "userId"
const val USER_ROLE = "user_role"
const val NAME_USER = "user_name"
const val PHONE_USER = "user_phone"
const val EMAIL_USER = "user_email"

class AuthPrefs(
    private val prefs: SharedPreferences
) {

    private var token = ""
    private var userId = -1
    private var userRole = -1
    private var nameUser = ""
    private var phoneUser = ""
    private var emailUser = ""

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

    fun getUserName(): String {
        if (nameUser.isEmpty()) {
            nameUser = prefs.getString(NAME_USER, "") ?: ""
        }
        return nameUser
    }

    fun setUserName(nameUser: String) {
        this.nameUser = nameUser
        prefs.edit()
            .putString(NAME_USER, nameUser)
            .apply()
    }

    fun getUserPhone(): String {
        if (phoneUser.isEmpty()) {
            phoneUser = prefs.getString(PHONE_USER, "") ?: ""
        }
        return phoneUser
    }

    fun setUserPhone(phoneUser: String) {
        this.phoneUser = phoneUser
        prefs.edit()
            .putString(PHONE_USER, phoneUser)
            .apply()
    }

    fun getUserEmail(): String {
        if (emailUser.isEmpty()) {
            emailUser = prefs.getString(EMAIL_USER, "") ?: ""
        }
        return emailUser
    }

    fun setUserEmail(emailUser: String) {
        this.emailUser = emailUser
        prefs.edit()
            .putString(EMAIL_USER, emailUser)
            .apply()
    }
}