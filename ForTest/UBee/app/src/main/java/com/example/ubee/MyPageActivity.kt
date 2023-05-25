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
        // name 데이터 받아온 후 마이페이지에 표시하기
        val nameText = findViewById<TextView>(R.id.userNameText)
        val emailText = findViewById<TextView>(R.id.emailText)
        val nameText2 = findViewById<TextView>(R.id.userNameText2)
        val phoneNum = findViewById<TextView>(R.id.phoneNumberText)
        val borrow = findViewById<TextView>(R.id.rentalText)
        db.collection("userData")
            .get()
            .addOnCompleteListener { task ->
                if(task.isSuccessful){
                    for(i in task.result!!){
                        if(i.id == uid){
                            val userName = i.data["name"].toString()
                            val phoneNumber = i.data["phoneNum"].toString()
                            val borrowNumber = i.data["borrow"].toString()
                            nameText.setText(userName)
                            nameText2.setText(userName)
                            phoneNum.setText(phoneNumber)
                            emailText.setText(uid)
                            if(borrowNumber == "1"){
                                borrow.setText("미대여중")
                            }else if (borrowNumber == "2"){
                                borrow.setText("대여중")
                            }
                        }
                    }
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