package ru.harlion.curtainspb.ui.auth.authorization

import android.content.Context
import androidx.lifecycle.MutableLiveData
import ru.harlion.curtainspb.base.BaseViewModel
import ru.harlion.curtainspb.repo.AuthPrefs
import ru.harlion.curtainspb.repo.data.DataRepository
import ru.harlion.curtainspb.repo.data.DataRepository.context

class AuthViewModel : BaseViewModel() {

    var isAuthComplete = MutableLiveData(false)
    var isAuthFail = MutableLiveData(false)

    fun authUser(email: String, password: String) {
        +DataRepository.authUser(email, password, {
            val prefs =
                AuthPrefs(context.getSharedPreferences("user", Context.MODE_PRIVATE))
            prefs.setToken(it.accessToken)
            prefs.setUserId(it.userId)
            prefs.setUserRole(it.userRole)
            isAuthComplete.value = true
        }, {
            isAuthFail.value = true
        })
    }
}