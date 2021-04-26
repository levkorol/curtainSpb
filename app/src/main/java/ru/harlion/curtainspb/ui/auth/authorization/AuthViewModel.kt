package ru.harlion.curtainspb.ui.auth.authorization

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.harlion.curtainspb.repo.data.DataRepository
import java.io.Closeable

class AuthViewModel : ViewModel() {

    var isAuthComplete = MutableLiveData(false)

    private val closeables = ArrayList<Closeable>()

    fun authUser(email: String, password: String) {
        +DataRepository.authUser(email, password, {
            isAuthComplete.value = true
        }, {})
    }

    protected operator fun Closeable.unaryPlus() {
        closeables += this
    }

    override fun onCleared() {
        super.onCleared()
        closeables.forEach(Closeable::close)
    }
}