package ru.harlion.curtainspb.ui.sketch

import ru.harlion.curtainspb.repo.data.DataRepository
import java.io.Closeable
import java.io.File

class SketchPresenter : IPresenter {

    private var view: IView? = null
    private var currentCall: Closeable? = null

    override fun attach(view: IView) {
        this.view = view
        currentCall = DataRepository.templates(
            view::showPictures,
            Throwable::printStackTrace,
        )
    }

    override fun detach() {
        view = null
    }

    override fun onSaveClicked() {
        view?.goToSave()
    }

    override fun onSendSketchToBD(file: File) {
        DataRepository.sendPhoto(file)
    }
}

interface IPresenter {
    fun attach(view: IView)
    fun detach()
    fun onSaveClicked()
    fun onSendSketchToBD(file: File)
}
