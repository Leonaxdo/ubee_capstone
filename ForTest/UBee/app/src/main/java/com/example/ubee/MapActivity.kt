package com.example.ubee

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ubee.data.mapMarkerData
import com.example.ubee.databinding.ActivityMapBinding
import com.example.ubee.model.mapMarkerModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.Overlay
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.MarkerIcons
import com.sothree.slidinguppanel.SlidingUpPanelLayout


class MapActivity : AppCompatActivity(), OnMapReadyCallback {
    private val mapView: MapView by lazy {
        findViewById(R.id.navermap_map_view)
    }

    companion object {
        lateinit var naverMap: NaverMap
    }

    // 스캐너 설정
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        // result : 스캔된 결과

        // 내용이 없다면
        if (result.contents == null) {
            Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
        } else { // 내용이 있다면

            // 1. Toast 메시지 출력.
            Toast.makeText(
                this,
                "Scanned: " + result.contents,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private lateinit var binding: ActivityMapBinding
    private var select: Boolean = false

    // 권한 가져오기
    var permissions = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        // 권한이 있다면 지도 표기.
        if (isPermitted()) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_map)

            binding = ActivityMapBinding.inflate(layoutInflater)

            setContentView(binding.root)
            mapView.onCreate(savedInstanceState)
            mapView.getMapAsync(this)

            // 툴바 상단 왼쪽 뒤로가기 버튼 생성 및 동작 처리.
            binding.toolbarMap.setNavigationIcon(R.drawable.ic_back_arrow)
            binding.toolbarMap.setNavigationOnClickListener {
                finish()
            }

            // 현재 위치로 이동 버튼
            binding.goCurBtn.setOnClickListener {
                fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
                setUpdateLocationListner()
            }

            // 대여하기 버튼을 클릭 했을 때 처리 -> QrScanner 이동
            binding.ubBorrowBtn.setOnClickListener {
                if (!select) {
                    Toast.makeText(this.applicationContext, "우산 대여 지역을 선택해주세요", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    val options = ScanOptions()
                    options.setOrientationLocked(false) // 세로, 가로모드 모두 지원
                    options.setBeepEnabled(false)   // 스캔 시 삑 소리
                    options.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
                    options.captureActivity = CaptureActivity::class.java
                    barcodeLauncher.launch(options)
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, permissions, 99)
        }
    }

    // 위치 허용 권한 여부 설정
    fun isPermitted(): Boolean {
        for (p in permissions) {
            if (ContextCompat.checkSelfPermission(this, p) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onMapReady(naverMap: NaverMap) {

        MapActivity.naverMap = naverMap

        // 단국대학교 죽전캠퍼스 위치로 초기 위치 지정. (위도, 경도, 줌 레벨)
        val initializeCam = CameraPosition(
            LatLng(37.32027694161876, 127.12684476172711),
            15.5
        )
        naverMap.cameraPosition = initializeCam
        setMarker(
            37.322125955030074,
            127.1268153294535,
            3,
            "단국대 범정관",
            "학교 건물",
            "경기도 용인시 수지구 죽전로 152",
            "이용시간 미정",
            "X"
        )
        setMarker(
            37.322846257322354,
            127.1251918807539,
            10,
            "1319",
            "카페",
            "경기도 용인시 수지구 죽전로 144번길 15-14",
            "월~금: 10 ~ 22시, 토: 11 ~ 21시, 일: 11 ~ 22시",
            "031-889-1319"
        )
        setMarker(
            37.32325893183231,
            127.12476383350179,
            18,
            "옹고집",
            "음식점",
            "경기도 용인시 수지구 죽전동 1323-7",
            "이용시간 미정",
            "031-261-9289"
        )
        setMarker(
            37.3248727671386,
            127.10770712182837,
            13,
            "죽전역 2번출구",
            "지하철 역",
            "경기도 용인시 수지구 포은대로 536",
            "24시간",
            "X"
        )
        setMarker(
            37.32015759845343,
            127.11308752599354,
            4,
            "보정동 행정 복지 센터",
            "공공시설",
            "경기도 용인시 기흥구 죽전로 40",
            "평일: 9 ~ 18시",
            "X"
        )
    }


    // 우산개수 표기 아이콘 생성 함수
    fun setMarker(
        lat: Double,
        lng: Double,
        ubCnt: Int,
        storeName: String,
        storeType: String,
        address: String,
        time: String,
        contact: String
    ) {
        val marker = Marker()
        marker.isIconPerspectiveEnabled
        marker.position = LatLng(lat, lng)
        marker.map = naverMap

        // 우산 개수마다 색 정리
        if (ubCnt < 5) {
            marker.icon = OverlayImage.fromResource(R.drawable.ic_red_ub)
        } else if (ubCnt < 15) {
            marker.icon = OverlayImage.fromResource(R.drawable.ic_normal_ub)
        } else {
            marker.icon = OverlayImage.fromResource(R.drawable.ic_blue_ub)
        }

        // 마커를 클릭했을때 이벤트 처리.
        marker.setOnClickListener(Overlay.OnClickListener {
            select = true
            binding.ubBorrowBtn.setImageResource(R.drawable.btn_borrow_ub)

            binding.mainFrame.panelState = SlidingUpPanelLayout.PanelState.ANCHORED
            binding.ubPlaceName.text = storeName
            binding.ubSortPlace.text = storeType
            binding.ubAddressInfo.text = address
            binding.ubTimeInfo.text = time
            binding.ubContactInfo.text = contact

            if (ubCnt < 5) {
                binding.ubStatusText.text = "우산이 5개 미만으로 남았습니다."
                binding.ubStatusText.setTextColor(Color.RED)
            } else if (ubCnt < 15) {
                binding.ubStatusText.text = "우산이 15개 미만으로 남았습니다."
                binding.ubStatusText.setTextColor(Color.rgb(0, 204, 204))
            } else {
                binding.ubStatusText.text = "우산이 15개 이상 남았습니다."
                binding.ubStatusText.setTextColor(Color.BLUE)
            }

            false
        })
    }

    // 현재 위치로 이동
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient // 자동으로 gps값 수신
    lateinit var locationCallback: LocationCallback //gps 응답 값

    @SuppressLint("MissingPermission")
    fun setUpdateLocationListner() {
        val locationRequest = com.google.android.gms.location.LocationRequest.create()
        locationRequest.run {
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
            // interval = 1000 // 1초에 한번씩 GPS 요청 - 1초에 한 번씩 위치 업데이트 -> 계속 사용자 위치 따라다니면서 사용하려면 주석 해제
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for ((i, location) in locationResult.locations.withIndex()) {
                    Log.d("location: ", "${location.latitude}, ${location.longitude}")
                    setLastLocation(location)
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    fun setLastLocation(location: Location) {
        val myLocation = LatLng(location.latitude, location.longitude)
        val marker = Marker()
        marker.position = myLocation
        marker.icon = MarkerIcons.BLUE
        marker.width = 70
        marker.height = 100
        marker.map = naverMap

        val cameraUpdate = CameraUpdate.scrollTo(myLocation)
        naverMap.moveCamera(cameraUpdate)
    }


    // mapview 생명주기 관리
    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}