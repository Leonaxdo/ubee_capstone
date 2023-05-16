package com.example.ubee

import android.content.DialogInterface
import android.content.Intent
import android.media.metrics.Event
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.example.ubee.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import kotlin.math.E

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener{

    private lateinit var binding: ActivityMainBinding
    lateinit var navigationView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Setup view binding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup action bar - Replace title with app icon
        setSupportActionBar(binding.mainToolbar.toolbar)

        navigationView = findViewById(R.id.main_nav_view)
        navigationView.setNavigationItemSelectedListener(this)

        // Inactivate dark mode
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        // 로그인 버튼 클릭시 (https://ddolcat.tistory.com/274)
        var headerView = binding.mainNavView.getHeaderView(0)
        var btn = headerView.findViewById<TextView>(R.id.draw_login)
        btn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(
            R.menu.menu_toolbar, menu
        )

        return true
    }

    // 메뉴 아이템 버튼이 눌렸을 때 처리
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            // 메뉴 버튼이 눌렸을 때 옆 draw 메뉴가 나오도록 함.
            R.id.menu_select -> {
                binding.mainDrawable.openDrawer(GravityCompat.END)
                super.onOptionsItemSelected(item)
            }
            else ->
                super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_report -> {
                val intent = Intent(this, AnnounceActivity::class.java)
                startActivity(intent)
            }

            R.id.item_notice -> {
                val builder = android.app.AlertDialog.Builder(this)

                builder.setTitle("우비 고객 센터")
                    .setMessage(" 02) XXX - XXXX ")
                    .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, id ->
                        })
                // 다이얼로그를 띄워주기
                builder.show()
            }

            R.id.item_event -> {
                val intent = Intent(this, EventActivity::class.java)
                startActivity(intent)
            }
        }
        return false
    }
}