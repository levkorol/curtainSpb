package ru.harlion.curtainspb.ui.sketch

import android.content.Context.MODE_PRIVATE
import ru.harlion.curtainspb.AppCurtainSpb
import ru.harlion.curtainspb.models.data.Template
import ru.harlion.curtainspb.repo.AuthPrefs
import ru.harlion.curtainspb.repo.data.DataRepository
import ru.harlion.curtainspb.utils.Pager

class SketchPresenter : IPresenter {

    private val prefs =
        AuthPrefs(AppCurtainSpb.appContext.getSharedPreferences("user", MODE_PRIVATE))

    private var view: IView? = null
    private val pager = Pager<Template>(
        10,
        { page, pageSize ->

            if (prefs.hasToken()) {
                DataRepository.templatesForUser(
                    page,
                    pageSize,
                    { loaded(it) },
                    { it.printStackTrace(); loaded(null) })
            } else {
                DataRepository.templates(
                    page,
                    pageSize,
                    { loaded(it) },
                    { it.printStackTrace(); loaded(null) })
            }

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

    override fun onEndReached() {
        pager.load()
    }
}

interface IPresenter {
    fun attach(view: IView)
    fun detach()
    fun onSaveClicked()
    fun onEndReached()
}
