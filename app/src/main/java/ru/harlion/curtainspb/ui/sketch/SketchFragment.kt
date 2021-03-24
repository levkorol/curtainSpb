package ru.harlion.curtainspb.ui.sketch

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
import ru.harlion.curtainspb.ui.save_project.SaveProjectFragment
import ru.harlion.curtainspb.ui.sketch.recyclerview.SketchAdapter
import ru.harlion.curtainspb.utils.replaceFragment

class SketchFragment : Fragment(), SketchPresenter.View {

    private lateinit var adapter: SketchAdapter

    private lateinit var presenter: SketchPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SketchPresenter()

    }

    override fun onDestroy() {
        super.onDestroy()

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

        sketch_iv.setImageURI(arguments!!.getParcelable("image"))

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

    override fun goToSave() {
        replaceFragment(SaveProjectFragment())
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