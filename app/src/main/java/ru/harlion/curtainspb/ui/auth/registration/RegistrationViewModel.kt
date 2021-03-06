package ru.harlion.curtainspb.ui.auth.registration

import android.content.Context
import android.content.Context.MODE_PRIVATE
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.curtainspb.models.data.UsersRequest
import ru.harlion.curtainspb.repo.AuthPrefs
import ru.harlion.curtainspb.repo.data.DataRepository
import java.io.Closeable

class RegistrationViewModel : ViewModel() {

    var isRegistrationComplete = MutableLiveData(false)

    var currentTask: Closeable? = null

    fun registerUsers(
        context: Context,
        name: String,
        phone: String,
        email: String,
        password: String
    ) {
        currentTask = DataRepository.registerUser(
            request = UsersRequest(
                name,
                phone,
                email,
                password
            ),
            {
                val prefs =
                    AuthPrefs(context.getSharedPreferences("user", MODE_PRIVATE))
                prefs.setToken(it.accessToken)
                prefs.setUserId(it.userId)

                isRegistrationComplete.value = true
            },
            Throwable::printStackTrace
        )
    }

    override fun onCleared() {
        super.onCleared()
        currentTask?.let {
            it.close()
            currentTask = null
        }
    }
}