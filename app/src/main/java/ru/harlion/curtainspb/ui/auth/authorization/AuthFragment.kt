package ru.harlion.curtainspb.ui.auth.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_auth.*
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.ui.auth.password_recovery.PasswordRecoveryFragment
import ru.harlion.curtainspb.ui.auth.registration.RegistrationFragment
import ru.harlion.curtainspb.ui.auth.user_arreement.UserAgreementFragment
import ru.harlion.curtainspb.ui.main_menu.MainMenuFragment
import ru.harlion.curtainspb.utils.replaceFragment


class AuthFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        forget_password_fragment.setOnClickListener {
            replaceFragment(PasswordRecoveryFragment())
        }

        registration.setOnClickListener {
            replaceFragment(RegistrationFragment())
        }

        user_arreement.setOnClickListener {
            replaceFragment(UserAgreementFragment())
        }

        main_menu.setOnClickListener {
            replaceFragment(MainMenuFragment())
        }
    }
}