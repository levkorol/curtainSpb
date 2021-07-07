package ru.harlion.curtainspb.ui.sketch

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.fragment_scetch.*
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.base.BaseFragment
import ru.harlion.curtainspb.databinding.FragmentScetchBinding
import ru.harlion.curtainspb.models.data.Template
import ru.harlion.curtainspb.repo.AuthPrefs
import ru.harlion.curtainspb.ui.grid_list_sketch.GridListSketchFragment
import ru.harlion.curtainspb.ui.save_project.SaveProjectFragment
import ru.harlion.curtainspb.ui.sketch.recyclerview.SketchAdapter
import ru.harlion.curtainspb.utils.ovalOutline
import ru.harlion.curtainspb.utils.replaceFragment
import java.io.File
import java.io.FileOutputStream


class SketchFragment : BaseFragment(), IView {

    private lateinit var adapter: SketchAdapter
    private lateinit var binding: FragmentScetchBinding
    private lateinit var presenter: IPresenter
    private lateinit var prefs: AuthPrefs

    private val uri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SketchPresenter()
        prefs =
            AuthPrefs(requireContext().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE))
        setFragmentResultListener("sketch") { _, bndl ->
            Glide.with(this).load(bndl.getString("url")).into(binding.editorView.topView)
        }
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
        adapter = SketchAdapter(
            { Glide.with(this).load(it.toString()).into(binding.editorView.topView) },
            this,
            presenter::onEndReached,
        )
        recyclerView?.layoutManager = llm
        recyclerView?.adapter = adapter

        presenter.attach(this)

        requireArguments().let {
            it.getString("imageUrl") // saved
                ?.let { Glide.with(this).load(it).into(editorView.bottomView) }
            it.getParcelable<Uri>("imageUri") // gallery
                ?.let { Glide.with(this).load(it).into(editorView.bottomView) }
            it.getString("imageFile") // camera
                ?.let { editorView.bottomView.setImageBitmap(BitmapFactory.decodeFile(it)) }
        }

        initClick()

        editorView.showWatermark = prefs.getUserRole() != 4
    }

    override fun onDestroyView() {
        super.onDestroyView()
        presenter.detach()
    }

    override fun goToSave() {
        fileToBitmap()
        replaceFragment(SaveProjectFragment())
    }

    override fun showPictures(templates: List<Template>) {
        adapter.templates = templates
        adapter.notifyDataSetChanged()
    }

    private fun fileToBitmap() {
        val file = File(
            File(requireActivity().filesDir, "upload").also(File::mkdirs),
            "pick.png"
        )
        editorView.toBitmap().compress(Bitmap.CompressFormat.PNG, 100, FileOutputStream(file))
    }

    private fun initClick() {

        binding.cardViewSaveProject.setOnClickListener {
            if (requireArguments().getBoolean("edit") || binding.editorView.topView.drawable != null) {
                presenter.onSaveClicked()
            } else {
                showToast("Сначала выберите эскиз")
            }
        }

        binding.cardViewSaveProject.ovalOutline()

        binding.showAll.setOnClickListener { replaceFragment(GridListSketchFragment()) }

        binding.fSketchBack.setOnClickListener { parentFragmentManager.popBackStack() }
        binding.fSketchBack.ovalOutline()
        binding.fSketchBack.clipToOutline = true

        binding.editorView.viewTreeObserver.addOnPreDrawListener {
            val newVisibility =
                if (binding.editorView.topView.drawable != null) View.VISIBLE else View.GONE
            if (binding.removeSketch.visibility == newVisibility) true else {
                binding.removeSketch.visibility = newVisibility
                false
            }
        }

        binding.removeSketch.setOnClickListener {
            binding.editorView.topView.setImageDrawable(
                null
            )
        }

    }

    companion object {
        fun newInstance(image: Uri): SketchFragment {
            val fragment = SketchFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("imageUri", image)
            }
            return fragment
        }

        fun newInstance(image: File): SketchFragment {
            val fragment = SketchFragment()
            fragment.arguments = Bundle().apply {
                putString("imageFile", image.absolutePath)
            }
            return fragment
        }

        fun forProject(url: String): SketchFragment = SketchFragment().apply {
            arguments = Bundle(1).apply {
                putString("imageUrl", url)
                putBoolean("edit", true)
            }
        }
    }
}

interface IView {
    fun goToSave()
    fun showPictures(templates: List<Template>)
}