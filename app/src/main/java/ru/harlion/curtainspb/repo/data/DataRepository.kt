package ru.harlion.curtainspb.repo.data

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BASIC
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.harlion.curtainspb.models.data.UsersRequest
import ru.harlion.curtainspb.models.data.UsersResponse

object DataRepository {

    private val service: DataService;

    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(BASIC) })
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl("http://pzntech.ru:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service = retrofit.create(DataService::class.java)
    }

    fun registerUser(request: UsersRequest, callback: Callback<UsersResponse>) =
        service.registerUser(request).enqueue(callback)
}