package ru.harlion.curtainspb.ui.sketch

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_scetch.*
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.models.Sketch
import ru.harlion.curtainspb.ui.grid_list_sketch.GridListSketchFragment
import ru.harlion.curtainspb.ui.save_project.SaveProjectFragment
import ru.harlion.curtainspb.ui.sketch.recyclerview.SketchAdapter
import ru.harlion.curtainspb.utils.replaceFragment
import java.io.File
import java.io.FileOutputStream

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

        val recyclerView: RecyclerView? = view.findViewById(R.id.recyclerView)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.HORIZONTAL
        adapter = SketchAdapter()
        recyclerView?.layoutManager = llm
        recyclerView?.adapter = adapter

        presenter.attach(this)

        editorView.bottomView.setImageURI(arguments!!.getParcelable("image"))

        // TODO editorView.topView.loadSomething() загрузить картинку

        initClick()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detach()
    }

    override fun goToSave() {
        saveTemp()
        replaceFragment(SaveProjectFragment())
    }

    override fun showPictures(sketches: List<Sketch>) {
        adapter.sketch = sketches

    }

    private fun saveTemp() { //todo сохранять получившиюся картинку  битмап в файл
        //    val path: String = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + context?.packageName//directory
        //  val path: String = Environment.getRootDirectory() + "/test_image.jpg"
        val out = FileOutputStream(File(File(requireActivity().filesDir, "upload").also(File::mkdirs), "pick.png"))
        editorView.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    private fun initClick() {
        cardView_save_project.setOnClickListener {
            presenter.onSaveClicked()
        }

        show_all.setOnClickListener {
            replaceFragment(GridListSketchFragment())
        }

//        delete_pick.setOnClickListener {
//            editorView.topView.setImageDrawable(null)
//            editorView.topView.visibility = View.GONE
//            delete_pick.visibility = View.GONE
//            save_sketch.visibility = View.GONE
//        }
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