package ru.harlion.curtainspb.ui.save_project

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import okio.buffer
import okio.sink
import okio.source
import ru.harlion.curtainspb.base.BaseFragment
import ru.harlion.curtainspb.databinding.FragmentSaveProjectBinding
import ru.harlion.curtainspb.ui.main_menu.MainMenuFragment
import ru.harlion.curtainspb.ui.request_cost.RequestCostFragment
import ru.harlion.curtainspb.utils.replaceFragment
import java.io.File
import java.io.OutputStream

class SaveProjectFragment : BaseFragment() {

    private lateinit var binding: FragmentSaveProjectBinding
    private lateinit var permissionLauncher: ActivityResultLauncher<String>
    private val viewModel: SaveProjectViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { if (it) saveInGallery() }
    }

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
            saveInGallery() //todo сохранение в галлерею или отправка на сервер при переходе на экран заяки
            sendFilePickOnBd()
            replaceFragment(RequestCostFragment())
        }

        binding.saveProjectInGallery.setOnClickListener {
            saveInGallery()
            showToast("Проект успешно сохранен в галлерею")
            sendFilePickOnBd()
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

    private fun sendFilePickOnBd() {
        val file = File(
            File(requireActivity().filesDir, "upload").also(File::mkdirs),
            "pick.png"
        )
        viewModel.onSendSketchToBD(file)
    }

    private fun saveInGallery() {
        if (Build.VERSION.SDK_INT >= 23 &&
            requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            return permissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        val resolver = requireContext().contentResolver
        val photoCollection =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            else
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        val photoUri = resolver
            .insert(photoCollection, ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "curtain.png")
                if (Build.VERSION.SDK_INT >= 29) put(
                    MediaStore.Video.Media.DATE_TAKEN,
                    System.currentTimeMillis()
                )
            })!!

        ParcelFileDescriptor.AutoCloseOutputStream(resolver.openFileDescriptor(photoUri, "w", null))
            .use { os: OutputStream ->
                os.sink().buffer().writeAll(
                    File(
                        File(requireActivity().filesDir, "upload").also(File::mkdirs),
                        "pick.png"
                    ).source()
                )
            }
    }
}