package ru.harlion.curtainspb.ui.request_cost

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import ru.harlion.curtainspb.databinding.FragmentRequestCostBinding
import ru.harlion.curtainspb.repo.data.DataRepository
import ru.harlion.curtainspb.ui.main_menu.MainMenuFragment
import ru.harlion.curtainspb.utils.replaceFragment
import java.io.Closeable
import java.io.File


class RequestCostFragment : Fragment() {

    private lateinit var binding: FragmentRequestCostBinding
    private var currentRequest: Closeable? = null

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

        binding.fRequestCostButtonSend.setOnClickListener {
            currentRequest?.close()
            currentRequest = DataRepository.request(
                File(File(requireActivity().filesDir, "upload"), "pick.png"),
                binding.fRequestCostInputName.text.toString(),
                binding.fRequestCostInputPhone.text.toString(),
                binding.fRequestCostInputEmail.text.toString(),
                binding.fRequestCostInputWidth.text.toString(),
                binding.fRequestCostInputHeight.text.toString(),
                binding.fRequestCostInputComment.text.toString(),
                {
                    Toast.makeText(
                        requireContext(),
                        "Ваша заявка успешно отправлена",
                        Toast.LENGTH_LONG
                    ).show()
                    replaceFragment(MainMenuFragment())
                },
                { message ->
                    Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                },
                Throwable::printStackTrace,
            )
        }
    }

    override fun onDestroyView() {
        currentRequest?.let {
            it.close()
            currentRequest = null
        }
        super.onDestroyView()
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