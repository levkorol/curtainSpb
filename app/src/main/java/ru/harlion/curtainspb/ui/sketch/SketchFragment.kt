package ru.harlion.curtainspb.ui.sketch

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
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
            it.getString("imageUrl")
                ?.let { Glide.with(this).load(it).into(editorView.bottomView) }
            it.getParcelable<Uri>("imageUri")
                ?.let { Glide.with(this).load(it).into(editorView.bottomView) }
            it.getString("imageFile")
                ?.let { editorView.bottomView.setImageBitmap(BitmapFactory.decodeFile(it)) }
        }

        initClick()


        if (prefs.getUserRole() == 4) {
            editorView.showWatermark = false
        } else {
            editorView.showWatermark = true
        }
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

    /*private val topViewTarget = object : Target<Drawable> {
        override fun onStart() {}
        override fun onStop() {}
        override fun onDestroy() {}
        override fun onLoadStarted(placeholder: Drawable?) {}
        override fun onLoadFailed(errorDrawable: Drawable?) {}
        override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
            binding.editorView.topView.setImageDrawable(resource)
        }

        override fun onLoadCleared(placeholder: Drawable?) {}
        override fun getSize(cb: SizeReadyCallback) {
            cb.onSizeReady(Int.MIN_VALUE, Int.MIN_VALUE)
        }

        override fun removeCallback(cb: SizeReadyCallback) {}

        private var request: Request? = null
        override fun setRequest(request: Request?) {
            this.request = request
        }

        override fun getRequest(): Request? = request
    }*/

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