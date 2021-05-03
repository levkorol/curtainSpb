package ru.harlion.curtainspb.ui.grid_list_sketch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.databinding.FragmentGridlListSketchsBinding
import ru.harlion.curtainspb.models.data.Template
import ru.harlion.curtainspb.ui.sketch.recyclerview.SketchAdapter


class GridListSketchFragment : Fragment(), IView {

    private lateinit var adapter: SketchAdapter
    private lateinit var binding: FragmentGridlListSketchsBinding
    private lateinit var presenter: IPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = GridPresenter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGridlListSketchsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView? = view.findViewById(R.id.recycler_view_gridle)
        adapter = SketchAdapter {
            setFragmentResult(
                "sketch",
                Bundle(1).apply { putString("url", it.toString()) }
            )
            parentFragmentManager.popBackStack()
        }
        recyclerView?.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView?.adapter = adapter

        presenter.attach(this)

        initClick()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detach()
    }

    override fun showPictures(templates: List<Template>) {
        adapter.templates = templates
    }

    private fun initClick() {
        binding.cvBackGrid.setOnClickListener { parentFragmentManager.popBackStack() }
    }
}

interface IView {
    fun showPictures(templates: List<Template>)
}