package com.example.flo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//왜 이렇게 파일을 따로 나누냐면
//const val BASE_URL = "http~~"
//const val DEVELOP_URL = "http~~"
//이렇게 개발서버냐, 실제서버냐, 임시 서버냐 등 여러 서버를 사용하는 상황에서는 이렇게 만들어야 유동적으로 네트워크를 설정할 수 있음
fun getRetrofit() : Retrofit{
    val retrofit = Retrofit.Builder()
            .baseUrl("http://13.125.121.202")
            .addConverterFactory(GsonConverterFactory.create())     //데이터 객체화 해줌
            .build()

    return retrofit
}