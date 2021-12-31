package com.example.flo

//뷰 인터페이스 : 액티비티랑 서비스 클래스랑 연결시켜놓은 인터페이스
//로그인시 처리 내용을 설명하는 함수들
interface SignUpView {
    fun onSignUpLoading()
    fun onSignUpSuccess()
    fun onSignUpFailure(code : Int, message:String)
}