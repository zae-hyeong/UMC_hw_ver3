package com.example.flo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

fun saveJwt(context : Context, jwt : String) {
    //getSharedPreference는 액티비티 권한 가진애가 호출 가능하기 때문에, 어디서든 이거 쓸라면 context 가져와야 함
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putString("jwt", jwt)
    editor.apply()
}

fun getJwt(context : Context) : String {
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

    return spf.getString("jwt", "")!!
}

fun saveUserIdx(context: Context, userIdx : Int) {
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
    val editor = spf.edit()

    editor.putInt("userIdx", userIdx)
    editor.apply()
}

fun getUserIdx(context : Context) : Int {
    val spf = context.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)

    return spf.getInt("userIdx", 0)
}