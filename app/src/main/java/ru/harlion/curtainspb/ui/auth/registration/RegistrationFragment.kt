package ru.harlion.curtainspb.ui.auth.registration

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.harlion.curtainspb.base.BaseFragment
import ru.harlion.curtainspb.databinding.FragmentRegistrationBinding
import ru.harlion.curtainspb.ui.main_menu.MainMenuFragment
import ru.harlion.curtainspb.utils.replaceFragment

class RegistrationFragment : BaseFragment() {

    private val viewModel: RegistrationViewModel by viewModels()
    private lateinit var binding: FragmentRegistrationBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()

        initViewModel()
    }

    private fun initViewModel() {
        viewModel.isRegistrationComplete.observe(viewLifecycleOwner) {
            if (it) {
                showToast("Пользователь успешно зарегистрирован")
                replaceFragment(MainMenuFragment())
            }
        }
    }

    private fun initClicks() {

        binding.registrationLoginButton.setOnClickListener {

            val name = binding.fragmentRegistrationName.text.toString()
            val email = binding.fragmentRegistrationEmail.text.toString()
            val password = binding.fragmentRegistrationPassword.text.toString()
            val phone = binding.fragmentRegistrationPhone.text.toString()

            viewModel.registerUsers(
                requireContext(),
                name = name,
                phone = phone,
                email = email,
                password = password
            )
        }

        binding.fRegistrationBack.setOnClickListener { parentFragmentManager.popBackStack() }

        binding.fragmentRegistrationEmail.addTextChangedListener(watcher)
        binding.fragmentRegistrationName.addTextChangedListener(watcher)
        binding.fragmentRegistrationPassword.addTextChangedListener(watcher)
        binding.fragmentRegistrationPhone.addTextChangedListener(watcher)
        binding.fragmentRegistrationSwitch.setOnCheckedChangeListener { _, _ -> updateButton() }
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            updateButton()
        }
    }

    private fun updateButton() {
        binding.registrationLoginButton.isEnabled =
            binding.fragmentRegistrationSwitch.isChecked &&
                    binding.fragmentRegistrationEmail.text.isNotBlank() &&
                    binding.fragmentRegistrationName.text.isNotBlank() &&
                    binding.fragmentRegistrationPassword.text!!.isNotBlank() &&
                    binding.fragmentRegistrationPhone.text.isNotBlank()
    }
}
