package com.truefinch.enceladus.repository.server

import com.google.gson.GsonBuilder
import com.truefinch.enceladus.utils.jsonConvert.DurationJsonConvert
import com.truefinch.enceladus.utils.jsonConvert.ZoneIdJsonConvert
import com.truefinch.enceladus.utils.jsonConvert.ZonedDateTimeJsonConvert
import io.reactivex.schedulers.Schedulers
import okhttp3.Headers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.time.Duration
import java.time.ZoneId
import java.time.ZonedDateTime

class Server {
    private val baseUrl = "http://planner.skillmasters.ga/"
    internal lateinit var api: PlannerApi

    init {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor { chain ->
                //                val token = InjectApplication.inject
//                    .getSharedPreferences(USER_ID_TOKEN_PREF, Context.MODE_PRIVATE)
//                    .getString(USER_ID_TOKEN, "")
                val token = "serega_mem"
                val originalRequest = chain.request()
//                Log.d("OkHttp", "X-Firebase-Auth: $token")
//                Log.d("OkHttp", "User id: ${getCurrentFirebaseUser().uid}")
                val headers = Headers.Builder()
                    .add("X-Firebase-Auth", token ?: "")
                    .build()

                val newRequest = originalRequest.newBuilder()
                    .headers(headers)
                    .build()

                chain.proceed(newRequest)
            }
            .addInterceptor { chain ->
                val request = chain.request()
                val response = chain.proceed(request)

                when (response.code()) {
                    204 -> throw NoContent(response.message())
                    400 -> throw BadRequest(response.message())
                    401 -> throw NotAuthorized()
                    403 -> throw AccessDenied()
                    404 -> throw NotFind()
                    500 -> throw InternalError()
                }
                response
            }
            .build()

        val rxAdapter = RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io())

        val json = GsonBuilder()
            .registerTypeAdapter(ZonedDateTime::class.java, ZonedDateTimeJsonConvert())
            .registerTypeAdapter(Duration::class.java, DurationJsonConvert())
            .registerTypeAdapter(ZoneId::class.java, ZoneIdJsonConvert())
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(json))
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(rxAdapter)
            .build()

        api = retrofit.create(PlannerApi::class.java)
    }
}
