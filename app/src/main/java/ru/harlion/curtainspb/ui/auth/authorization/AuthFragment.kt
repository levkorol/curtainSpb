package ru.harlion.curtainspb.ui.auth.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.harlion.curtainspb.base.BaseFragment
import ru.harlion.curtainspb.databinding.FragmentAuthBinding
import ru.harlion.curtainspb.ui.auth.password_recovery.PasswordRecoveryFragment
import ru.harlion.curtainspb.ui.auth.registration.RegistrationFragment
import ru.harlion.curtainspb.ui.auth.user_arreement.fragment.UserAgreementFragment
import ru.harlion.curtainspb.ui.main_menu.MainMenuFragment
import ru.harlion.curtainspb.utils.replaceFragment

class AuthFragment : BaseFragment() {

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
                replaceFragment(MainMenuFragment(), false)
            }
        }

        viewModel.isAuthFail.observe(viewLifecycleOwner) {
            if (it) {
                showToast("Ошибка авторизации")
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

            if (login.isEmpty() || password.isEmpty()) {
                showToast("Логин или пароль не могут быть пустыми")
            } else {
                viewModel.authUser(login, password)
            }
        }
    }
}