package ru.harlion.curtainspb.ui.sketch

class SketchPresenter {
    private var view: View? = null
    fun attach(view: View) {
        this.view = view
    }
    fun detach() {
        view = null
    }

    fun onSaveClicked() {
        view?.goToSave()
    }

    interface View {
        fun goToSave()
    }

}
