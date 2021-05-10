package ru.harlion.curtainspb.models.data

import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl

val BASE_URL = "https://api.pzntech.ru/static/templates/".toHttpUrl()

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