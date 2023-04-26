package com.example.ubee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ubee.adapter.EventRecyclerAdapter
import com.example.ubee.databinding.ActivityEventBinding

class EventActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEventBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_event)

        binding = ActivityEventBinding.inflate(layoutInflater)

        // 툴바 상단 왼쪽 뒤로가기 버튼 생성 및 동작 처리.
        binding.toolbarEvent.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.toolbarEvent.setNavigationOnClickListener {
            finish()
        }

        binding.eventRecycler.adapter = EventRecyclerAdapter()

        setContentView(binding.root)
    }
}