package ru.harlion.curtainspb.ui.auth.authorization.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.curtainspb.repo.data.DataRepository
import java.io.Closeable
import java.util.concurrent.Future

class AuthViewModel : ViewModel() {

    var isAuthComplete = MutableLiveData(false)
    var currentTask: Future<*>? = null
    private val closeables = ArrayList<Closeable>()

    fun authUser(email: String, password: String) {
        + DataRepository.authUser(email, password, {}, {})
    }

    protected operator fun Closeable.unaryPlus() {
        closeables += this
    }

    override fun onCleared() {
        super.onCleared()
        closeables.forEach(Closeable::close)
    }
}