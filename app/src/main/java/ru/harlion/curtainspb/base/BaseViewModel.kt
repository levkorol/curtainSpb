package ru.harlion.curtainspb.base

import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import ru.harlion.curtainspb.AppCurtainSpb
import ru.harlion.curtainspb.repo.AuthPrefs
import ru.harlion.curtainspb.repo.data.DataRepository
import java.io.Closeable
import java.io.File

open class BaseViewModel : ViewModel() {

    private lateinit var prefs: AuthPrefs

    private val closeables = ArrayList<Closeable>()

    protected operator fun Closeable.unaryPlus() {
        closeables += this
    }

    @CallSuper
    override fun onCleared() {
        closeables.forEach(Closeable::close)
        super.onCleared()
    }

    fun getProfileUser() {
        prefs = AuthPrefs(
            AppCurtainSpb.appContext.getSharedPreferences(
                "user",
                AppCompatActivity.MODE_PRIVATE
            )
        )
        +DataRepository.getProfile(
            {
                prefs.setUserName(it.name)
                prefs.setUserEmail(it.email)
                prefs.setUserPhone(it.phone.toString())
            },
            {

            }
        )
    }

    fun sendOnBdPick(file: File) {
        /*+DataRepository.templates(
            {

            }, {

            }
        )*/
    }
}