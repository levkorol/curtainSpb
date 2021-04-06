package ru.harlion.curtainspb.repo.data

import androidx.lifecycle.LiveData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.harlion.curtainspb.models.data.UsersRequest
import ru.harlion.curtainspb.models.data.UsersResponse

interface DataService {
    @POST("users")
    fun registerUser(@Body request: UsersRequest): Call<UsersResponse>
}