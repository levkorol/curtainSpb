package ru.harlion.curtainspb.ui.auth.password_recovery

import androidx.lifecycle.MutableLiveData
import ru.harlion.curtainspb.base.BaseViewModel
import ru.harlion.curtainspb.repo.data.DataRepository

class PasswordRecoveryViewModel : BaseViewModel() {

    val isSuccess = MutableLiveData(false)
    val isError = MutableLiveData(false)

    fun passwordRecovery(email: String) {
        +DataRepository.recover(email, {
            isSuccess.value = true
        }, {
            isError.value = true
        }, {

        })
    }
}