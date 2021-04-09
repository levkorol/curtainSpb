package ru.harlion.curtainspb.ui.auth.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_registration.*
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.models.data.UsersRequest
import ru.harlion.curtainspb.repo.data.DataRepository
import java.util.concurrent.Future


class RegistrationFragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loginView.setOnClickListener {
            registerUsers()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        currentTask?.let {
            it.cancel(true)
            currentTask = null
        }
    }

    private var currentTask: Future<*>? = null
    private fun registerUsers() {
        currentTask = DataRepository.registerUser(
            request = UsersRequest(
                name = "Elon Musk",
                phone = "123",
                email = "elon@gmail.com",
                password = "password"
            ),
            {},
            Throwable::printStackTrace
        )
    }
}