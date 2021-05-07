package ru.harlion.curtainspb.models.data

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

data class Resp<T>(
    val data: T
)

data class AuthData(
    val accessToken: String,
    val userId: Int,
    val userRole: Int
)

private val BASE_URL = "https://api.pzntech.ru/static/templates/".toHttpUrl()
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
