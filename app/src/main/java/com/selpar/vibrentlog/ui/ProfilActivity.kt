package com.selpar.vibrentlog.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.selpar.vibrentlog.R
import org.json.JSONArray
import java.util.HashMap

class ProfilActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var edad:EditText
    lateinit var edsoyad:EditText
    lateinit var edtc:EditText
    lateinit var edtel:EditText
    lateinit var edmail:EditText
    lateinit var edadres:EditText
    lateinit var btn_kaydet:Button
   override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)
        onBaslat()
       onBilgiGetir()
        val bottomNavigationView: BottomNavigationView
        bottomNavigationView = findViewById<View>(com.selpar.vibrentlog.R.id.bottomNavigationView) as BottomNavigationView
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@ProfilActivity)
        val menu = bottomNavigationView.menu
        this.onNavigationItemSelected(menu.findItem(com.selpar.vibrentlog.R.id.ic_profil))
       btn_kaydet.setOnClickListener {
           guncelle_profil()
       }
    }
    private fun onBilgiGetir() {
        val urlek ="&personel=" + intent.getStringExtra("personel")
        var url = "https://pratikhasar.com/netting/vibrentlog/vib_mobil.php?tur=profilbilgi" + urlek
        val queue: RequestQueue = Volley.newRequestQueue(this)
        Log.d("profil", url)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {

                    val json = response["profil"] as JSONArray
                    for (i in 0 until json.length()) {

                        val item = json.getJSONObject(i)


                        if(item.getString("adi")!="null")
                            edad.setText(item.getString("adi"))
                        if(item.getString("soyadi")!="null")
                            edsoyad.setText(item.getString("soyadi"))
                        if(item.getString("tcno")!="null")
                            edtc.setText(item.getString("tcno"))
                        if(item.getString("email")!="null")
                            edmail.setText(item.getString("email"))
                        if(item.getString("adres")!="null")
                            edadres.setText(item.getString("adres"))
                        if(item.getString("telefon")!="null")
                            edtel.setText(item.getString("telefon"))

                    }
                } catch (e: Exception) {
                }

            }, { error ->
                Log.e("TAG", "RESPONSE IS $error")

            }
        )
        queue.add(request)



    }

    private fun guncelle_profil() {
        val queue = Volley.newRequestQueue(this)
        var url = "https://viprentlog.com/mobil/vib_mobil.php"
        val postRequest: StringRequest = @SuppressLint("RestrictedApi")
        object : StringRequest(
            Method.POST, url,
            com.android.volley.Response.Listener { response -> // response
                Log.d("Response", response!!)
                onBilgiGetir()
                Toast.makeText(this,"Profil Güncelleme Başarılı ", Toast.LENGTH_SHORT).show()


            },
            com.android.volley.Response.ErrorListener {
                Log.d("Response", "HATALI")
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["personel"] =intent.getStringExtra("personel").toString()
                params["adi"]=edad.getText().toString()
                params["soyadi"]=edsoyad.getText().toString()
                params["tcno"]=edtc.getText().toString()
                params["tel"]=edtel.getText().toString()
                params["email"]=edmail.getText().toString()
                params["adres"]=edadres.getText().toString()
                params["tur"] = "profil_guncelleme"
                return params
            }
        }
        queue.add(postRequest)
    }

    private fun onBaslat() {
        edad=findViewById(R.id.edad)
        edsoyad=findViewById(R.id.edsoyad)
        edtc=findViewById(R.id.adtc)
        edtel=findViewById(R.id.edtel)
        edmail=findViewById(R.id.edmail)
        edadres=findViewById(R.id.edadres2)
        btn_kaydet=findViewById(R.id.btn_kaydet)
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
