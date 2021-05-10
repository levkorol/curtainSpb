package ru.harlion.curtainspb.ui.main_menu.saved_projects

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.databinding.FragmentSavedProjectsBinding
import ru.harlion.curtainspb.repo.AuthPrefs

class SavedProjectsFragment : Fragment() {

    private lateinit var adapter: SavedProjectsAdapter
    private lateinit var binding: FragmentSavedProjectsBinding
    private val viewModel: SavedProjectsViewModel by viewModels()
    private lateinit var prefs: AuthPrefs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSavedProjectsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        prefs =
            AuthPrefs(requireContext().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE))

        val recyclerView: RecyclerView? = view.findViewById(R.id.recycler_view_saved_project)
        val llm = LinearLayoutManager(view.context)
        llm.orientation = LinearLayoutManager.VERTICAL
        adapter = SavedProjectsAdapter { }
        recyclerView?.layoutManager = llm
        recyclerView?.adapter = adapter

        initViewModel()

        binding.cvBack.setOnClickListener { parentFragmentManager.popBackStack() }
    }

    private fun initViewModel() {
        viewModel.getSavedProjects(prefs.getUserId())

        viewModel.savedProjectList.observe(viewLifecycleOwner) { savedProject ->
            adapter.savedProjects = savedProject
        }
    }
}