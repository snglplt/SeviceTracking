package com.selpar.vibrentlog.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.selpar.vibrentlog.R
import com.selpar.vibrentlog.adapter.islerim_getir_adapter
import com.selpar.vibrentlog.fragment.BugunFragment
import com.selpar.vibrentlog.fragment.YapilacakFragment
import com.selpar.vibrentlog.model.IslerimModel
import org.json.JSONArray
import java.util.ArrayList

class GorevEkleActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var newArrayList: ArrayList<IslerimModel>
    lateinit var kadi:String
    lateinit var sifre:String
    lateinit var personel:String
    val hizmettipi=ArrayList<String>()
    val atarihi=ArrayList<String>()
    val asaati=ArrayList<String>()
    val nereden=ArrayList<String>()
    val nereye=ArrayList<String>()
    val aractipi=ArrayList<String>()
    val id=ArrayList<String>()
    val cat=ArrayList<String>()
    lateinit var adi:String
    lateinit var soyadi:String
    lateinit var floatingActionButton:FloatingActionButton
    lateinit var btn_bugun:Button
    lateinit var btn_yapilacak:Button
    var bundlem=Bundle()
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.islerim)
        floatingActionButton=findViewById(R.id.floatingActionButton)
        btn_bugun=findViewById(R.id.btn_bugun)
        btn_yapilacak=findViewById(R.id.btn_yapilacak)

        kadi= intent.getStringExtra("kadi").toString()
        sifre= intent.getStringExtra("sifre").toString()
        personel= intent.getStringExtra("personel").toString()
        adi= intent.getStringExtra("adi").toString()
        soyadi= intent.getStringExtra("soyadi").toString()
        Toast.makeText(this,"personel: "+personel,Toast.LENGTH_LONG).show()
        val bottomNavigationView: BottomNavigationView
        bottomNavigationView = findViewById<View>(com.selpar.vibrentlog.R.id.bottomNavigationView) as BottomNavigationView
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@GorevEkleActivity)
        val menu = bottomNavigationView.menu
        this.onNavigationItemSelected(menu.findItem(com.selpar.vibrentlog.R.id.ic_job))
        floatingActionButton.setOnClickListener {
            val i=Intent(this,IsEkleActivity::class.java)
            i.putExtra("personel",personel)
            i.putExtra("adi",intent.getStringExtra("adi"))
            i.putExtra("soyadi",intent.getStringExtra("soyadi"))
            startActivity(i)
        }
        bugunGetir()
        btn_bugun.setOnClickListener {bugunGetir()  }
        btn_yapilacak.setOnClickListener {sonraGetir()  }
    }
    private fun bugunGetir() {
        btn_bugun.setBackgroundColor(Color.BLUE)
        btn_yapilacak.setBackgroundColor(Color.GRAY)
        val fragobj = BugunFragment()
        bundlem.putString("personel", personel)
        bundlem.putString("adi", intent.getStringExtra("adi"))
        bundlem.putString("soyadi", intent.getStringExtra("soyadi"))
        fragobj.arguments=bundlem

        // fragobje.arguments=bundlem
        //fragobj.stArguments(bundlem)
        val fragmentmaneger=this.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_yapilacaklar,  fragobj)
            .commit()
    }
    private fun sonraGetir() {
        btn_bugun.setBackgroundColor(Color.GRAY)
        btn_yapilacak.setBackgroundColor(Color.BLUE)
        val fragobj = YapilacakFragment()
        bundlem.putString("personel", personel)
        bundlem.putString("adi", intent.getStringExtra("adi"))
        bundlem.putString("soyadi", intent.getStringExtra("soyadi"))
        fragobj.arguments=bundlem

        // fragobje.arguments=bundlem
        //fragobj.stArguments(bundlem)
        val fragmentmaneger=this.supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_yapilacaklar,  fragobj)
            .commit()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            com.selpar.vibrentlog.R.id.ic_profil -> {
                val i = Intent(this, ProfilActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",personel)
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            com.selpar.vibrentlog.R.id.ic_home -> {
                val i = Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",personel)
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            com.selpar.vibrentlog.R.id.ic_notification -> {
                val i = Intent(this, BildirimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",personel)
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            com.selpar.vibrentlog.R.id.ic_search -> {
                val i = Intent(this, AramaActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",personel)
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