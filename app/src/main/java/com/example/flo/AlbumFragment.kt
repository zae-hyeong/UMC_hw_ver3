package com.example.flo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.flo.databinding.FragmentAlbumBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {

    lateinit var binding: FragmentAlbumBinding
    private var gson: Gson = Gson()

    val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked : Boolean = false

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        val albumData = arguments?.getString("album")
        val album = gson.fromJson(albumData, Album::class.java)

        isLiked = isLikedAlbum(album.id)

        setViews(album)

        //ROOM_DB
        val songs = getSongs(album.id) //앨범안에 있는 수록곡들을 불러옵니다.
        // 이 다음에 수록곡 프래그먼트에 songs을 전달해주는 식으로 사용하시면 됩니다.

        setClickListners(album)

        //set click listener
        binding.albumBackIv.setOnClickListener {
            (context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, HomeFragment())
                    .commitAllowingStateLoss()
        }

        //init viewpager
        val albumAdapter = AlbumVPAdapter(this)

        binding.albumContentVp.adapter = albumAdapter
        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp) {
            tab, position ->
            tab.text = information[position]
        }.attach()

        return binding.root
    }

    private fun setViews(album: Album) {
        binding.albumMusicTitleTv.text = album.title.toString()
        binding.albumSingerNameTv.text = album.singer.toString()
        binding.albumAlbumIv.setImageResource(album.coverImg!!)

        if(isLiked) {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
        }
        else {
            binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }

    }


    /*----------추가 함수 영역 ----------*/

    //ROOM_DB
    private fun getSongs(albumIdx: Int): ArrayList<Song>{
        val songDB = SongDatabase.getInstance(requireContext())!!

        val songs = songDB.songDao().getSongsInAlbum(albumIdx) as ArrayList

        return songs
    }

    private fun likeAlbum(userId:Int, albumId : Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        //like 된 앨범을 DB에 정보 저장
        songDB.albumDao().likeAlbum((like))

    }

    private fun setClickListners(album : Album) {
        val userId : Int = getUserIdx(requireContext())

        binding.albumLikeIv.setOnClickListener {
            if(isLiked) { //좋아요 취소
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(userId, album.id)
            }
            else {  //좋아요 설정
                binding.albumLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likeAlbum(userId, album.id)
            }
        }
    }

    private fun isLikedAlbum(albumId:Int) : Boolean {
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getUserIdx(requireContext())

        val likeId : Int? =songDB.albumDao().islikeAlbum(userId, albumId)   //like id 받아옴

        return likeId != null //like가 안돼있으면 null 가져옴 -> null이 아니면(like에 있으면) true 리턴
    }

    private fun disLikedAlbum(userId : Int, albumId:Int) {
        val songDB = SongDatabase.getInstance(requireContext())!!

        songDB.albumDao().disLikeAlbum(userId, albumId)   //like id 받아옴
        //취소만 하기 때문에 리턴값은 없음
    }

    //jwt를 사용해서 유저 정보를 가져옴
//    private fun getUserIdx() : Int {
//        //액티비티끼리가 아니라 프레그먼트에서 사용하는 sharedPreference
//        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
//
//        return spf!!.getInt("jwt", 0)
//
//    }
}