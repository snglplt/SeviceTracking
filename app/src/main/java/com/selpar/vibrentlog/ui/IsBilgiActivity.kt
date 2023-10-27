package com.selpar.vibrentlog.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.selpar.vibrentlog.R
import com.selpar.vibrentlog.adapter.islerim_getir_adapter
import com.selpar.vibrentlog.model.IslerimModel
import org.json.JSONArray
import java.util.ArrayList
import java.util.HashMap

class IsBilgiActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var hizmettipi: EditText
    lateinit var atarihi: EditText
    lateinit var asaati: EditText
    lateinit var nereden: EditText
    lateinit var nereye: EditText
    lateinit var aractipi: EditText
    lateinit var id: EditText
    lateinit var iveren: EditText
    lateinit var itutari: EditText
    lateinit var odemetipi: EditText
    lateinit var firmasi: EditText
    lateinit var kullanici: EditText
    lateinit var neredenkonum: EditText
    lateinit var nereyekonum: EditText
    lateinit var neredenadres: EditText
    lateinit var nereyeadres: EditText
    lateinit var btnguncelle: Button
    lateinit var btnimzala: Button
    lateinit var sp_is_durumu:Spinner
    lateinit var cat:TextView
    var dat_atarihi:String=""
    lateinit var btn_resim:Button






    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_is_bilgi)
        onBaslat()
        onBilgiGetir()
        val bottomNavigationView: BottomNavigationView
        bottomNavigationView = findViewById<View>(com.selpar.vibrentlog.R.id.bottomNavigationView) as BottomNavigationView
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@IsBilgiActivity)
        val menu = bottomNavigationView.menu

        onBilgiGetir()
        guncelle_isdurumu_is(intent.getStringExtra("cat").toString())

        onAlert()
        btn_resim.setOnClickListener {
            val i = Intent(this, ResimYukleActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
            i.putExtra("cat",cat.getText().toString())
            i.putExtra("id",id.getText().toString())
            i.putExtra("personel",intent.getStringExtra("personel"))
            i.putExtra("adi",intent.getStringExtra("adi"))
            i.putExtra("soyadi",intent.getStringExtra("soyadi"))
            this.startActivity(i)
            overridePendingTransition(0, 0)
            finish()
        }
        atarihi.setOnFocusChangeListener { _, hasFocus ->
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
                        dat_atarihi = (year.toString() + "-" + (monthOfYear + 1) + "-" +dayOfMonth )
                        val dat_gosterilen = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" +year )

                        atarihi.setText(dat_gosterilen)
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

        btnguncelle.setOnClickListener {
            Toast.makeText(this,cat.getText().toString(),Toast.LENGTH_LONG).show()
            guncelle(cat.getText().toString())

        }
        btnimzala.setOnClickListener {
            val i=Intent(this,ImzaActivity::class.java)
            i.putExtra("cat",cat.getText().toString())
            i.putExtra("id",id.getText().toString())
            i.putExtra("personel",intent.getStringExtra("personel"))
            i.putExtra("adi",intent.getStringExtra("adi"))
            i.putExtra("soyadi",intent.getStringExtra("soyadi"))
            startActivity(i)
        }

    }

    @SuppressLint("MissingInflatedId")
    private fun onAlert() {
        val alertadd = AlertDialog.Builder(this)
        alertadd.setTitle("İŞ DURUMU GÜNCELLENSİN Mİ?")
        val factory = LayoutInflater.from(this)
        val view: View = factory.inflate(R.layout.is_durumu, null)

        sp_is_durumu=view.findViewById<Spinner>(R.id.sp_is_durumu)
        val alspinner_is = ArrayList<String>()
        var url = "https://viprentlog.com/mobil/vib_mobil.php?tur=is_durumu"
        val queue: RequestQueue = Volley.newRequestQueue(this)
        Log.d("islerim", url)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {

                    val json = response["isdurumu"] as JSONArray
                    for (i in 0 until json.length()) {

                        val item = json.getJSONObject(i)

                        alspinner_is.add(item.getString("adi"))
                    }
                    val adapter1: Any? = ArrayAdapter<Any?>(
                        view.getContext(),
                        android.R.layout.simple_spinner_item,
                        alspinner_is as List<Any?>
                    )
                    sp_is_durumu.setAdapter(adapter1 as SpinnerAdapter?)
                } catch (e: Exception) {
                }

            }, { error ->
                Log.e("TAG", "RESPONSE IS $error")

            }
        )
        queue.add(request)


        alertadd.setView(view)
        alertadd.setPositiveButton(
            "EVET"
        ) { dialogInterface, which ->
            
            Toast.makeText(this,"cat "+cat.getText().toString(),Toast.LENGTH_LONG).show()
            guncelle_isdurumu(sp_is_durumu.selectedItem.toString())

        }
        alertadd.setNegativeButton("Hayır"){
                dialog, which -> dialog.dismiss()
        }
        alertadd.show()
    }

    private fun guncelle_isdurumu(isdurumu: String) {
        val queue = Volley.newRequestQueue(this)
        var url = "https://viprentlog.com/mobil/vib_mobil.php"
        val postRequest: StringRequest = @SuppressLint("RestrictedApi")
        object : StringRequest(
            Method.POST, url,
            com.android.volley.Response.Listener { response -> // response
                Log.d("Response", response!!)
                //Toast.makeText(this,"Güncelleme Başarılı: "+isdurumu,Toast.LENGTH_SHORT).show()
                val builder = AlertDialog.Builder(this)

                builder.setTitle(getString(R.string.isdurumu))
                    .setMessage("Güncelleme Başarılı: "+isdurumu)
                    .setPositiveButton(getString(R.string.tamam)) { dialog, which ->
                        // Positive button click handler
                    }

                val dialog = builder.create()
                dialog.show()

            },
            com.android.volley.Response.ErrorListener {
                Log.d("Response", "HATALI")
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["isdurumu"] = isdurumu
                params["cat"] =cat.getText().toString()
                params["tur"] = "is_durumu_guncelle"
                return params
            }
        }
        queue.add(postRequest)
    }

    private fun guncelle_isdurumu_is(cat:String) {
        val queue = Volley.newRequestQueue(this)
        var url = "https://viprentlog.com/mobil/vib_mobil.php"
        val postRequest: StringRequest = @SuppressLint("RestrictedApi")
        object : StringRequest(
            Method.POST, url,
            com.android.volley.Response.Listener { response -> // response
                Log.d("Response", response!!)
                Toast.makeText(this,"İş Durumu Güncelleme Başarılı: "+cat,Toast.LENGTH_SHORT).show()


            },
            com.android.volley.Response.ErrorListener {
                Log.d("Response", "HATALI")
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["cat"] =cat
                params["tur"] = "is_durumu_guncelle_gordu"
                return params
            }
        }
        queue.add(postRequest)
    }

    private fun onBaslat() {
        btn_resim=findViewById(R.id.btn_resim)
        cat=findViewById(R.id.cat)
        hizmettipi=findViewById(R.id.hizmettipi)
        atarihi=findViewById(R.id.atarih)
        asaati=findViewById(R.id.asaati)
        nereden=findViewById(R.id.nereden)
        nereye=findViewById(R.id.nereye)
        aractipi=findViewById(R.id.aractipi)
        id=findViewById(R.id.id)
        iveren=findViewById(R.id.iveren)
        itutari=findViewById(R.id.itutari)
        odemetipi=findViewById(R.id.odemetipi)
        firmasi=findViewById(R.id.firmasi)
        kullanici=findViewById(R.id.kullanici)
        neredenkonum=findViewById(R.id.neredenkonum)
        nereyekonum=findViewById(R.id.nereyekonum)
        neredenadres=findViewById(R.id.neredenadres)
        nereyeadres=findViewById(R.id.nereyeadres)
        btnguncelle=findViewById(R.id.btnguncelle)
        btnimzala=findViewById(R.id.btnimzala)
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

    @SuppressLint("SuspiciousIndentation")
    private fun onBilgiGetir() {
        val urlek ="&cat=" + intent.getStringExtra("cat")+"&id="+intent.getStringExtra("id")
        var url = "https://viprentlog.com/mobil/vib_mobil.php?tur=islerimbilgi" + urlek
        val queue: RequestQueue = Volley.newRequestQueue(this)
        Log.d("islerim", url)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {

                    val json = response["islerim"] as JSONArray
                    for (i in 0 until json.length()) {

                        val item = json.getJSONObject(i)

                        cat.setText(item.getString("cat"))
                        id.setText(item.getString("id"))
                        hizmettipi.setText(item.getString("hizmettipi"))
                        try{
                            val bul_tarih =item.getString("atarihi").split("-")
                            atarihi.setText(bul_tarih[2]+"-"+bul_tarih[1]+"-"+bul_tarih[0])
                        }catch (e :Exception){
                            atarihi.setText(item.getString("atarihi"))
                        }
                        asaati.setText(item.getString("asaati"))
                        if(item.getString("nereden")!="null")
                        nereden.setText(item.getString("nereden"))
                        if(item.getString("nereye")!="null")
                        nereye.setText(item.getString("nereye"))
                        if(item.getString("aractipi")!="null")
                        aractipi.setText(item.getString("aractipi"))
                        if(item.getString("iveren")!="null")
                        iveren.setText(item.getString("iveren"))
                        if(item.getString("itutari")!="null")
                        itutari.setText(item.getString("itutari"))
                        if(item.getString("odemetipi")!="null")
                        odemetipi.setText(item.getString("odemetipi"))
                        if(item.getString("firmasi")!="null")
                        firmasi.setText(item.getString("firmasi"))
                        if(item.getString("kullanici")!="null")
                        kullanici.setText(item.getString("kullanici"))
                        if(item.getString("neredenkonum")!="null")
                        neredenkonum.setText(item.getString("neredenkonum"))
                        if(item.getString("nereyekonum")!="null")
                        nereyekonum.setText(item.getString("nereyekonum"))
                        if(item.getString("neredenadres ")!="null")
                        neredenadres.setText(item.getString("neredenadres"))
                        if(item.getString("nereyeadres")!="null")
                        nereyeadres.setText(item.getString("nereyeadres"))
                    }
                } catch (e: Exception) {
                }

            }, { error ->
                Log.e("TAG", "RESPONSE IS $error")

            }
        )
        queue.add(request)



    }
    private fun guncelle(cat:String) {
        val queue = Volley.newRequestQueue(this)
        var url = "https://viprentlog.com/mobil/vib_mobil.php"
        val postRequest: StringRequest = @SuppressLint("RestrictedApi")
        object : StringRequest(
            Method.POST, url,
            com.android.volley.Response.Listener { response -> // response
                Log.d("Response", response!!)
                onBilgiGetir()
                Toast.makeText(this,"Güncelleme Başarılı: ", Toast.LENGTH_SHORT).show()

            },
            com.android.volley.Response.ErrorListener {
                Log.d("Response", "HATALI")
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["cat"] = cat
                params["hizmettipi"] = hizmettipi.getText().toString()
                params["atarihi"] = dat_atarihi
                params["asaati"] = asaati.getText().toString()
                params["nereden"] = nereden.getText().toString()
                params["nereye"] = nereye.getText().toString()
                params["aractipi"] = aractipi.getText().toString()
                params["iveren"] = iveren.getText().toString()
                params["itutari"] = itutari.getText().toString()
                params["odemetipi"] = odemetipi.getText().toString()
                params["neredenkonum"] = neredenkonum.getText().toString()
                params["nereyekonum"] = nereyekonum.getText().toString()
                params["neredenadres"] = neredenadres.getText().toString()
                params["nereyeadres"] = nereyeadres.getText().toString()
                params["tur"] = "is_guncelle"
                return params
            }
        }
        queue.add(postRequest)
    }


}
