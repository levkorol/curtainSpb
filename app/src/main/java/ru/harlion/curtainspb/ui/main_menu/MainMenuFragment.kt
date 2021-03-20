package ru.harlion.curtainspb.ui.main_menu

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main_menu.*
import ru.harlion.curtainspb.R


class MainMenuFragment : Fragment() {

    companion object {
        private const val PICK_IMAGE = 100
        private const val CAMERA_INTENT = 12
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        open_camera.setOnClickListener {
            openCamera()
        }

        open_gallery.setOnClickListener {
            openGallery()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {

        }
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA_INTENT) {

        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE)
    }

    private fun openCamera() {
        val intent = Intent(ACTION_IMAGE_CAPTURE)
        //  intent.putExtra(EXTRA_OUTPUT, )
        startActivityForResult(intent, CAMERA_INTENT)
    }

}