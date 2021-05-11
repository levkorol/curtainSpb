package ru.harlion.curtainspb.ui.grid_list_sketch

import ru.harlion.curtainspb.repo.data.DataRepository
import java.io.Closeable

class GridPresenter : IPresenter {

    private var view: IView? = null
    private var currentCall: Closeable? = null

    override fun attach(view: IView) {
        this.view = view
        currentCall = DataRepository.templates(
            1, 100500,
            view::showPictures,
            Throwable::printStackTrace,
        )
    }

    override fun detach() {
        view = null
    }
}

interface IPresenter {
    fun attach(view: IView)
    fun detach()
}