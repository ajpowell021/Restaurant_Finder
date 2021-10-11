package com.adam.restaurant_finder

import android.content.Context
import com.adam.restaurant_finder.api.PlacesService
import com.adam.restaurant_finder.api.RemoteDataSource
import com.adam.restaurant_finder.api.RetrofitPlacesDataSource
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import okhttp3.logging.HttpLoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.android.support.AndroidSupportInjectionModule
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module(
    includes = [
        AndroidSupportInjectionModule::class
    ]
)
class AppModule {

    @Provides
    @Singleton
    fun appContext(app: App): Context = app

    @Provides
    fun provideClient(): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BASIC
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/")
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun providesGson(): Gson {
        return GsonBuilder()
            .serializeNulls()
            .setPrettyPrinting()
            .create()
    }

    @Provides
    fun getRemoteDataSource(service: PlacesService): RemoteDataSource {
        return RetrofitPlacesDataSource(service)
    }

    @Provides
    fun getPlacesService(retrofit: Retrofit): PlacesService {
        return retrofit.create(PlacesService::class.java)
    }

    @Provides
    fun getPicasso(context: Context): Picasso {
        return Picasso.get()
    }
}
