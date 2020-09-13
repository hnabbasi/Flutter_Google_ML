package com.intelliabb.flutter_google_ml

import android.content.Intent
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel

class MainActivity: FlutterActivity() {

    private lateinit var result: MethodChannel.Result

    override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        super.configureFlutterEngine(flutterEngine)

        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, MAIN_CHANNEL).setMethodCallHandler  {
            call, result ->

            this.result = result

            when (call.method) {
                "getScannedText" -> getScannedText()
                "getScannedBarcode" -> getScannedBarcode()
                "getScannedFace" -> getScannedFace()
                "getScannedObject" -> getScannedObject()
                else -> result.notImplemented()
            }
        }
    }

    private fun getScannedText() {
        val intent = Intent(this, ScanActivity::class.java)
        this.startActivityForResult(intent, TEXT_SCAN_CODE)
    }

    private fun getScannedBarcode() {
        val intent = Intent(this, BarcodeActivity::class.java)
        this.startActivityForResult(intent, BARCODE_SCAN_CODE)
    }

    private fun getScannedFace() {
        val intent = Intent(this, FaceActivity::class.java)
        this.startActivityForResult(intent, FACE_SCAN_CODE)
    }

    private fun getScannedObject() {
        val intent = Intent(this, ObjectActivity::class.java)
        this.startActivityForResult(intent, OBJECT_SCAN_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            TEXT_SCAN_CODE -> handleScanResults(data)
            BARCODE_SCAN_CODE -> handleScanResults(data)
            FACE_SCAN_CODE -> handleScanResults(data)
            OBJECT_SCAN_CODE -> handleScanResults(data)
            else -> result.error("ResultNotFound","Result Activity Not Found", null)
        }
    }

    private fun handleScanResults(intent: Intent?) {
        if(intent != null && intent.hasExtra("scanned_text")) {
            this.result.success(intent.getStringExtra("scanned_text"))
        } else {
            this.result.error("NotFound", "No text recognized", null)
        }
    }

    companion object {
        private const val TAG = "MainActivity"
        private const val MAIN_CHANNEL = "com.intelliabb.flutter_google_ml"
        private const val TEXT_SCAN_CODE = 0
        private const val BARCODE_SCAN_CODE = 1
        private const val FACE_SCAN_CODE = 2
        private const val OBJECT_SCAN_CODE = 3
    }
}
