package com.example.freightflow.network

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RouteApiService {

    @POST("api/routes/createRoute")
    fun createRoute(@Body routeRequest: RouteRequest): Call<ResponseBody>
}