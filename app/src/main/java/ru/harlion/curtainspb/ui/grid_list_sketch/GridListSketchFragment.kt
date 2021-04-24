package ru.harlion.curtainspb.ui.grid_list_sketch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.models.Sketch
import ru.harlion.curtainspb.models.data.Template
import ru.harlion.curtainspb.ui.sketch.recyclerview.SketchAdapter


class GridListSketchFragment : Fragment(), IView {

    private lateinit var adapter: SketchAdapter

    private lateinit var presenter: IPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = GridPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_gridl_list_sketchs, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView? = view.findViewById(R.id.recycler_view_gridle)
        val llm = LinearLayoutManager(view.context)
        // llm.orientation = LinearLayoutManager.VERTICAL
        adapter = SketchAdapter()
        recyclerView?.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView?.adapter = adapter

        presenter.attach(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun showPictures(templates: List<Template>) {
        adapter.templates = templates
    }
}

interface IView {
    fun showPictures(templates: List<Template>)
}