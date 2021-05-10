package ru.harlion.curtainspb.ui.main_menu.saved_projects

import androidx.lifecycle.MutableLiveData
import ru.harlion.curtainspb.base.BaseViewModel
import ru.harlion.curtainspb.models.data.SavedProject
import ru.harlion.curtainspb.repo.data.DataRepository

class SavedProjectsViewModel : BaseViewModel() {

    val savedProjectList: MutableLiveData<List<SavedProject>> = MutableLiveData()

    fun getSavedProjects(userId: Int) {
        +DataRepository.getSavedProjects(userId, {
            savedProjectList.value = it
        }, {

        }, {

        })
    }
}