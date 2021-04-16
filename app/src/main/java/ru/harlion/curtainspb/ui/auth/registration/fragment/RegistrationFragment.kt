package ru.harlion.curtainspb.ui.auth.registration.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.harlion.curtainspb.databinding.FragmentRegistrationBinding
import ru.harlion.curtainspb.ui.auth.registration.viewmodel.RegistrationViewModel


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

    }

    private fun initClicks() {

        binding.registrationLoginButton.setOnClickListener {

            val name = binding.fragmentRegistrationName.text.toString()
            val email = binding.fragmentRegistrationEmail.text.toString()
            val password = binding.fragmentRegistrationPassword.toString()
            val phone = binding.fragmentRegistrationPhone.toString()

            viewModel.registerUsers(name, email, password, phone)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.currentTask?.let {
            it.cancel(true)
            viewModel.currentTask = null
        }
    }
}