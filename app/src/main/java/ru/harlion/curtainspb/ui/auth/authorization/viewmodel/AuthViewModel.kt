package ru.harlion.curtainspb.ui.auth.authorization.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.curtainspb.repo.data.DataRepository
import java.util.concurrent.Future

class AuthViewModel : ViewModel() {

    var isAuthComplete = MutableLiveData(false)
    var currentTask: Future<*>? = null

    fun authUser(email: String, password: String) {
        DataRepository.authUser(email, password, {}, {}
        )
    }
}