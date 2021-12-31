package com.example.flo

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentLockerBinding
import com.google.android.material.tabs.TabLayoutMediator


class LockerFragment : Fragment() {

    lateinit var binding: FragmentLockerBinding
    private val information = arrayListOf("저장한 곡", "음악파일")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockerBinding.inflate(inflater, container, false)

        val lockerAdapter = LockerViewpagerAdapter(this)
        binding.lockerContentVp.adapter = lockerAdapter
        TabLayoutMediator(binding.lockerContentTb, binding.lockerContentVp) {
            tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        initView()   //뷰 초기화
    }

    private fun initView() {
        //로그인을 못하면 jwt에 들어있는 테이블이 비어있어 id가 0이 될것임
        val jwt = getUserIdx(requireContext())

        if(jwt == 0) { //로그인이 안된경우
            binding.lockerLoginTv.text = "로그인"
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, LoginActivity::class.java))
            }
        }

        else { //로그인이 된 경우
            binding.lockerLoginTv.text = "로그아웃"

            //로그아웃 해주는 함수
            logout()
            binding.lockerLoginTv.setOnClickListener {
                startActivity(Intent(activity, MainActivity::class.java))
            }
        }
    }

    //Jwt를 가져오는 함수(로그인이 되었는지 확인하기 위해)
//    private fun getUserIdx() : Int {
//        //액티비티끼리가 아니라 프레그먼트에서 사용하는 sharedPreference
//        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
//
//        return spf!!.getInt("jwt", 0)
//    }

    private fun logout() {
        //sharedPreference 값을 0으로 바꿔주면 됨 -> jwt
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        val editor = spf!!.edit()

        editor.remove("jwt")
        editor.apply()
    }

}