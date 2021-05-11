package ru.harlion.curtainspb.models.data

import androidx.appcompat.app.AppCompatActivity
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import ru.harlion.curtainspb.AppCurtainSpb
import ru.harlion.curtainspb.repo.AuthPrefs

val SAVED_PROJECT_URL = "https://api.pzntech.ru/static/users/".toHttpUrl()

class SavedProject(
    val id: Int = 0,
    val name: String = "",
) {
    private val prefs = AuthPrefs(
        AppCurtainSpb.appContext.getSharedPreferences(
            "user",
            AppCompatActivity.MODE_PRIVATE
        )
    )
    private val userId get() = prefs.getUserId()

    val imageUrl: HttpUrl
        get() = SAVED_PROJECT_URL.resolve("$userId/$name")!!
}