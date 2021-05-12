package ru.harlion.curtainspb.ui.sketch

import ru.harlion.curtainspb.models.data.Template
import ru.harlion.curtainspb.repo.data.DataRepository
import ru.harlion.curtainspb.utils.Pager
import java.io.File

class SketchPresenter : IPresenter {

    private var view: IView? = null
    private val pager = Pager<Template>(
        10,
        { page, pageSize ->
            DataRepository.templates(page, pageSize, { loaded(it) }, { it.printStackTrace(); loaded(null) })
        },
        { items ->
            view?.showPictures(items)
        }
    )

    override fun attach(view: IView) {
        this.view = view
        pager.get()
    }

    override fun detach() {
        view = null
        pager.dispose()
    }

    override fun onSaveClicked() {
        view?.goToSave()
    }

    override fun onSendSketchToBD(file: File) {
        DataRepository.sendPhoto(file)
    }

    override fun onEndReached() {
        pager.load()
    }
}

interface IPresenter {
    fun attach(view: IView)
    fun detach()
    fun onSaveClicked()
    fun onSendSketchToBD(file: File)
    fun onEndReached()
}
