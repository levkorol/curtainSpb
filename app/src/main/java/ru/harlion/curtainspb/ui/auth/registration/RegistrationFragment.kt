package ru.harlion.curtainspb.ui.auth.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_registration.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.harlion.curtainspb.R
import ru.harlion.curtainspb.models.data.UsersRequest
import ru.harlion.curtainspb.models.data.UsersResponse
import ru.harlion.curtainspb.repo.data.DataRepository


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

    private fun registerUsers() {
        DataRepository.registerUser(
            request = UsersRequest(
                name = "Elon Musk",
                phone = "123",
                email = "elon@gmail.com",
                password = "password"
            ), callback = object : Callback<UsersResponse> {
                override fun onResponse(
                    call: Call<UsersResponse>,
                    response: Response<UsersResponse>
                ) {
                    if (response.isSuccessful) {
                        // TODO пользователь создан, отправляем запрос
                    }
                }

                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    // TODO отобразить ошибку (внутри t - подробности)
                }
            }
        )
    }
}