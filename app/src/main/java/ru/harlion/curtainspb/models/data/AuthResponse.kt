package ru.harlion.curtainspb.models.data

data class AuthResponse(
    val data: Data
)

data class Data(
    val accessToken: String
)