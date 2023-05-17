package com.example.ubee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import com.example.ubee.databinding.ActivityPasswordSearchBinding

class PasswordSearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_search)
        val binding = ActivityPasswordSearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sendToEmail.setOnClickListener {
            val email = binding.authEmailEditView.text.toString()
            if(email.length != 0){
                MyApplication.auth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(this) { task ->
                        if(task.isSuccessful){
                            Toast.makeText(this, "전송 성공", Toast.LENGTH_LONG).show()
                            val intent = Intent(this, LoginActivity::class.java)
                            startActivity(intent)
                        }else {
                            Toast.makeText(this, "전송 실패", Toast.LENGTH_LONG).show()
                        }
                    }
            }else {
                Toast.makeText(this, "메일을 정확히 입력해 주세요.", Toast.LENGTH_LONG).show()
            }
        }

        // 툴바 세팅
        val toolbar = binding.toolbarMap
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        ab.setDisplayHomeAsUpEnabled(true)

    }

    // 뒤로가기 동작
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}