package com.jasakarya.data.source.remote

import com.jasakarya.data.response.Responses
import retrofit2.Response
import retrofit2.http.*

interface ApiServices {
    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") fullName: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<Responses>

    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<Responses>
}