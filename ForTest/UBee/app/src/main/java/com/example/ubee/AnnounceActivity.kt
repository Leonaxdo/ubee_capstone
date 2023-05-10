package com.example.ubee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ubee.databinding.ActivityAnnounceBinding

class AnnounceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAnnounceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_announce)

        binding = ActivityAnnounceBinding.inflate(layoutInflater)

        // 툴바 상단 왼쪽 뒤로가기 버튼 생성 및 동작 처리.
        binding.toolbarMap.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.toolbarMap.setNavigationOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }
}