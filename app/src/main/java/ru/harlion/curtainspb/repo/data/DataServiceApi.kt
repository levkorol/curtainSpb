package ru.harlion.curtainspb.repo.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import ru.harlion.curtainspb.models.data.*

interface DataServiceApi {
    @POST("users")
    fun registerUser(@Body request: UsersRequest): Call<UsersResponse>

    @POST("auth")
    fun auth(@Body request: AuthRequest): Call<Resp<AuthData>>

    @GET("templates?userId=id")
    fun getTemplates(@Path(value = "id") id: Int)

    @POST("users/:userId/images")
    fun sendProjectImage(@Path(value = "userId") userId: Int)

    @POST("requests")
    fun sendRequest()

    @POST("requests/reset")
    fun passwordRecovery()
}