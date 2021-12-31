package com.example.flo

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding
import com.example.flo.databinding.ActivityLoginSignupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginSignupActivity : AppCompatActivity(), SignUpView {

    lateinit var binding : ActivityLoginSignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signupDownIv.setOnClickListener {
            finish()
        }

        binding.signupSignupBtn.setOnClickListener {
            signUp()
            //finish()
        }
    }

    private fun getUser() : User {
        val email : String = binding.signupIdEt.text.toString() + "@" + binding.signupEmailEt.text.toString()
        val pw : String = binding.signupPwEt.text.toString()
        val name : String = binding.signupNameEt.text.toString()

        return User(email, pw, name)
    }

    //validate 처리 : 안채워진 칸이나 잘못된거 있는지 확인
    private fun signUp() {
        if(binding.signupIdEt.text.toString().isEmpty() || binding.signupEmailEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signupNameEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if(binding.signupPwEt.text.toString() != binding.signupPwCheckEt.text.toString()) {
            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
            return
        }

//        val userDB = SongDatabase.getInstance(this)!!
//        userDB.userDao().insert(getUser())
//
//        Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show()
//
//        //저장됐는지 확인
//        val users = userDB.userDao().getUsers()
//        Log.d("SIGNUP", users.toString())
//        이 부분을 네트워킹으로 바꿔줄 예정

        //레트로 핏 객체 생성
//        val retrofit = Retrofit.Builder().baseUrl("http://13.125.121.202").
//            addConverterFactory(GsonConverterFactory.create()).build()//맨뒤 요청의 슬래쉬는 SignUpService 부분에 붙어있음!

        //생성된 객채를 사용해서 서비스를 서비스 객체로 생성
//        val signUpService = retrofit.create(SignUpService::class.java)

        val authService = AuthService()
        authService.setSignUpView(this)

        //입력받은 유저부분만 authService(로그인 처리를 담당하는 서비스 클래스)로 보냄
        //응답에 따른 결과 처리(뷰 랜더링)는 인터페이스를 통해 아래 오버라이드 function으로 구현함
        authService.signUp(getUser()) //로그인 응답 처리 부분

        Log.d("SIGNUPACT/ASYNC", "Hello")
    }

    override fun onSignUpLoading() {
        binding.signupLodingSb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess() {
        binding.signupLodingSb.visibility = View.GONE

        finish() //성공하면 회원가입 액티비티티 종료
   }

    override fun onSignUpFailure(code: Int, message: String) {
        binding.signupLodingSb.visibility = View.GONE

        when(code) {
            2016, 2017 -> {
                binding.signupEmailWarningTv.text = message
                binding.signupEmailWarningTv.visibility = View.VISIBLE
            }
        }
    }
}