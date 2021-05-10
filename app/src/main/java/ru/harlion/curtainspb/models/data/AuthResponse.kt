package ru.harlion.curtainspb.models.data

data class Resp<T>(
    val data: T
)

data class AuthData(
    val accessToken: String,
    val userId: Int,
    val userRole: Int
)


