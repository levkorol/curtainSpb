package ru.harlion.curtainspb.ui.request_cost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.harlion.curtainspb.base.BaseViewModel
import ru.harlion.curtainspb.repo.data.DataRepository

class RequestCostViewModel : BaseViewModel() {

    private val _cost: MutableLiveData<String> = MutableLiveData("")
    val cost: LiveData<String> get() = _cost

    private val _phoneNumber: MutableLiveData<String> = MutableLiveData("")
    val phoneNumber: LiveData<String> get() = _phoneNumber

    private val _email: MutableLiveData<String> = MutableLiveData("")
    val email: LiveData<String> get() = _email

    fun getProfile() {
        +DataRepository.getProfile(
            {
                _cost.value = it.name
                _phoneNumber.value = it.phone
                _email.value = it.email
            },
            {

            }
        )
    }
}