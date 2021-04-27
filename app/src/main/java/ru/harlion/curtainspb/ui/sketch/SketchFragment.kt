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
import ru.harlion.curtainspb.databinding.FragmentScetchBinding
import ru.harlion.curtainspb.models.data.Template
import ru.harlion.curtainspb.ui.grid_list_sketch.GridListSketchFragment
import ru.harlion.curtainspb.ui.save_project.SaveProjectFragment
import ru.harlion.curtainspb.ui.sketch.recyclerview.SketchAdapter
import ru.harlion.curtainspb.utils.ovalOutline
import ru.harlion.curtainspb.utils.replaceFragment
import java.io.File
import java.io.FileOutputStream

class SketchFragment : Fragment(), IView {

    private lateinit var adapter: SketchAdapter
    private lateinit var binding: FragmentScetchBinding
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
    ): View {
        binding = FragmentScetchBinding.inflate(layoutInflater)
        return binding.root
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

        editorView.bottomView.setImageURI(requireArguments().getParcelable("image"))

        // TODO editorView.topView.loadSomething() загрузить картинку

        initClick()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detach()
    }

    override fun goToSave() {
        saveAndSendBdSketch()
        replaceFragment(SaveProjectFragment())
    }

    override fun showPictures(templates: List<Template>) {
        adapter.templates = templates

    }

    //сохраняет получившиюся картинку  битмап в файл
    // todo отправить на сервер (тепеерь отправлять только если сохраняет в галлерею или отправляет заявку)
    private fun saveAndSendBdSketch() {
        val file = File(
            File(requireActivity().filesDir, "upload").also(File::mkdirs),
            "pick.png"
        )
        editorView.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
        presenter.onSendSketchToBD(file)
    }

    private fun initClick() {

        binding.cardViewSaveProject.setOnClickListener { presenter.onSaveClicked() }

        binding.showAll.setOnClickListener { replaceFragment(GridListSketchFragment()) }

        binding.fSketchBack.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.fSketchBack.ovalOutline()

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
    fun showPictures(templates: List<Template>)
}