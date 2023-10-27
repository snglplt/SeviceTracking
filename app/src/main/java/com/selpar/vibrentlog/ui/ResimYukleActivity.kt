package com.selpar.vibrentlog.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.selpar.vibrentlog.R
import java.io.ByteArrayOutputStream
import java.util.HashMap

class ResimYukleActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    private val CAMERA_REQUEST = 1
    private lateinit var bitmap:Bitmap
    lateinit var imageForm:ImageView
    lateinit var btnYukle:Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resim_yukle)
        imageForm=findViewById(R.id.imageForm)
        btnYukle=findViewById(R.id.btnYukle)
        val bottomNavigationView: BottomNavigationView
        bottomNavigationView = findViewById<View>(com.selpar.vibrentlog.R.id.bottomNavigationView) as BottomNavigationView
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@ResimYukleActivity)
        val menu = bottomNavigationView.menu
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        // cameraIntent.setType("image/*")
        startActivityForResult(cameraIntent,CAMERA_REQUEST)
        imageForm.setOnClickListener {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            // cameraIntent.setType("image/*")
            startActivityForResult(cameraIntent,CAMERA_REQUEST)
        }
        btnYukle.setOnClickListener {
           onApi()
        }
    }
    fun ImageToString() : String {
        val byteArrayOutputsStream= ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputsStream)
        var imageByte=byteArrayOutputsStream.toByteArray()
        return Base64.encodeToString(imageByte, Base64.DEFAULT)
    }
    private fun onApi() {
        val image=ImageToString()
        val queue = Volley.newRequestQueue(this)
        var url = "https://viprentlog.com/mobil/vib_mobil.php"
        val postRequest: StringRequest = object : StringRequest(
            Method.POST, url,
            com.android.volley.Response.Listener { response -> // response
                Log.d("Response", response!!)
                Toast.makeText(this,"Ekleme Başarılı",Toast.LENGTH_LONG).show()
                val i = Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("personel"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)

            },
            com.android.volley.Response.ErrorListener {
                Log.d("Response", "HATALI")
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["cat"] = intent.getStringExtra("cat").toString()
                params["personel"] = intent.getStringExtra("personel").toString()
                params["yol"] = image
                params["kullanici"] = intent.getStringExtra("adi").toString()
                params["tur"] = "form_resim_kaydet"
                return params
            }
        }
        queue.add(postRequest)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data:  Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data!=null)
        {
            bitmap= data?.extras?.get("data") as Bitmap
            imageForm.setImageBitmap(bitmap)
        }
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.selpar.vibrentlog.R.id.ic_job -> {
                val i = Intent(this, GorevEkleActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

                i.putExtra("personel",intent.getStringExtra("personel"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            com.selpar.vibrentlog.R.id.ic_profil -> {
                val i = Intent(this, ProfilActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("personel"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            com.selpar.vibrentlog.R.id.ic_home -> {
                val i = Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("personel"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            com.selpar.vibrentlog.R.id.ic_notification -> {
                val i = Intent(this, BildirimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("personel"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            com.selpar.vibrentlog.R.id.ic_search -> {
                val i = Intent(this, AramaActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("personel"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
        }
        return true
    }


}