package ru.harlion.curtainspb.repo.data

import android.os.Handler
import android.os.Looper
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.harlion.curtainspb.models.data.AuthRequest
import ru.harlion.curtainspb.models.data.UsersRequest
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

object DataRepository {

    private val service: DataService;

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(BODY) })
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://pzntech.ru:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(DataService::class.java)
    }

    fun registerUser(request: UsersRequest, success: (String) -> Unit, error: (Throwable) -> Unit): Future<*> {
        val task = FutureTask {
            val resp = try {
                service.registerUser(request).execute().body()!!
                service.auth(AuthRequest(request.email, request.password)).execute().body()!!.data.accessToken
            } catch (e: Exception) {
                e
            }
            if (!Thread.currentThread().isInterrupted) {
                Handler(Looper.getMainLooper()).post {
                    if (resp is String) success(resp) else error(resp as Throwable)
                }
            }
        }
        Thread(task).start()
        return task
    }
}