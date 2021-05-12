package ru.harlion.curtainspb.models.data

import com.google.gson.annotations.SerializedName

data class UsersRequest(
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    @SerializedName("password")
    val password: String = "",
)