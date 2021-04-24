package ru.harlion.curtainspb.ui.grid_list_sketch

import ru.harlion.curtainspb.repo.SketchRepo

class GridPresenter : IPresenter {

    private var view: IView? = null

    override fun attach(view: IView) {
        this.view = view
//        view.showPictures(SketchRepo.getListSketch())
    }

    override fun detach() {
        view = null
    }
}

interface IPresenter {
    fun attach(view: IView)
    fun detach()
}