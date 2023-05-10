package com.example.ubee

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ubee.databinding.ActivityCaptureBinding
import com.journeyapps.barcodescanner.CaptureManager
import com.journeyapps.barcodescanner.DecoratedBarcodeView

class CaptureActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCaptureBinding
    private lateinit var captureManager: CaptureManager
    private lateinit var decoratedBarcodeView: DecoratedBarcodeView
    private var isFlash : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capture)
        binding = ActivityCaptureBinding.inflate(layoutInflater)

        setContentView(binding.root)

        // 뒤로가기 버튼 생성 및 뒤로가기 버튼 눌렀을 때 처리
        binding.toolbarQr.setNavigationIcon(R.drawable.ic_back_arrow)
        binding.toolbarQr.setNavigationOnClickListener {
            finish()
        }


        // 카메라 권한
        decoratedBarcodeView = findViewById<DecoratedBarcodeView>(R.id.customBarcodeView)
        val cameraPermission = ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
        if(cameraPermission == PackageManager.PERMISSION_GRANTED) {
            captureManager = CaptureManager(this, decoratedBarcodeView)
            captureManager.initializeFromIntent(intent, savedInstanceState)
            captureManager.decode()
        }
        // 카메라 권한이 없는 경우 다시 요청
        else {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA), 99)
        }

        // 밑의 손전등 버튼 컨트롤
        val lampBtn = binding.lampBtn
        lampBtn.setOnClickListener{
            if(!isFlash) {
                lampBtn.isSelected = true
                isFlash = true
                decoratedBarcodeView.setTorchOn()
            } else {
                lampBtn.isSelected = false
                isFlash = false
                decoratedBarcodeView.setTorchOff()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        captureManager.onResume()
    }

    override fun onPause() {
        super.onPause()
        captureManager.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        captureManager.onDestroy()
    }

    // onSaveInstanceState ? 또한 처리해주어야 한다.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        captureManager.onSaveInstanceState(outState)
    }

    // 카메라 권한을 요청할 수 있기 때문에
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        captureManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
