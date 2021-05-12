package ru.harlion.curtainspb.ui.grid_list_sketch

import ru.harlion.curtainspb.models.data.Template
import ru.harlion.curtainspb.repo.data.DataRepository
import ru.harlion.curtainspb.utils.Pager

class GridPresenter : IPresenter {

    private var view: IView? = null
    private val pager = Pager<Template>(
        30,
        { page, pageSize ->
            DataRepository.templates(
                page,
                pageSize,
                { loaded(it) },
                { it.printStackTrace(); loaded(null) })
        },
        { items ->
            view?.showPictures(items)
        }
    )

    override fun attach(view: IView) {
        this.view = view
        pager.load()
    }

    override fun detach() {
        view = null
        pager.dispose()
    }

    override fun onEndReached() {
        pager.load()
    }
}

interface IPresenter {
    fun attach(view: IView)
    fun detach()
    fun onEndReached()
}