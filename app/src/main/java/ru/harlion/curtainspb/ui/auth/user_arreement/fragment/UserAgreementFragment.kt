package ru.harlion.curtainspb.ui.auth.user_arreement.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.databinding.FragmentUserAgreementBinding


class UserAgreementFragment : Fragment() {

    private lateinit var binding: FragmentUserAgreementBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserAgreementBinding.inflate(layoutInflater)
        binding.agreement.text = resources.openRawResource(R.raw.user_agreement).reader().readText()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initClicks()
    }

    private fun initClicks() {
        binding.fUserAgreementBack.setOnClickListener { parentFragmentManager.popBackStack() }
    }
}