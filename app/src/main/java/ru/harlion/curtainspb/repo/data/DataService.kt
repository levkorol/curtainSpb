package ru.harlion.curtainspb.repo.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import ru.harlion.curtainspb.models.data.*

interface DataService {
    @POST("users")
    fun registerUser(@Body request: UsersRequest): Call<UsersResponse>

    @POST("auth")
    fun auth(@Body request: AuthRequest): Call<Resp<AuthData>>

}