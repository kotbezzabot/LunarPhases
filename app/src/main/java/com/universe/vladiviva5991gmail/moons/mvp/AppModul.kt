package com.universe.vladiviva5991gmail.moons.mvp

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.universe.vladiviva5991gmail.moons.BuildConfig
import com.universe.vladiviva5991gmail.moons.data.net.RestApi
import com.universe.vladiviva5991gmail.moons.data.net.RestService
import com.universe.vladiviva5991gmail.moons.data.repository.MoonRepositoryImpl
import com.universe.vladiviva5991gmail.moons.domain.executor.PostExecutionThread
import com.universe.vladiviva5991gmail.moons.domain.repository.MoonRepository
import com.universe.vladiviva5991gmail.moons.mvp.executor.UIThread
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModul constructor(private val context: Context) {
    @Provides
    @Singleton
    fun getContext(): Context = context

    @Provides
    @Singleton
    fun getUIThread(): PostExecutionThread = UIThread()

    @Provides
    @Singleton
    fun getMoonRepository(restService: RestService): MoonRepository = MoonRepositoryImpl(restService)

    @Provides
    @Singleton
    fun getRetrofit(okHttpClient: OkHttpClient, gson: Gson)
            : Retrofit = Retrofit.Builder()
            .baseUrl("http://api.burningsoul.in/")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()


    @Provides
    @Singleton
    fun getRestApi(retrofit: Retrofit): RestApi = retrofit.create(RestApi::class.java)


    @Provides
    @Singleton
    fun getGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun getOkHttp(): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()


        builder.readTimeout(5, TimeUnit.SECONDS) //время ожидания чего-то там
                .writeTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(5, TimeUnit.SECONDS)

        //.addInterceptor()//выполнить некие модификации?
        if (BuildConfig.DEBUG) {
            val httpLogging: HttpLoggingInterceptor = HttpLoggingInterceptor()
            httpLogging.setLevel(HttpLoggingInterceptor.Level.BODY)
            builder.addInterceptor(httpLogging)
        }

        return builder.build()

    }
}