package ru.harlion.curtainspb.ui.save_project

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_save_project.*
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.ui.main_menu.MainMenuFragment
import ru.harlion.curtainspb.ui.request_cost.RequestCostFragment
import ru.harlion.curtainspb.utils.replaceFragment


class SaveProjectFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_save_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        go_main_menu.setOnClickListener {
            replaceFragment(MainMenuFragment())
        }

        save_project_in_gallery_and_request.setOnClickListener {
            replaceFragment(RequestCostFragment())
        }

        save_project_in_gallery.setOnClickListener {
            saveInFolderGallery()
        }
    }

    private fun saveInFolderGallery() {
//        val path: String =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + directory
//        val outputDir = File(path)
//        outputDir.mkdirs()
//        val newFile = File(path + File.separator.toString() + "test.png")
//        val out = FileOutputStream(newFile)
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
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