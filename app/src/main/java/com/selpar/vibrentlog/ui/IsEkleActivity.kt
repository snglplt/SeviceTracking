package com.selpar.vibrentlog.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.selpar.vibrentlog.R
import org.json.JSONArray
import java.util.ArrayList
import java.util.HashMap

class IsEkleActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var edbaslangictarihi:EditText
    lateinit var edsaat:EditText
    lateinit var edad_soyad:EditText
    lateinit var edadres:EditText
    lateinit var edkonum:EditText
    lateinit var edkisi_sayisi:EditText
    lateinit var edvaliz_sayisi:EditText
    lateinit var edhavayolu:EditText
    lateinit var educuskodu:EditText
    lateinit var edisintutari:EditText
    lateinit var edtahsilsekli:EditText
    lateinit var ediveren:EditText
    lateinit var ednereye:EditText
    lateinit var btn_kaydet:Button
    lateinit var dat_edbaslangictarihi:String
    lateinit var sp_hizmetler:Spinner
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gorev_ekle)
        onBaslat()
        spinnerDoldur()
        Toast.makeText(this,intent.getStringExtra("personel").toString(),Toast.LENGTH_LONG).show()
        edbaslangictarihi.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val c = Calendar.getInstance()

                // on below line we are getting
                // our day, month and year.
                val year = c.get(Calendar.YEAR)
                val month = c.get(Calendar.MONTH)
                val day = c.get(Calendar.DAY_OF_MONTH)

                // on below line we are creating a
                // variable for date picker dialog.
                val datePickerDialog = DatePickerDialog(
                    // on below line we are passing context.
                    this,
                    { view, year, monthOfYear, dayOfMonth ->
                        // on below line we are setting
                        // date to our edit text.
                        dat_edbaslangictarihi = (year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth )
                        val dat_gosterilen = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" +year )

                        edbaslangictarihi.setText(dat_gosterilen)
                    },
                    // on below line we are passing year, month
                    // and day for the selected date in our date picker.
                    year,
                    month,
                    day
                )
                // at last we are calling show
                // to display our date picker dialog.
                datePickerDialog.show()
            } else {
            }
        }
        edsaat.setText(Calendar.getInstance().getTime().toString().substring(11,19))
        val bottomNavigationView: BottomNavigationView
        bottomNavigationView = findViewById<View>(com.selpar.vibrentlog.R.id.bottomNavigationView) as BottomNavigationView
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@IsEkleActivity)

        val menu = bottomNavigationView.menu
        btn_kaydet.setOnClickListener { isEkle() }


    }

    private fun spinnerDoldur() {
        val alspinner_is = ArrayList<String>()
        var url = "https://viprentlog.com/mobil/vib_mobil.php?tur=hizmet_durumu"
        val queue: RequestQueue = Volley.newRequestQueue(this)
        Log.d("islerim", url)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {

                    val json = response["hizmetler"] as JSONArray
                    for (i in 0 until json.length()) {

                        val item = json.getJSONObject(i)

                        alspinner_is.add(item.getString("adi"))
                    }
                    val adapter1: Any? = ArrayAdapter<Any?>(
                        this,
                        android.R.layout.simple_spinner_item,
                        alspinner_is as List<Any?>
                    )
                    sp_hizmetler.setAdapter(adapter1 as SpinnerAdapter?)
                } catch (e: Exception) {
                }

            }, { error ->
                Log.e("TAG", "RESPONSE IS $error")

            }
        )
        queue.add(request)    }

    private fun isEkle()  {
        val queue = Volley.newRequestQueue(this)
        var url = "https://viprentlog.com/mobil/vib_mobil.php"
        val postRequest: StringRequest = @SuppressLint("RestrictedApi")
        object : StringRequest(
            Method.POST, url,
            com.android.volley.Response.Listener { response -> // response
                Log.d("Response", response!!)

                Toast.makeText(this,"Ekleme Başarılı: ", Toast.LENGTH_SHORT).show()
                val i = Intent(this, GorevEkleActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("personel"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                startActivity(i)

            },
            com.android.volley.Response.ErrorListener {
                Log.d("Response", "HATALI")
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["personel"]=intent.getStringExtra("personel").toString()
                params["atarihi"] = dat_edbaslangictarihi
                params["asaati"] = edsaat.getText().toString()
                params["adres"] = edadres.getText().toString()
                params["konum"] = edkonum.getText().toString()
                params["kisi_sayisi"] = edkisi_sayisi.getText().toString()
                params["valiz_sayisi"] = edvaliz_sayisi.getText().toString()
                params["hy_sirketi"] = edhavayolu.getText().toString()
                params["ukodu"] = educuskodu.getText().toString()
                params["itutari"] = edisintutari.getText().toString()
                params["odemetipi"] = edtahsilsekli.getText().toString()
                params["iveren"] = ediveren.getText().toString()
                params["nereye"] = ednereye.getText().toString()
                params["hizmettipi"] = sp_hizmetler.selectedItem.toString()
                params["ad_soyad"] = edad_soyad.getText().toString()
                params["tur"] = "is_ekle"
                return params
            }
        }
        queue.add(postRequest)
    }



    private fun onBaslat() {
        ednereye=findViewById(R.id.ednereye)
        sp_hizmetler=findViewById(R.id.sp_hizmetler)
        edbaslangictarihi=findViewById(R.id.edbaslangictarihi)
        edsaat=findViewById(R.id.edsaat)
        edad_soyad=findViewById(R.id.edad_soyad)
        edadres=findViewById(R.id.edadres)
        edkonum=findViewById(R.id.edkonum)
        edkisi_sayisi=findViewById(R.id.edkisi_sayisi)
        edvaliz_sayisi=findViewById(R.id.edvaliz_sayisi)
        edhavayolu=findViewById(R.id.edhavayolu)
        educuskodu=findViewById(R.id.educuskodu)
        edisintutari=findViewById(R.id.edisintutari)
        edtahsilsekli=findViewById(R.id.edtahsilsekli)
        ediveren=findViewById(R.id.ediveren)
        btn_kaydet=findViewById(R.id.btn_kaydet)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.ic_job -> {
                val i = Intent(this, GorevEkleActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("personel"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            R.id.ic_profil -> {
                val i = Intent(this, ProfilActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("personel"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            R.id.ic_home -> {
                val i = Intent(this, HomeActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("personel"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            R.id.ic_notification -> {
                val i = Intent(this, BildirimActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",intent.getStringExtra("personel"))
                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            R.id.ic_search -> {
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