package ru.harlion.curtainspb.ui.auth.registration.viewmodel

import androidx.lifecycle.ViewModel
import ru.harlion.curtainspb.models.data.UsersRequest
import ru.harlion.curtainspb.repo.data.DataRepository
import java.util.concurrent.Future

class RegistrationViewModel : ViewModel() {

    var currentTask: Future<*>? = null

    fun registerUsers(name: String, phone: String, email: String, password: String) {
        currentTask = DataRepository.registerUser(
            request = UsersRequest(
                name,
                phone,
                email,
                password
            ),
            {},
            Throwable::printStackTrace
        )
    }
}