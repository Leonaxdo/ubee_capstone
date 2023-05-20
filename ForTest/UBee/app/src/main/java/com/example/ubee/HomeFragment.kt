package com.example.ubee

import android.content.Intent
import android.graphics.Color
import android.graphics.Color.parseColor
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.ubee.adapter.HomeViewPagerAdapter
import com.example.ubee.databinding.FragmentHomeBinding
import com.google.android.material.tabs.TabLayoutMediator
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import retrofit2.Call
import retrofit2.Callback
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private var result: String? = "-1"

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

    @RequiresApi(Build.VERSION_CODES.O)
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

        // 현재 시간 처리
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyyMMddHH")
        val formatted = current.format(formatter)

        // 자외선 처리 (수정이 필요: 왜인지 모르겠지만 onCreateView에 넣었을 때 이 함수가 2번 호출되어 api가 두번 호출된다)
        retrofitWork(formatted)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val act = activity as MainActivity
        act.supportActionBar?.show()
    }

    // 자외선 api retrofit work
    private fun retrofitWork(time: String) {
        val service = RetrofitApi.uvService

        // RetrofitApi의 형식대로 값 넘기기
        service.getEmgMedData("nGo5x8Xykd2+xqgTIOehhXd4Xf+0pLcBs5dlL8bhawf5TIS5DLCCEF6qF54BtVg4A5tYFQDnO45qdtsHRL9Qvg==", "JSON", "1100000000", time)
            .enqueue(object : Callback<EmgMedResponse> {
                override fun onResponse(
                    call: Call<EmgMedResponse>,
                    response: retrofit2.Response<EmgMedResponse>
                ) {
                    if (response.isSuccessful) {
//                        Log.d("TAG", response.body().toString())
//                        Log.d("pasak", "$result")
//                        Log.d("pasakTime", time)
                        result = response.body()?.response?.body?.items?.item?.get(0)?.h0
                        binding.uvNum.text = "$result" // 자외선 출력
                        binding.uvNum.setTextColor(Color.RED) // UV지수 색상 지정

                        //UV 아이콘 색상 지정
                        if((result?.toInt() ?: 0) < 3) { // 낮음
                            binding.uvIcon.setColorFilter(parseColor("#FF54B1FF"))
                        }else if((result?.toInt() ?: 0) < 6) { // 보통
                            binding.uvIcon.setColorFilter(parseColor("#FFFAD98F"))
                        }else if((result?.toInt() ?: 0) < 8) { // 높음
                            binding.uvIcon.setColorFilter(parseColor("#FFFD8D3C"))
                        }else if((result?.toInt() ?: 0) < 11) { // 매우높음
                            binding.uvIcon.setColorFilter(parseColor("#FFC30000"))
                        }else { // 위험
                            binding.uvIcon.setColorFilter(parseColor("#FF54248E"))
                        }
                    }
                }

                override fun onFailure(call: Call<EmgMedResponse>, t:  Throwable) {
                    Log.d("UV API Get Fail", t.message.toString())
                }
            })
    }
}