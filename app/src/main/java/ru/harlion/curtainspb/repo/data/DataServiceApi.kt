package ru.harlion.curtainspb.repo.data

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import ru.harlion.curtainspb.models.data.*

interface DataServiceApi {
    @POST("users")
    fun registerUser(@Body request: UsersRequest): Call<UsersResponse>

    @POST("auth")
    fun auth(@Body request: AuthRequest): Call<Resp<AuthData>>

    @GET("templates")
    fun getTemplates(@Query("userId") id: Int?): Call<Resp<List<Template>>>

    @POST("users/{userId}/images") @Multipart
    fun sendProjectImage(@Path(value = "userId") userId: Int, @Part image: MultipartBody.Part): Call<Unit>

    // @FormUrlEncoded
    @POST("requests") @Multipart
    fun sendRequest(
        @Part image: MultipartBody.Part,
        @Part("name") name: String,
        @Part("phone") phone: String,
        @Part("email") email: String,
        @Part("weight") width: String,
        @Part("height") height: String,
        @Part("comment") comment: String,
    ): Call<Unit>

    @POST("requests/reset")
    fun passwordRecovery()
}