package com.selpar.vibrentlog.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.selpar.vibrentlog.R
import com.selpar.vibrentlog.adapter.islerim_getir_adapter
import com.selpar.vibrentlog.model.IslerimModel
import org.json.JSONArray
import java.util.ArrayList


class YapilacakFragment : Fragment() {

    lateinit var rc_islerim:RecyclerView
    lateinit var personel:String
    lateinit var adi:String
    lateinit var soyadi:String
    private lateinit var newArrayList: ArrayList<IslerimModel>
    lateinit var kadi:String
    lateinit var sifre:String
    val hizmettipi= ArrayList<String>()
    val atarihi= ArrayList<String>()
    val asaati= ArrayList<String>()
    val nereden= ArrayList<String>()
    val nereye= ArrayList<String>()
    val aractipi= ArrayList<String>()
    val id= ArrayList<String>()
    val cat= ArrayList<String>()
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_yapilacak, container, false)
        rc_islerim=view.findViewById(R.id.rc_islerim)
        val args = this.arguments
        kadi = args?.getString("kadi").toString()
        adi= args?.getString("adi").toString()
        soyadi= args?.getString("soyadi").toString()
        personel= args?.getString("personel").toString()
        // Inflate the layout for this fragment
        acikIsGetir()
        return view
    }
    private fun acikIsGetir() {
        val urlek ="&personel=" + personel
        var url = "https://viprentlog.com/mobil/vib_mobil.php?tur=islerim" + urlek
        val queue: RequestQueue = Volley.newRequestQueue(requireContext())
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
                rc_islerim.layoutManager = LinearLayoutManager(requireContext())
                rc_islerim.setHasFixedSize(false)
                newArrayList = arrayListOf<IslerimModel>()
                getUserData(requireContext())

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
        rc_islerim.adapter = islerim_getir_adapter(newArrayList)

    }



}