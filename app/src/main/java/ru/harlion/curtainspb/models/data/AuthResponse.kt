package ru.harlion.curtainspb.models.data

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

data class Resp<T>(
    val data: T
)

data class AuthData(
    val accessToken: String,
    val userId: Int,
)

private val BASE_URL = "https://api.pzntech.ru/api/v1/static/".toHttpUrl()
class Template(
    val id: Int,
    val name: String,
    private val image: String,
    val roleId: Int,
    val isOpen: Boolean,
) {
    val imageUrl: HttpUrl
        get() = BASE_URL.resolve(image)!!
}
