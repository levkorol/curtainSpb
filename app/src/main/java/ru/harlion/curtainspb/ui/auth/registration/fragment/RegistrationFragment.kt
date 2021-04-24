package ru.harlion.curtainspb.ui.auth.registration.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.harlion.curtainspb.databinding.FragmentRegistrationBinding
import ru.harlion.curtainspb.ui.auth.registration.viewmodel.RegistrationViewModel
import ru.harlion.curtainspb.ui.main_menu.fragment.MainMenuFragment
import ru.harlion.curtainspb.utils.replaceFragment


class RegistrationFragment : Fragment() {

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
    }
}
