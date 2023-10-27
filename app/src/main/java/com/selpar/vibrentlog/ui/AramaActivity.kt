package com.selpar.vibrentlog.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.selpar.vibrentlog.R
import com.selpar.vibrentlog.adapter.gecmis_islerim_adapter
import com.selpar.vibrentlog.adapter.islerim_getir_adapter
import com.selpar.vibrentlog.model.IslerimModel
import org.json.JSONArray
import java.util.ArrayList

class AramaActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var rc_gecmis_islerim: RecyclerView
    private lateinit var newArrayList: ArrayList<IslerimModel>
    lateinit var kadi:String
    lateinit var sifre:String
    lateinit var personel:String
    val hizmettipi= ArrayList<String>()
    val atarihi= ArrayList<String>()
    val asaati= ArrayList<String>()
    val nereden= ArrayList<String>()
    val nereye= ArrayList<String>()
    val aractipi= ArrayList<String>()
    val id= ArrayList<String>()
    val cat= ArrayList<String>()
    lateinit var adi:String
    lateinit var soyadi:String
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arama)
        kadi= intent.getStringExtra("kadi").toString()
        sifre= intent.getStringExtra("sifre").toString()
        personel= intent.getStringExtra("personel").toString()
        adi= intent.getStringExtra("adi").toString()
        soyadi= intent.getStringExtra("soyadi").toString()
        rc_gecmis_islerim=findViewById(R.id.rc_gecmis_islerim)

        val bottomNavigationView: BottomNavigationView
        bottomNavigationView = findViewById<View>(com.selpar.vibrentlog.R.id.bottomNavigationView) as BottomNavigationView
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@AramaActivity)
        val menu = bottomNavigationView.menu
        this.onNavigationItemSelected(menu.findItem(com.selpar.vibrentlog.R.id.ic_search))
        acikIsGetir()

    }
    private fun acikIsGetir() {
        val urlek ="&personel=" + personel
        var url = "https://viprentlog.com/mobil/vib_mobil.php?tur=biten_isler" + urlek
        val queue: RequestQueue = Volley.newRequestQueue(this)
        Log.d("islerim", url)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {

                    val json = response["islerim"] as JSONArray
                    for (i in 0 until json.length()) {

                        val item = json.getJSONObject(i)

                        cat.add(item.getString("cat"))
                        id.add(item.getString("id"))
                        if(item.getString("hizmettipi")!="null"){
                            hizmettipi.add(item.getString("hizmettipi"))
                        }else{
                            hizmettipi.add(item.getString(" "))
                        }

                        if(item.getString("atarihi")!="null"){
                            atarihi.add(item.getString("atarihi"))
                        }else{
                            atarihi.add(item.getString(" "))
                        }

                        if(item.getString("asaati")!="null"){
                            asaati.add(item.getString("asaati"))
                        }else{
                            asaati.add(item.getString(" "))
                        }

                        if(item.getString("nereden")!="null"){
                            nereden.add(item.getString("nereden"))
                        }else{
                            nereden.add(item.getString(" "))
                        }

                        if(item.getString("nereye")!="null"){
                            nereye.add(item.getString("nereye"))
                        }else{
                            nereye.add(item.getString(" "))
                        }









                    }
                } catch (e: Exception) {
                }
                rc_gecmis_islerim.layoutManager = LinearLayoutManager(this)
                rc_gecmis_islerim.setHasFixedSize(false)
                newArrayList = arrayListOf<IslerimModel>()
                getUserData(this)

            }, { error ->
                Log.e("TAG", "RESPONSE IS $error")

            }
        )
        queue.add(request)



    }
    private fun getUserData(context: Context) {
        for (i in hizmettipi.indices) {
            val news = IslerimModel(
                cat[i],
                context,
                id[i],
                hizmettipi[i],
                atarihi[i],
                asaati[i],
                nereden[i],
                nereye[i],
                "aractipi[i]",
                adi,
                soyadi,
                personel
            )

            newArrayList.add(news)
        }
        rc_gecmis_islerim.adapter = gecmis_islerim_adapter(newArrayList)

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

        }
        return true
    }
}