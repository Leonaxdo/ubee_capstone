package com.example.ubee

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.ubee.adapter.HomeViewPagerAdapter
import com.example.ubee.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    // 스캐너 설정
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        // result : 스캔된 결과

        // 내용이 없다면
        if (result.contents == null) {
            Toast.makeText(context, "Cancelled", Toast.LENGTH_LONG).show()
        }
        else { // 내용이 있다면

            // 1. Toast 메시지 출력.
            Toast.makeText(
                context,
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)

        // qr 스캔 버튼 클릭 했을 떄 처리 (home -> qr 스캔 페이지)
        binding.qrBtn.setOnClickListener {
            val CamOptions = ScanOptions()
            CamOptions.setOrientationLocked(false) // 세로, 가로모드 모두 지원
            CamOptions.setBeepEnabled(false)   // 스캔 시 삑 소리
            CamOptions.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            CamOptions.captureActivity = CaptureActivity::class.java
            barcodeLauncher.launch(CamOptions)
        }


        // 우산 찾기 버튼 클릭 처리
        binding.mapImgBtn.setOnClickListener {
            val intent = Intent(context, MapActivity::class.java)
            startActivity(intent)
        }


        // 이용방법 버튼 클릭 처리
        binding.howUseBtn.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToInfoFragment()
            findNavController().navigate(action)
        }

        binding.announceBtn.setOnClickListener {
            val intent = Intent(context, AnnounceActivity::class.java)
            startActivity(intent)
        }


        // 홈 화면의 이벤트 리스트
        val viewPager2 = binding.eventViewPager
        val tabLayout = binding.viewpagerTap

        viewPager2.adapter = HomeViewPagerAdapter()
        TabLayoutMediator(tabLayout, viewPager2) {
            tab, position ->
        }.attach()
        binding.eventViewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val act = activity as MainActivity
        act.supportActionBar?.show()
    }
}