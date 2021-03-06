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
    fun getTemplates(
        @Query("userId") id: Int?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): Call<Resp<List<Template>>>

    @GET("templates/foruser/userId={userId}")
    fun getTemplatesForUser(
        @Path("userId") id: Int?,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): Call<Resp<List<Template>>>

    @GET("users/{userId}/images")
    fun getSavedProjects(@Path("userId") userId: Int): Call<Resp<List<SavedProject>>>

    @POST("users/{userId}/images")
    @Multipart
    fun sendProjectImage(
        @Path(value = "userId") userId: Int,
        @Part image: MultipartBody.Part
    ): Call<Unit>

    // @FormUrlEncoded
    @POST("requests")
    @Multipart
    fun sendRequest(
        @Part image: MultipartBody.Part,
        @Part("name") name: String,
        @Part("userId") userId: Int,
        @Part("phone") phone: String,
        @Part("email") email: String,
        @Part("width") width: String,
        @Part("height") height: String,
        @Part("comments") comments: String,
    ): Call<Unit>

    @POST("requests/phone")
    fun requestPhone(@Body requestPhone: RequestPhoneBody): Call<Unit>

    @POST("requests/reset")
    fun passwordRecovery(@Body request: RecoveryRequest): Call<MessageResponse>

    @GET("users/{userId}")
    fun getUserProfile(@Path(value = "userId") userId: Int): Call<Resp<UsersResponse>>
}