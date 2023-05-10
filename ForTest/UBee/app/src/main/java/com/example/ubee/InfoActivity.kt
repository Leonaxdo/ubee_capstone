package com.example.ubee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ubee.databinding.ActivityInfoBinding

class InfoActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        binding = ActivityInfoBinding.inflate(layoutInflater)

        // 툴바 상단 왼쪽 뒤로가기 버튼 생성 및 동작 처리.
        binding.toolbarMap.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.toolbarMap.setNavigationOnClickListener {
            finish()
        }

        setContentView(binding.root)
    }
}