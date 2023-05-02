package com.marivaldo.catalogomusicaldigital

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.CodeScannerView
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ErrorCallback
import com.budiyev.android.codescanner.ScanMode

class ReadQrCodeActivity : AppCompatActivity() {
    private lateinit var codeScanner: CodeScanner
    private lateinit var tvDescription: TextView

    var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_read_qr_code)
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        tvDescription = findViewById(R.id.tv_description)

        codeScanner = CodeScanner(this, scannerView)


        // Parameters (default values)
        codeScanner.camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
        codeScanner.formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,
        // ex. listOf(BarcodeFormat.QR_CODE)
        codeScanner.autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
        codeScanner.scanMode = ScanMode.SINGLE // or CONTINUOUS or PREVIEW
        codeScanner.isAutoFocusEnabled = true // Whether to enable auto focus or not
        codeScanner.isFlashEnabled = false // Whether to enable flash or not

        // Callbacks
        codeScanner.decodeCallback = DecodeCallback {
            runOnUiThread {
                playQrMusic(it.text)
            }
        }
        codeScanner.errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
            runOnUiThread {
                Toast.makeText(this, "Camera initialization error: ${it.message}",
                    Toast.LENGTH_LONG).show()
            }
        }

        scannerView.setOnClickListener {
            codeScanner.startPreview()
            tvDescription.setText("Instrumento: detectando...")
            mediaPlayer.reset()
        }
    }

    fun playQrMusic(data: String){
        var raw: Int = R.raw.drums
        var description: String = "Instrumento: detectando..."
        if(data == "drums1"){
            raw = R.raw.drums
            description = "Instrumento: Bateria"
        }else if(data == "guitar1"){
            raw = R.raw.guitar
            description = "Instrumento: Violão"
        }else if(data == "eletricguitar1"){
            raw = R.raw.eletric_guitar
            description = "Instrumento: Guitarra"
        }

        tvDescription.setText(description)

        mediaPlayer.reset()
        mediaPlayer = MediaPlayer.create(this, raw)
        mediaPlayer.start()
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }
}