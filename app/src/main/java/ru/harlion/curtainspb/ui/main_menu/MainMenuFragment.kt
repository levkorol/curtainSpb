package ru.harlion.curtainspb.ui.main_menu

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.vansuita.pickimage.IntentResolver
import kotlinx.android.synthetic.main.fragment_main_menu.*
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.ui.sketch.SketchFragment
import ru.harlion.curtainspb.utils.replaceFragment


class MainMenuFragment : Fragment() {

    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2

    }

    private lateinit var picker: IntentResolver

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        picker = IntentResolver(requireActivity(), "", savedInstanceState)

        open_camera.setOnClickListener {
            startActivityForResult(picker.cameraIntent, CAMERA)
        }

        open_gallery.setOnClickListener {
            startActivityForResult(picker.galleryIntent, GALLERY)
        }

        open_gallery_folder_project.setOnClickListener {
            openGallerySaveProject()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        picker.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA) {
            replaceFragment(SketchFragment.newInstance(Uri.fromFile(picker.cameraFile())))
        }
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY) {
            replaceFragment(SketchFragment.newInstance(data!!.data!!))
        }
    }

    private fun openGallerySaveProject() {
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        // intent.setDataAndType(Uri.withAppendedPath(Uri.fromFile(file), "/AppPics"), "image/*")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }
}