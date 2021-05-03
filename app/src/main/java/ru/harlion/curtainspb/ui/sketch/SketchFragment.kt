package ru.harlion.curtainspb.ui.sketch

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.Request
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import it.sephiroth.android.library.uigestures.UIGestureRecognizer
import it.sephiroth.android.library.uigestures.UIGestureRecognizerDelegate
import it.sephiroth.android.library.uigestures.UITapGestureRecognizer
import it.sephiroth.android.library.uigestures.setGestureDelegate
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
        setFragmentResultListener("sketch") { _, bndl ->
            Glide.with(this).load(bndl.getString("url")).into(topViewTarget)
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
        adapter = SketchAdapter { Glide.with(this).load(it.toString()).into(topViewTarget) }
        recyclerView?.layoutManager = llm
        recyclerView?.adapter = adapter

        presenter.attach(this)

        editorView.bottomView.setImageURI(requireArguments().getParcelable("image"))

        // TODO editorView.topView.loadSomething() загрузить картинку

        initClick()

        viewEdit()
    }

    // gesture recognizer actionlistener
    private val actionListener = { recognizer: UIGestureRecognizer ->
        // gesture recognized
    }

    private fun viewEdit() {

        val delegate = UIGestureRecognizerDelegate();

        // single tap gesture
        val recognizer1 = UITapGestureRecognizer(requireContext())
        recognizer1.tapsRequired = 1
        recognizer1.touchesRequired = 1
        recognizer1.tag = "single-tap";
        recognizer1.actionListener = actionListener

        // double tap gesture
        val recognizer2 = UITapGestureRecognizer(requireContext())
        recognizer2.tag = "double-tap"
        recognizer2.tapsRequired = 2
        recognizer2.touchesRequired = 1
        recognizer2.actionListener = actionListener

        // We want to recognize a single tap and a double tap separately. Normally, when the user
        // performs a double tap, the single tap would be triggered twice.
        // In this way, however, the single tap will wait until the double tap will fail. So a single tap
        // and a double tap will be triggered separately.
        recognizer1.requireFailureOf = recognizer2

        // add both gestures to the delegate
        delegate.addGestureRecognizer(recognizer1)
        delegate.addGestureRecognizer(recognizer2)

        // forward the touch events to the delegate
        val rootView = binding.editorView
        rootView.setGestureDelegate(delegate)

        // optional delegate methods
        delegate.shouldReceiveTouch = { recognizer -> true }
        delegate.shouldBegin = { recognizer -> true }
        delegate.shouldRecognizeSimultaneouslyWithGestureRecognizer = { recognizer, other -> true }
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

    private val topViewTarget = object : Target<Drawable> {
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