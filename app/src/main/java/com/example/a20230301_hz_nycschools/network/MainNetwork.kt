package com.example.a20230301_hz_nycschools.network

import com.example.a20230301_hz_nycschools.network.response.SATResult
import com.example.a20230301_hz_nycschools.network.response.School
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://data.cityofnewyork.us/resource/"

private val service: MainNetwork by lazy {

    var loggingInterceptor =  HttpLoggingInterceptor().also {
        it.level = HttpLoggingInterceptor.Level.BODY
    }

    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    retrofit.create(MainNetwork::class.java)
}

fun getNetworkService() = service

/**
 * Main network interface which will fetch a new welcome title for us
 */
interface MainNetwork {
    @GET("s3k6-pzi2.json")
    suspend fun fetchAllSchoolList(): List<School>

    @GET("f9bf-2cp4.json")
    suspend fun fetchSATResults(): List<SATResult>

}