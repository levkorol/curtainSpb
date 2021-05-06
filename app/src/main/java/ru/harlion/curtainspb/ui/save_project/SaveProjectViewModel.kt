package ru.harlion.curtainspb.ui.save_project

import ru.harlion.curtainspb.base.BaseViewModel
import ru.harlion.curtainspb.repo.data.DataRepository
import java.io.File

class SaveProjectViewModel : BaseViewModel() {

    fun onSendSketchToBD(file: File) {
        DataRepository.sendPhoto(file)
    }
}