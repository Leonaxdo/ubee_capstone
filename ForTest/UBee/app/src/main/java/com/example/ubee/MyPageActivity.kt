package com.example.ubee

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.MenuItem
import android.widget.TextView
import com.example.ubee.MyApplication.Companion.db
import com.example.ubee.MyApplication.Companion.email
import com.example.ubee.databinding.ActivityMainBinding
import com.example.ubee.databinding.ActivityMyPageBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MyPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyPageBinding
    val db = Firebase.firestore
    var uid = "null"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyPageBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // 사용자 이메일 정보 받아오기
        uid = intent.getStringExtra("uid").toString()
        // name 데이터 받아온 후 이름 칸에 표시 하기
        val nameText = findViewById<TextView>(R.id.userNameText)
        db.collection("userData")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    for(i in task.result!!){
                        if(i.id == uid){
                            val userName = i.data["name"].toString()
                            nameText.setText(userName)
                        }
                    }
                }
            }
        // email 데이터 받아온 후 이메일 칸에 표시 하기
        val emailText = findViewById<TextView>(R.id.emailText)
        db.collection("userData")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    emailText.setText(uid)
                }
            }


        //툴바 셋팅
        val toolbar = binding.toolbarMap
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        ab.setDisplayHomeAsUpEnabled(true)
    }

    //뒤로가기 동작
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