package ru.harlion.curtainspb.repo.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import androidx.annotation.CheckResult
import com.google.gson.Gson
import com.google.gson.JsonObject
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level.BODY
import okio.IOException
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.harlion.curtainspb.models.data.*
import java.io.Closeable
import java.io.File
import java.util.concurrent.ExecutionException
import java.util.concurrent.FutureTask

@SuppressLint("StaticFieldLeak")
object DataRepository {
    lateinit var context: Context
    private val userPrefs: SharedPreferences
        get() = context.getSharedPreferences("user", Context.MODE_PRIVATE)
    private val service: DataServiceApi

    private val gson = Gson()
    init {
        val client = OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { setLevel(BODY) })
            .addNetworkInterceptor {
                val token = userPrefs.getString("accessToken", null)
                it.proceed(
                    if (token.isNullOrBlank()) it.request()
                    else it.request().newBuilder().header("Authorization", "Bearer $token").build()
                )
            }
            .build()

        val retrofit = Retrofit.Builder()
            .client(client)
            //.baseUrl("http://pzntech.ru:8080/api/v1/") //todo https://api.pzntech.ru/api/v1/
            .baseUrl("http://pzntech.ru:8080/api/v1/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        service = retrofit.create(DataServiceApi::class.java)
    }

    fun registerUser(
        request: UsersRequest,
        success: (AuthData) -> Unit,
        error: (Throwable) -> Unit
    ): Closeable {
        val task = object : FutureTask<AuthData>({
            service.registerUser(request).execute().body()!!
            service.auth(AuthRequest(request.email, request.password)).execute().body()!!.data
        }) {
            override fun done() {
                if (!isCancelled) {
                    Handler(Looper.getMainLooper()).post {
                        if (!isCancelled) {
                            try {
                                val resp = get()
                                success(resp)
                            } catch (e: ExecutionException) {
                                error(e.cause ?: e)
                            }
                        }
                    }
                }
            }
        }
        Thread(task).start()
        return Closeable { task.cancel(true) }
    }

    fun sendPhoto(file: File) {
        service.sendProjectImage(
            userPrefs.getInt("userId", -1),
            MultipartBody.Part.createFormData(
                "image",
                file.name,
                file.asRequestBody("image/*".toMediaType())
            )
        ).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {}

            override fun onFailure(call: Call<Unit>, t: Throwable) {
            }
        })
    }

    @CheckResult fun authUser(
        email: String,
        password: String,
        success: (AuthData) -> Unit,
        error: (Throwable) -> Unit
    ): Closeable {
        val call = service.auth(AuthRequest(email, password))
        call.enqueue(object : Callback<Resp<AuthData>> {
            override fun onResponse(call: Call<Resp<AuthData>>, response: Response<Resp<AuthData>>) {
                if (response.isSuccessful) {
                    success(response.body()!!.data)
                } else {
                    onFailure(call, IOException("HTTP ${response.code()}"))
                }
            }

            override fun onFailure(call: Call<Resp<AuthData>>, t: Throwable) {
                error(t)
            }
        })
        return Closeable(call::cancel)
    }

    @CheckResult fun templates(
        success: (List<Template>) -> Unit,
        error: (Throwable) -> Unit,
    ): Closeable {
        val call = service.getTemplates(userPrefs.getInt("userId", -1).takeIf { it >= 0 })
        call.enqueue(object : Callback<Resp<List<Template>>> {
            override fun onResponse(call: Call<Resp<List<Template>>>, response: Response<Resp<List<Template>>>) {
                if (response.isSuccessful) success(response.body()!!.data)
                else onFailure(call, IOException("HTTP ${response.code()}"))
            }
            override fun onFailure(call: Call<Resp<List<Template>>>, t: Throwable) {
                error(t)
            }
        })
        return Closeable(call::cancel)
    }


    @CheckResult fun request(
        image: File,
        name: String,
        phone: String,
        email: String,
        width: String,
        height: String,
        comment: String,
        success: () -> Unit,
        errorMessage: (String) -> Unit,
        error: (Throwable) -> Unit,
    ): Closeable =
        service.sendRequest(
            MultipartBody.Part.createFormData("image", image.name, image.asRequestBody("image/*".toMediaType())),
            name, phone, email, width, height, comment
        ).enqueue(
            { _ -> success() },
            {
                val message = gson.fromJson(it.charStream(), JsonObject::class.java)
                    .getAsJsonPrimitive("message").asString
                errorMessage(message)
            },
            error,
        )

    @CheckResult fun recover(
        email: String,
        success: (String) -> Unit,
        errorMessage: (String) -> Unit,
        error: (Throwable) -> Unit,
    ): Closeable =
        service.passwordRecovery(RecoveryRequest(email)).enqueue(
            { success(it.message) },
            {
                val message = gson.fromJson(it.charStream(), JsonObject::class.java)
                    .getAsJsonPrimitive("message").asString
                errorMessage(message)
            },
            error,
        )

    fun <T> Call<T>.enqueue(success: (T) -> Unit, httpError: (ResponseBody) -> Unit, error: (Throwable) -> Unit): Closeable {
        enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) success(response.body()!!)
                else try { httpError(response.errorBody()!!) } catch (e: Exception) { error(e) }
            }
            override fun onFailure(call: Call<T>, t: Throwable) {
                error(t)
            }
        })
        return Closeable(::cancel)
    }
}