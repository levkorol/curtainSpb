package ru.harlion.curtainspb.repo.data

import android.content.Context
import android.os.Handler
import android.os.Looper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.harlion.curtainspb.models.data.AuthData
import ru.harlion.curtainspb.models.data.AuthRequest
import ru.harlion.curtainspb.models.data.UsersRequest
import java.io.File
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

object DataRepository {
    lateinit var context: Context
    private val service: DataServiceApi

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(BODY) })
            .addNetworkInterceptor {
                val token = context.getSharedPreferences("user", Context.MODE_PRIVATE).getString("accessToken", null)
                it.proceed(
                    if (token.isNullOrBlank()) it.request()
                    else it.request().newBuilder().header("Authorization", "Bearer $token").build()
                )
            }
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://pzntech.ru:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(DataServiceApi::class.java)
    }

    fun registerUser(
        request: UsersRequest,
        success: (AuthData) -> Unit,
        error: (Throwable) -> Unit
    ): Future<*> {
        val task = FutureTask {
            val resp = try {
                service.registerUser(request).execute().body()!!
                service.auth(AuthRequest(request.email, request.password)).execute().body()!!.data
            } catch (e: Exception) {
                e
            }
            if (!Thread.currentThread().isInterrupted) {
                Handler(Looper.getMainLooper()).post {
                    if (resp is AuthData) success(resp) else error(resp as Throwable)
                }
            }
        }
        Thread(task).start()
        return task
    }

    fun sendPhoto(file: File) {
        service.sendProjectImage(
            context.getSharedPreferences("user", Context.MODE_PRIVATE).getInt("userId", -1),
            MultipartBody.Part.createFormData("image", file.name, file.asRequestBody("image/*".toMediaType()))
        ).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {

            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }
}