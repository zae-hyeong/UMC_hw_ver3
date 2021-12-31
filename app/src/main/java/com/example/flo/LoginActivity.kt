package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flo.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity(), LoginView {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.loginSignupTv.setOnClickListener {
            startActivity(Intent(this, LoginSignupActivity::class.java))
        }

        binding.loginDownIv.setOnClickListener {
            finish()
        }

        binding.logoLoginBtn.setOnClickListener {
            login()
            //startMainActivity()
        }
    }

    private fun login() {
        //validate 처리 : 안채워진 칸이나 잘못된거 있는지 확인

        if (binding.loginIdEt.text.toString().isEmpty() || binding.loginEmailEt.text.toString().isEmpty()) {
            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
            return
        }
        if (binding.loginPwEt.text.toString().isEmpty()) {
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val email: String = binding.loginIdEt.text.toString() + "@" + binding.loginEmailEt.text.toString()
        val pw: String = binding.loginPwEt.text.toString()
        val user  = User(email, pw, "")

//        //DB에 있는지 확인인
//        val songDB = SongDatabase.getInstance(this)!!
//        val user = songDB.userDao().getUser(email, pw)
//
//        //user가 null이 아닐때 처리하는 let문
//       user?.let{
//            Log.d("LOGINACT/GET_USER", "userId : ${user.id}, $user")
//
//           //발급받은 jwt를 저장해주는 함수
//           saveJwt(user.id)
//      }

        val authService = AuthService()
        authService.setLoginView(this)

        authService.login(user)

        //Toast.makeText(this, "회원정보가 없습니다.", Toast.LENGTH_SHORT).show()
    }

    private fun saveJwt(jwt : Int) {
        val spf = getSharedPreferences("auth", MODE_PRIVATE)
        val editor = spf.edit()

        editor.putInt("jwt", jwt)
        editor.apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    override fun onLoginLoading() {
        binding.loginLodingSb.visibility = View.VISIBLE
    }

    override fun onLoginSuccess(auth: Auth) {
        binding.loginLodingSb.visibility = View.GONE

        saveJwt(this, auth.jwt)
        saveUserIdx(this, auth.userIdx) // 원래는 유저 인덱스는 프라이버시라 이렇게 가져오가나 하면 해킹의 위험이 있는데 그냥 가져옴

        startMainActivity()
    }

    override fun onLoginFailure(code: Int, message: String) {
        binding.loginLodingSb.visibility = View.GONE

        when(code) {
            2015, 2019, 3014 -> {
                binding.loginPwErrorTv.visibility = View.VISIBLE
                binding.loginPwErrorTv.text = message
            }
        }
    }
}