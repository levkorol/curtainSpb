package ru.harlion.curtainspb.ui.sketch

import ru.harlion.curtainspb.repo.SketchRepo

class SketchPresenter : IPresenter {

    private var view: IView? = null

    override fun attach(view: IView) {
        this.view = view
        view.showPictures(SketchRepo.getListSketch())
    }

    override fun detach() {
        view = null
    }

    override fun onSaveClicked() {
        view?.goToSave()
    }

    override fun onSendSketchToBD() {

    }
}

interface IPresenter {
    fun attach(view: IView)
    fun detach()
    fun onSaveClicked()
    fun onSendSketchToBD()
}
