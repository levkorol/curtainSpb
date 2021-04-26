package ru.harlion.curtainspb.ui.save_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.harlion.curtainspb.databinding.FragmentSaveProjectBinding
import ru.harlion.curtainspb.ui.main_menu.MainMenuFragment
import ru.harlion.curtainspb.ui.request_cost.RequestCostFragment
import ru.harlion.curtainspb.utils.replaceFragment


class SaveProjectFragment : Fragment() {

    private lateinit var binding: FragmentSaveProjectBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSaveProjectBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.goMainMenu.setOnClickListener {
            replaceFragment(MainMenuFragment())
        }

        binding.saveProjectInGalleryAndRequest.setOnClickListener {
            replaceFragment(RequestCostFragment())
        }

        binding.saveProjectInGallery.setOnClickListener {
            saveInFolderGallery()
        }

        binding.fSaveProjectUrlSite.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://pzn.su")
            )
            startActivity(browserIntent)
        }

        binding.cvBackSaveProject.setOnClickListener { parentFragmentManager.popBackStack() }
    }

    private fun saveInFolderGallery() {
        // File(File(requireActivity().filesDir, "upload").also(File::mkdirs), "pick.png").renameTo("")
    }

    companion object {

        fun newInstance(image: Uri): SaveProjectFragment {
            val fragment = SaveProjectFragment()
            fragment.arguments = Bundle().apply {
                putParcelable("image", image)
            }
            return fragment
        }
    }
}