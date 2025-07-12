package android.example.balesuk.data.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitInstance {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.99.144:8000/") // For Android emulator
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    val api: ApiService = retrofit.create(ApiService::class.java)
}
