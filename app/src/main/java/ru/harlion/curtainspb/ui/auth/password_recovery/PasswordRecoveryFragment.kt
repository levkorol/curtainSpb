package ru.harlion.curtainspb.ui.auth.password_recovery

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ru.harlion.curtainspb.base.BaseFragment
import ru.harlion.curtainspb.databinding.FragmentPasswordRecoveryBinding
import ru.harlion.curtainspb.ui.auth.authorization.AuthFragment
import ru.harlion.curtainspb.utils.replaceFragment

class PasswordRecoveryFragment : BaseFragment() {

    private lateinit var binding: FragmentPasswordRecoveryBinding

    private val viewModel: PasswordRecoveryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPasswordRecoveryBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()

        initViewModel()
    }

    private fun initViewModel() {
        viewModel.isError.observe(viewLifecycleOwner) {
            if (it == true) {
                showToast("Что-то пошло не так. Попробуйте снова")
            }
        }

        viewModel.isSuccess.observe(viewLifecycleOwner) {
            if (it == true) {
                showToast("Отлично! Мы уже отправили вам ссылку для восстановления пароля!")
                replaceFragment(AuthFragment())
            }
        }
    }

    private fun initClicks() {
        binding.fPasswordRecoveryBack.setOnClickListener { parentFragmentManager.popBackStack() }

        binding.fPasswordButtonRecovery.setOnClickListener {
            val email = binding.fPasswordRecoveryEmail.text.toString()
            viewModel.passwordRecovery(email)
        }

        binding.fPasswordRecoveryEmail.addTextChangedListener(watcher)
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            binding.fPasswordButtonRecovery.isEnabled =
                binding.fPasswordRecoveryEmail.text.isNotBlank()
        }
    }
}