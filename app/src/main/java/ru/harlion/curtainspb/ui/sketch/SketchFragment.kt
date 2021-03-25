package ru.harlion.curtainspb.ui.sketch

import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_scetch.*
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.models.Sketch
import ru.harlion.curtainspb.ui.save_project.SaveProjectFragment
import ru.harlion.curtainspb.ui.sketch.recyclerview.SketchAdapter
import ru.harlion.curtainspb.utils.replaceFragment

class SketchFragment : Fragment(), IView {

    private lateinit var adapter: SketchAdapter

    private lateinit var presenter: IPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SketchPresenter()

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_scetch, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.attach(this)

        editorView.bottomView.setImageURI(arguments!!.getParcelable("image"))

        // TODO editorView.topView.loadSomething()

        cardView_save_project.setOnClickListener {
            presenter.onSaveClicked()
        }

        val recyclerView: RecyclerView? = view.findViewById(R.id.recyclerView)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.HORIZONTAL
        adapter = SketchAdapter()
        recyclerView?.layoutManager = llm
        recyclerView?.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detach()
    }

    //region IView
    override fun goToSave() {
        replaceFragment(SaveProjectFragment())
    }

    override fun showPictures(sketches: List<Sketch>) {
        // TODO adapter <- sketches
    }
    //endregion IView

    // TODO проверить по нажатию на какую-нибудь кнопочку
    private fun saveTemp() {
//        val path: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + directory
//        val outputDir = File(path)
//        outputDir.mkdirs()
//        val newFile = File(path + File.separator.toString() + "test.png")
//        val out = FileOutputStream(newFile)
//        editorView.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    companion object {
        fun newInstance(image: Uri): SketchFragment {
            val fragment = SketchFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("image", image)
            }
            return fragment
        }
    }

}

interface IView {
    fun goToSave()
    fun showPictures(sketches: List<Sketch>)
}