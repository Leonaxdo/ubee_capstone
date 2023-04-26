package com.example.ubee

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ubee.databinding.ActivityAnnounceBinding
import com.example.ubee.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)

        setContentView(binding.root)
    }
}