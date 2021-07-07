package ru.harlion.curtainspb.ui.main_menu

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.exifinterface.media.ExifInterface
import androidx.fragment.app.viewModels
import com.vansuita.pickimage.IntentResolver
import ru.harlion.curtainspb.base.BaseFragment
import ru.harlion.curtainspb.base.CommonDialog
import ru.harlion.curtainspb.databinding.FragmentMainMenuBinding
import ru.harlion.curtainspb.repo.AuthPrefs
import ru.harlion.curtainspb.ui.auth.authorization.AuthFragment
import ru.harlion.curtainspb.ui.auth.registration.RegistrationFragment
import ru.harlion.curtainspb.ui.main_menu.saved_projects.SavedProjectsFragment
import ru.harlion.curtainspb.ui.sketch.SketchFragment
import ru.harlion.curtainspb.utils.replaceFragment
import java.io.File
import java.io.FileOutputStream


class MainMenuFragment : BaseFragment() {

    private lateinit var prefs: AuthPrefs
    private lateinit var dialog: CommonDialog
    private val viewModel: MainMenuViewModel by viewModels()

    companion object {
        private const val CAMERA = 1
        private const val GALLERY = 2
    }

    private lateinit var picker: IntentResolver
    private lateinit var binding: FragmentMainMenuBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs =
            AuthPrefs(requireContext().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        picker = IntentResolver(requireActivity(), "", savedInstanceState)

        initClicks()

        visibleViews()

        initViewModel()
    }

    private fun initViewModel() {
        if (prefs.hasToken()) {
            viewModel.getProfileUser()
        }
    }

    private fun visibleViews() {
        if (prefs.hasToken()) {
            binding.exit.visibility = View.VISIBLE
            binding.registration.visibility = View.GONE
        } else {
            binding.exit.visibility = View.GONE
            binding.registration.visibility = View.VISIBLE
        }
    }

    private fun initClicks() {
        binding.openCamera.setOnClickListener {
            startActivityForResult(picker.cameraIntent, CAMERA)
        }

        binding.openGallery.setOnClickListener {
            startActivityForResult(picker.galleryIntent, GALLERY)
        }

        binding.openGalleryFolderProject.setOnClickListener {
            openGallerySaveProject()
        }

        binding.exit.setOnClickListener {
            openDialog()
        }

        binding.registration.setOnClickListener {
            replaceFragment(RegistrationFragment())
        }

        binding.urlSite.setOnClickListener {
            val browserIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://pzn.su")
            )
            startActivity(browserIntent)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        picker.onSaveInstanceState(outState)
    }

    private var cameraHandled = false
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && requestCode == CAMERA) {
            val image = picker.cameraFile()
            val rotation = file.inputStream().use(::getRotation)
            if (rotation != 0) {
                rotate(BitmapFactory.decodeFile(file.path), rotation)
                    .compress(Bitmap.CompressFormat.JPEG, 95, FileOutputStream(file))
            }
            replaceFragment(SketchFragment.newInstance(image))
            cameraHandled = true
        }
        if (resultCode == Activity.RESULT_OK && requestCode == GALLERY) {
            val rotation = requireContext().contentResolver.openInputStream(data!!.data!!)!!.use(::getRotation)
            if (rotation == 0) {
                replaceFragment(SketchFragment.newInstance(data!!.data!!))
            } else {
                val image = picker.cameraFile()
                val bitmap = requireContext().contentResolver.openInputStream(data!!.data!!)!!
                    .use(BitmapFactory::decodeStream)
                rotate(bitmap, rotation)
                    .compress(Bitmap.CompressFormat.JPEG, 95, FileOutputStream(file))
                replaceFragment(SketchFragment.newInstance(image))
            }
        }
    }
    override fun onResume() {
        super.onResume()
        if (!cameraHandled) {
            picker.cameraFile().delete()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        cameraHandled = false
    }

    private fun openGallerySaveProject() {
        if (!prefs.hasToken()) {
            showToast("Доступно только для зарегистрированных пользователей")
        } else {
            replaceFragment(SavedProjectsFragment())
        }
    }

    private fun openDialog() {
        dialog = CommonDialog(requireContext())
        dialog.setMessage("Вы действительно хотите выйти из профиля?")
        dialog.setPositiveButton("Да") {
            prefs.removeToken()
            prefs.removeUserId()
            prefs.removeUserRole()
            replaceFragment(AuthFragment())
        }
        dialog.setNegativeButton("Нет") {}
        dialog.show()
    }

    private fun getRotation(inputStream: InputStream): Int {
        var rotate = 0
        try {
            val orientation: Int = ExifInterface(inputStream)
                .getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
            rotate = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                else -> 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rotate
    }
    private fun rotate(bitmap: Bitmap, degrees: Int): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(degrees.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }
}