package ru.harlion.curtainspb.ui.request_cost

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.harlion.curtainspb.databinding.FragmentRequestCostBinding


class RequestCostFragment : Fragment() {

    private lateinit var binding: FragmentRequestCostBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRequestCostBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()
    }

    private fun initClicks() {

        binding.cvBack.setOnClickListener { parentFragmentManager.popBackStack() }

        binding.fRequestCostInputName.addTextChangedListener(watcher)
        binding.fRequestCostInputPhone.addTextChangedListener(watcher)
        binding.fRequestCostInputWidth.addTextChangedListener(watcher)
        binding.fRequestCostInputEmail.addTextChangedListener(watcher)
        binding.fRequestCostInputHeight.addTextChangedListener(watcher)
    }

    private val watcher = object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(p0: Editable?) {
            binding.fRequestCostButtonSend.isEnabled =
                binding.fRequestCostInputName.text.isNotBlank()
                        && binding.fRequestCostInputPhone.text.isNotBlank()
                        && binding.fRequestCostInputWidth.text.isNotBlank()
                        && binding.fRequestCostInputEmail.text.isNotBlank()
                        && binding.fRequestCostInputHeight.text.isNotBlank()
        }
    }
}