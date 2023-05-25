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
    private var result_uv: String? = "-1"
    private var result_rain: String? = "-1"

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 현재 시간 처리
        val current = LocalDateTime.now()
        val formatter_uv = DateTimeFormatter.ofPattern("yyyyMMddHH")
        val formatter_rain_date = DateTimeFormatter.ofPattern("yyyyMMdd")
        val formatter_rain_time = DateTimeFormatter.ofPattern("HHmm")
        val uv_time = current.format(formatter_uv)
        val rain_data = current.format(formatter_rain_date)
        var rain_time = current.format(formatter_rain_time)

        // 기상청 제공 api 호출은 정각으로부터 40분 지난 시점에 가능함 (그에 대한 처리)
        if(rain_time.toInt() % 100 < 40) {
            val past = LocalDateTime.now().minusHours(1)
            rain_time = past.format(formatter_rain_time)
        }


        // 자외선 처리
        retrofitWork(uv_time)

        // 강수량 처리
        retrofitWorkR(rain_data, rain_time)
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

        // 메인 화면 날짜 적용
        val current = LocalDateTime.now()
        val formatter_date = DateTimeFormatter.ofPattern("MM월 dd일")
        val home_date = current.format(formatter_date)
        binding.nowDate.text = "TODAY ($home_date) 까지"

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
        service.getUvData("${R.string.uv_key}", "JSON", "1100000000", time)
            .enqueue(object : Callback<UvResponse> {
                override fun onResponse(
                    call: Call<UvResponse>,
                    response: retrofit2.Response<UvResponse>
                ) {
                    if (response.isSuccessful) {
//                        Log.d("TAG", response.body().toString())
//                        Log.d("pasakResult_uv", "result_uv")
//                        Log.d("pasakTime_uv", time)
                        result_uv = response.body()?.response?.body?.items?.item?.get(0)?.h0
                        binding.uvNum.text = "$result_uv" // 자외선 출력
                        binding.uvNum.setTextColor(Color.RED) // UV지수 색상 지정

                        //UV 아이콘 색상 지정
                        if((result_uv?.toInt() ?: 0) < 3) { // 낮음
                            binding.uvIcon.setColorFilter(parseColor("#FF54B1FF"))
                        }else if((result_uv?.toInt() ?: 0) < 6) { // 보통
                            binding.uvIcon.setColorFilter(parseColor("#FFFAD98F"))
                        }else if((result_uv?.toInt() ?: 0) < 8) { // 높음
                            binding.uvIcon.setColorFilter(parseColor("#FFFD8D3C"))
                        }else if((result_uv?.toInt() ?: 0) < 11) { // 매우높음
                            binding.uvIcon.setColorFilter(parseColor("#FFC30000"))
                        }else { // 위험
                            binding.uvIcon.setColorFilter(parseColor("#FF54248E"))
                        }
                    }
                }

                override fun onFailure(call: Call<UvResponse>, t:  Throwable) {
                    Log.d("UV API Get Fail", t.message.toString())
                }
            })
    }

    // 강수량 api retrofit work
    private fun retrofitWorkR(date: String, time: String) {
        val service = RetrofitApiR.rainService

        // RetrofitApi의 형식대로 값 넘기기
        service.getRainData("${R.string.rain_key}",
                            "1", "1000", "JSON", date, time, "63", "125")
            .enqueue(object : Callback<RainResponse> {
                override fun onResponse(
                    call: Call<RainResponse>,
                    response: retrofit2.Response<RainResponse>
                ) {
                    if (response.isSuccessful) {
//                        Log.d("TAG", response.body().toString())
                        result_rain = response.body()?.response?.body?.items?.item?.get(2)?.obsrValue ?: "0"
//                        Log.d("pasakResult_rain", "$result_rain")
//                        Log.d("pasakDate_rain", date)
//                        Log.d("pasakTime_rain", time)

                        binding.rainNum.text = "$result_rain" // 강수량 출력
                        binding.rainNum.setTextColor(Color.BLUE) // 강수량 색상 지정

                        //강수량 아이콘 지정
                        if((result_rain?.toInt() ?: 0) <= 0) { // 비 안옴
                            binding.rainIcon.setImageResource(R.drawable.rain_icon_sun)
                        }else if((result_rain?.toInt() ?: 0) < 3) { // 약한비
                            binding.rainIcon.setImageResource(R.drawable.rain_icon_1)
                        }else if((result_rain?.toInt() ?: 0) < 15) { // 보통비
                            binding.rainIcon.setImageResource(R.drawable.rain_icon_2)
                        }else if((result_rain?.toInt() ?: 0) < 30) { // 강한비
                            binding.rainIcon.setImageResource(R.drawable.rain_icon_3)
                        }else { // 매우 강한비
                            binding.rainIcon.setImageResource(R.drawable.rain_icon_4)
                        }
                    }
                }

                override fun onFailure(call: Call<RainResponse>, t:  Throwable) {
                    Log.d("RAIN API Get Fail", t.message.toString())
                }
            })
    }

}