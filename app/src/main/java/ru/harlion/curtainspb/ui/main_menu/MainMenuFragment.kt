package ru.harlion.curtainspb.ui.main_menu

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore.ACTION_IMAGE_CAPTURE
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_main_menu.*
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.ui.sketch.SketchFragment
import ru.harlion.curtainspb.ui.splash.SplashFragment
import ru.harlion.curtainspb.utils.replaceFragment


class MainMenuFragment : Fragment() {

    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private val CAMERA_REQUEST = 1888
    private val MY_CAMERA_PERMISSION_CODE = 100

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
            replaceFragment(SplashFragment())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE) {

            val bitmap = data?.extras?.get("data")
            replaceFragment(SketchFragment())
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
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(arrayOf(CAMERA), MY_CAMERA_PERMISSION_CODE)
        } else {
            val cameraIntent = Intent(ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, CAMERA_REQUEST)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "camera permission granted", Toast.LENGTH_LONG)
                    .show()
                val cameraIntent = Intent(ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            } else {
                Toast.makeText(requireContext(), "camera permission denied", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }
}