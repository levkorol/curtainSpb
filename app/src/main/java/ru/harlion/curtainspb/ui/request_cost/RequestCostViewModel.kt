package ru.harlion.curtainspb.ui.request_cost

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.harlion.curtainspb.base.BaseViewModel
import ru.harlion.curtainspb.repo.data.DataRepository

class RequestCostViewModel : BaseViewModel() {

    private val _cost: MutableLiveData<String> = MutableLiveData("")
    val cost: LiveData<String> get() = _cost


    fun getProfile() {
        +DataRepository.getProfile(
            { _cost.value = it.name },
            {}
        )
    }
}