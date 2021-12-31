package com.example.flo

import com.google.gson.annotations.SerializedName

//SerializedName하면 이름이 달라도 알아서 파싱해줌
data class Auth (
    @SerializedName("userIdx")val userIdx : Int,
    @SerializedName("jwt") val jwt : String
)

data class AuthResponse(
    @SerializedName("isSuccess") val isSuccess : Boolean,
    @SerializedName("code") val code : Int,
    @SerializedName("message") val message : String,
    @SerializedName("result") val result : Auth?
)
