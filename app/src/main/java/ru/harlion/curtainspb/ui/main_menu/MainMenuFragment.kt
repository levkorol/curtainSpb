package ru.harlion.curtainspb.ui.main_menu

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.vansuita.pickimage.IntentResolver
import ru.harlion.curtainspb.base.BaseFragment
import ru.harlion.curtainspb.base.CommonDialog
import ru.harlion.curtainspb.databinding.FragmentMainMenuBinding
import ru.harlion.curtainspb.repo.AuthPrefs
import ru.harlion.curtainspb.ui.auth.authorization.AuthFragment
import ru.harlion.curtainspb.ui.auth.registration.RegistrationFragment
import ru.harlion.curtainspb.ui.sketch.SketchFragment
import ru.harlion.curtainspb.utils.replaceFragment


class MainMenuFragment : BaseFragment() {

    private lateinit var prefs: AuthPrefs
    private lateinit var dialog: CommonDialog

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
        if (!prefs.hasToken()) {
            showToast("Доступно только для зарегистрированных пользователей")
        } else {
            //todo
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
}