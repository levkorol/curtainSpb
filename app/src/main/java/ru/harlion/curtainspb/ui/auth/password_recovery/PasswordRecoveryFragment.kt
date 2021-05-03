package ru.harlion.curtainspb.ui.auth.password_recovery

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.harlion.curtainspb.databinding.FragmentPasswordRecoveryBinding

class PasswordRecoveryFragment : Fragment() {

    private lateinit var binding: FragmentPasswordRecoveryBinding

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
    }

    private fun initClicks() {
        binding.fPasswordRecoveryBack.setOnClickListener { parentFragmentManager.popBackStack() }

        binding.fPasswordButtonRecovery.setOnClickListener {
            TODO("viewModel")
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