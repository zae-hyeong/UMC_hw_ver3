package com.example.flo

import android.util.Log
import android.view.View
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthService { //여기 안에서 레트로핏 호출/응답 관리

    private lateinit var signUpView: SignUpView
    private lateinit var loginView: LoginView

    fun setSignUpView(signUpView : SignUpView) {
        this.signUpView = signUpView
    }

    fun setLoginView(loginView : LoginView) {
        this.loginView = loginView
    }

    //이제 여기에서 회원가입 관리
    fun signUp(user : User) {
//        레트로 핏 객체 생성
        val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202").
            addConverterFactory(GsonConverterFactory.create()).build()//맨뒤 요청의 슬래쉬는 SignUpService 부분에 붙어있음!

//        생성된 객채를 사용해서 서비스를 서비스 객체로 생성
        val authService = retrofit.create(AuthRetrofitInterface::class.java)

        signUpView.onSignUpLoading() // 로딩중

        //SignUp service에서 Call<> 을 선언헀기 때문에 enqueue라는 메소드를 사용해서 서버의 리턴값을 받아서 처리할 수 있음!
       authService.signUp(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) { //서버의 응답을 여기서 다시 받아 처리
                //데이터를 받고(보고) 처리

                Log.d("SIGNUPACT/API-RESPONSE", response.toString())

                //우리 서버가 보내준 데이터 파싱을 위해
                val resp = response.body()!! //서버에서 보내준 정보

                Log.d("SIGNUPACT/API-ASDF", resp.toString())
                when(resp.code) { //만들어진 서버의 응답 코드
                    1000 -> signUpView.onSignUpSuccess() //finish() //로그인 성공 -> 회원가입 액티비티 종료료
                    else -> signUpView.onSignUpFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                //통신 실패 -> 에러 건네줌
                Log.d("SIGNUPACT/API-ERROR", t.toString())
                signUpView.onSignUpFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun login(user : User) {
//        레트로 핏 객체 생성
        val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202").
        addConverterFactory(GsonConverterFactory.create()).build() //맨뒤 요청의 슬래쉬는 SignUpService 부분에 붙어있음!
//        생성된 객채를 사용해서 서비스를 서비스 객체로 생성
        val authService = retrofit.create(AuthRetrofitInterface::class.java)

        loginView.onLoginLoading() // 로딩중

        //SignUp service에서 Call<> 을 선언헀기 때문에 enqueue라는 메소드를 사용해서 서버의 리턴값을 받아서 처리할 수 있음!
        authService.login(user).enqueue(object : Callback<AuthResponse> {
            override fun onResponse(call: Call<AuthResponse>, response: Response<AuthResponse>) { //서버의 응답을 여기서 다시 받아 처리
                //데이터를 받고(보고) 처리

                Log.d("LOGINACT/API-RESPONSE", response.toString())

                //우리 서버가 보내준 데이터 파싱을 위해
                val resp = response.body()!!

                when(resp.code) { //만들어진 서버의 응답 코드
                    1000 -> loginView.onLoginSuccess(resp.result!!) //finish() //로그인 성공
                    else -> loginView.onLoginFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                //통신 실패 -> 에러 건네줌
                Log.d("LOGINACT/API-ERROR", t.toString())
                signUpView.onSignUpFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }
}