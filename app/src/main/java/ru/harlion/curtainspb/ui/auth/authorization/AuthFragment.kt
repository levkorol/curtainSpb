package ru.harlion.curtainspb.ui.auth.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import ru.harlion.curtainspb.databinding.FragmentAuthBinding
import ru.harlion.curtainspb.ui.auth.password_recovery.PasswordRecoveryFragment
import ru.harlion.curtainspb.ui.auth.registration.RegistrationFragment
import ru.harlion.curtainspb.ui.auth.user_arreement.fragment.UserAgreementFragment
import ru.harlion.curtainspb.ui.main_menu.MainMenuFragment
import ru.harlion.curtainspb.utils.replaceFragment

class AuthFragment : Fragment() {

    private lateinit var binding: FragmentAuthBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()

        initClicks()
    }

    private fun initViewModel() {
        viewModel.isAuthComplete.observe(viewLifecycleOwner) {
            if (it) {
                replaceFragment(MainMenuFragment())
            }
        }
    }

    private fun initClicks() {
        binding.forgetPasswordFragment.setOnClickListener {
            replaceFragment(PasswordRecoveryFragment())
        }

        binding.registration.setOnClickListener {
            replaceFragment(RegistrationFragment())
        }

        binding.userArreement.setOnClickListener {
            replaceFragment(UserAgreementFragment())
        }

        binding.mainMenu.setOnClickListener {
            replaceFragment(MainMenuFragment())
        }

        binding.buttonLoginAuth.setOnClickListener {
            val login = binding.loginInput.text.toString()
            val password = binding.passwordAuth.text.toString()

            viewModel.authUser(login, password)
        }
    }
}