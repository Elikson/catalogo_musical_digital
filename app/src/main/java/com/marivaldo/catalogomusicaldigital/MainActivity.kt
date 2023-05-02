package com.marivaldo.catalogomusicaldigital

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MainActivity : AppCompatActivity() {
    var mediaPlayer = MediaPlayer()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestPermission()
            ) { isGranted: Boolean ->
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your
                    // app.
                    Toast.makeText(this, "Permissão concedida", Toast.LENGTH_SHORT).show()
                    navigateQrCodeActivity()
                } else {
                    // Explain to the user that the feature is unavailable because the
                    // feature requires a permission that the user has denied. At the
                    // same time, respect the user's decision. Don't link to system
                    // settings in an effort to convince the user to change their
                    // decision.
                    Toast.makeText(this, "Permissão não concedida", Toast.LENGTH_SHORT).show()
                }
            }

        val btnOpenCamera = findViewById<FloatingActionButton>(R.id.btn_open_camera)
        btnOpenCamera.setOnClickListener { view ->
            run {

                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED -> {
                        // You can use the API that requires the permission.
                        navigateQrCodeActivity()
                    }
                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                        // In an educational UI, explain to the user why your app requires this
                        // permission for a specific feature to behave as expected, and what
                        // features are disabled if it's declined. In this UI, include a
                        // "cancel" or "no thanks" button that lets the user continue
                        // using your app without granting the permission.
                        requestPermissionLauncher.launch(
                            Manifest.permission.CAMERA)
                    }
                    else -> {
                        // You can directly ask for the permission.
                        // The registered ActivityResultCallback gets the result of this request.
                        requestPermissionLauncher.launch(
                            Manifest.permission.CAMERA)
                    }
                }
            }
        }
    }

    fun navigateQrCodeActivity(){
        val intent: Intent = Intent(this, ReadQrCodeActivity::class.java)
        startActivity(intent)
    }

    fun clickPlay(v: View){
        var raw: Int = R.raw.drums
        if(v.id == R.id.btn_drums){
            raw = R.raw.drums
        }else if(v.id == R.id.btn_guitar){
            raw = R.raw.guitar
        }else if(v.id == R.id.btn_eletric_guitar){
            raw = R.raw.eletric_guitar
        }

        mediaPlayer.reset()
        mediaPlayer = MediaPlayer.create(this, raw)
        mediaPlayer.start()
    }

    fun clickShowQrCode(v: View){
        var resource: Int = R.drawable.drums1
        if(v.id == R.id.btn_qrdrums){
            resource = R.drawable.drums1
        }else if(v.id == R.id.btn_qrguitar){
            resource = R.drawable.guitar1
        }else if(v.id == R.id.btn_qreletric_guitar){
            resource = R.drawable.eletricguitar1
        }

        val showQrCodeLayout = layoutInflater.inflate(R.layout.inflate_show_qr_code, null)

        val imageViewQr = showQrCodeLayout.findViewById<ImageView>(R.id.img_qr)
        imageViewQr.setImageResource(resource)

        val showQrdialog: AlertDialog
        val builder = AlertDialog.Builder(this)
        builder.setMessage("Qr Code")
            .setPositiveButton("Fechar",
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                }).setView(showQrCodeLayout)

        showQrdialog = builder.create()
        showQrdialog.show()
    }
}