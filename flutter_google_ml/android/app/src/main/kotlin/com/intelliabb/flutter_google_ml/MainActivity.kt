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
            if(call.method == SCAN_METHOD){
                this.result = result
                getScannedText()
            } else {
                result.notImplemented()
            }
        }
    }

    private fun getScannedText() {
        val intent = Intent(this, ScanActivity::class.java)
        this.startActivityForResult(intent, SCAN_ACTIVITY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            SCAN_ACTIVITY_CODE -> handleScanResults(data)
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
        private val MAIN_CHANNEL = "com.intelliabb.flutter_google_ml"
        private val SCAN_METHOD = "getScannedText"
        private val SCAN_ACTIVITY_CODE = 0
    }
}
