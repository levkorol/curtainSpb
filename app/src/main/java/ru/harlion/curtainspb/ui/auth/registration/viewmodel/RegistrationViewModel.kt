package ru.harlion.curtainspb.ui.auth.registration.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import ru.harlion.curtainspb.models.data.UsersRequest
import ru.harlion.curtainspb.repo.data.DataRepository
import java.util.concurrent.Future

class RegistrationViewModel : ViewModel() {

    var currentTask: Future<*>? = null

    fun registerUsers(context: Context, name: String, phone: String, email: String, password: String) {
        currentTask = DataRepository.registerUser(
            request = UsersRequest(
                name,
                phone,
                email,
                password
            ),
            {
                context.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                    .putInt("userId", it.userId)
                    .putString("accessToken", it.accessToken)
                    .apply()
            },
            Throwable::printStackTrace
        )
    }
}