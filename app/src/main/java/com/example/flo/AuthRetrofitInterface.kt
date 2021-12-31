package com.example.flo

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRetrofitInterface {
    @POST("/users")
    fun signUp(@Body user : User) : Call<AuthResponse> //retropy call로 가서 AuthResponse와 파싱해줌
    //데이터를 Json으로 변경해서 보냄

    @POST("/users/login")
    fun login(@Body user : User) : Call<AuthResponse>
}