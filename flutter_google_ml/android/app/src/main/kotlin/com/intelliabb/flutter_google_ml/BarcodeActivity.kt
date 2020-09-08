package com.intelliabb.flutter_google_ml

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Size
import android.view.View
import android.widget.Toast
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage

class BarcodeActivity : AppCompatActivity() {
    private var preview: Preview? = null
    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private var viewFinder: PreviewView? = null

    private lateinit var cameraExecutor: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        viewFinder = findViewById(R.id.viewFinder)

        if(allPermissionsGranted())
            startCamera()
        else
            ActivityCompat.requestPermissions(this,
                    BarcodeActivity.REQUIRED_PERMISSIONS,
                    BarcodeActivity.REQUEST_CODE_PERMISSIONS)

        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
            preview = Preview.Builder().build()
            imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setTargetResolution(Size(480,200))
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, ImageAnalysis.Analyzer { imageProxy ->
                            processImage(imageProxy)
                        })
                    }

            val cameraSelector = CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build()

            try {
                cameraProvider.unbindAll()

                camera = cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
                preview?.setSurfaceProvider(viewFinder!!.createSurfaceProvider())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, ContextCompat.getMainExecutor(this))
    }

    @SuppressLint("UnsafeExperimentalUsageError")
    private fun processImage(imageProxy: ImageProxy) {
        val image = InputImage.fromMediaImage(imageProxy.image!!, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()
        scanner.process(image)
                .addOnSuccessListener { barcodes ->
                    processBarcode(barcodes)
                }
                .addOnFailureListener{ e -> // Task failed with an exception
                    e.printStackTrace()
                }
                .addOnCompleteListener {
                    imageProxy.close()
                }
    }

    private fun processBarcode(barcodes: List<Barcode>) {
        if(barcodes.isEmpty())
            return
        handleResult(barcodes.joinToString { it.rawValue!! })
    }

    private fun handleResult(result: String) {
        val intent = Intent()
        intent.putExtra("scanned_text", result)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this,
                        "Permissions not granted.",
                        Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    fun onCancelTapped(view: View) {
        setResult(Activity.RESULT_CANCELED)
        finish()
    }

    companion object {
        private const val TAG = "BarcodeActivity"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
    }
}