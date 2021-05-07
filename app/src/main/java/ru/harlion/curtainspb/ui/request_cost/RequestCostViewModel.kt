package ru.harlion.curtainspb.ui.request_cost

import ru.harlion.curtainspb.base.BaseViewModel
import ru.harlion.curtainspb.repo.data.DataRepository

class RequestCostViewModel : BaseViewModel() {

    fun getProfile(name: String) {
        +DataRepository.getProfile(
            {
                it.name
            },
            {}
        )
    }
}