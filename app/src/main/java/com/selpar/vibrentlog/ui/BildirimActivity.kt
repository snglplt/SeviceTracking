package com.selpar.vibrentlog.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.selpar.vibrentlog.MainActivity
import com.selpar.vibrentlog.R
import java.util.HashMap

class BildirimActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bildirim)
        Toast.makeText(this,"PERSONEL:  "+intent.getStringExtra("id").toString(),Toast.LENGTH_LONG).show()
        val bottomNavigationView: BottomNavigationView
        bottomNavigationView = findViewById<View>(com.selpar.vibrentlog.R.id.bottomNavigationView) as BottomNavigationView
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@BildirimActivity)
        val menu = bottomNavigationView.menu
        onAlert()

    }

    private fun onAlert() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle(getString(R.string.isdurumu))
            .setMessage("ÇIKIŞ YAPILSIN MI?")
            .setPositiveButton("Evet") { dialog, which ->
                cikisYap()
            }
            .setNegativeButton("Hayır"){
                    dialog, which ->
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun cikisYap() {
        cikisYapApi(intent.getStringExtra("id"))
        val sheredpreferens = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
        sheredpreferens.edit().remove("kadi").commit()
        sheredpreferens.edit().remove("sifre").commit()
        sheredpreferens.edit().remove("personel").commit()
        val editor = sheredpreferens.edit()
        editor.commit()
        editor.clear()
        editor.remove("kadi")
        editor.remove("sifre")
        val i=Intent(this,MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun cikisYapApi(personel: String?) {
        val queue = Volley.newRequestQueue(this)
        var url = "https://viprentlog.com/mobil/vib_mobil.php"
        val postRequest: StringRequest = @SuppressLint("RestrictedApi")
        object : StringRequest(
            Method.POST, url,
            com.android.volley.Response.Listener { response -> // response
                Log.d("Response", response!!)
                //Toast.makeText(this,"Güncelleme Başarılı: "+isdurumu,Toast.LENGTH_SHORT).show()


            },
            com.android.volley.Response.ErrorListener {
                Log.d("Response", "HATALI")
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["personel"] = intent.getStringExtra("id").toString()
                params["tur"] = "cikis_yap"
                return params
            }
        }
        queue.add(postRequest)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.selpar.vibrentlog.R.id.ic_job -> {
                val i = Intent(this, GorevEkleActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("id"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            com.selpar.vibrentlog.R.id.ic_profil -> {
                val i = Intent(this, ProfilActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("id"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            com.selpar.vibrentlog.R.id.ic_home -> {
                val i = Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("id"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)

                finish()
                true
            }

            com.selpar.vibrentlog.R.id.ic_search -> {
                val i = Intent(this, AramaActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("id"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)

                finish()
                true
            }
        }
        return true
    }}