package com.example.ubee

import android.content.Intent
import android.os.Build.VERSION_CODES.M
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.example.ubee.databinding.ActivitySignupBinding
import androidx.navigation.fragment.findNavController
import com.example.ubee.MyApplication.Companion.auth
import com.example.ubee.MyApplication.Companion.email
import com.example.ubee.databinding.ActivityLoginBinding
import com.example.ubee.databinding.FragmentHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //여기서부터 수정

        binding.goSignInBtn.setOnClickListener {
            val email = binding.authEmailEditView.text.toString()
            val password = binding.authPasswordEditView.text.toString()
            MyApplication.auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    if(task.isSuccessful){
                        saveStore()
                        MyApplication.auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { sendTask ->
                                if(sendTask.isSuccessful){
                                    Toast.makeText(baseContext, "회원가입에서 성공, 전송된 메일을 확인해 주세요", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                }else {
                                    Toast.makeText(baseContext, "메일 발송 실패", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                    }else {
                        Toast.makeText(baseContext, "회원가입 실패", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                    }
                }
            }

        // 툴바 세팅 (https://hamzzibari.tistory.com/102)
        val toolbar = binding.toolbarMap
        setSupportActionBar(toolbar)
        val ab = supportActionBar!!
        ab.setDisplayShowTitleEnabled(false)
        ab.setDisplayHomeAsUpEnabled(true)



    }


    // 뒤로가기 동작 (https://hamzzibari.tistory.com/102)
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun saveStore(){
        val data = mapOf(
            "email" to binding.authEmailEditView.text.toString(),
            "name" to binding.editTextTextPersonName.text.toString(),
            "identityNum" to binding.editTextTextPersonName3.text.toString()
        )

        MyApplication.db.collection("userData")
            .document(binding.editTextTextPersonName.text.toString())
            .set(data)
    }
}
