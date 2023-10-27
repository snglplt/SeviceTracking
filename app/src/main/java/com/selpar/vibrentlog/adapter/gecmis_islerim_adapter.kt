package com.selpar.vibrentlog.adapter


import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.content.res.TypedArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.selpar.vibrentlog.model.IslerimModel
import com.selpar.vibrentlog.ui.GecmisIsBilgisiActivity
import com.selpar.vibrentlog.ui.IsBilgiActivity

class gecmis_islerim_adapter (private val newsList : ArrayList<IslerimModel>) :
    RecyclerView.Adapter<gecmis_islerim_adapter.MyViewHolder>() {





    var sayac:Int=0
    @SuppressLint("MissingInflatedId")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=
            LayoutInflater.from(parent.context).inflate(com.selpar.vibrentlog.R.layout.tum_kayitli_islerim,parent,false)



        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = newsList[position]

        if(currentItem.hizmettipi!="null")
            holder.txthizmettipi.text=currentItem.hizmettipi

        if(currentItem.atarihi!="null") {
            val bul_tarih = currentItem.atarihi.split("-")
            holder.txtatarihi.text = bul_tarih[2]+"-"+bul_tarih[1]+"-"+bul_tarih[0]
        }
        if(currentItem.asaati!="null")
            holder.txtasaati.text=currentItem.asaati
        if(currentItem.nereden!="null")
            holder.txtnereden.text=currentItem.nereden
        if(currentItem.nereye!="null")
            holder.txtnereye.text=currentItem.nereye
        holder.btn_baslat.setOnClickListener {
            val imservis = Intent(currentItem.context, GecmisIsBilgisiActivity::class.java)
            imservis.putExtra("id", currentItem.id)
            imservis.putExtra("cat", currentItem.cat)
            imservis.putExtra("personel", currentItem.personel)
            imservis.putExtra("adi", currentItem.adi)
            imservis.putExtra("soyadi", currentItem.soyadi)
            currentItem.context.startActivity(imservis)
        }
        holder.itemView.setOnClickListener {

            val imservis = Intent(currentItem.context, GecmisIsBilgisiActivity::class.java)
            imservis.putExtra("id", currentItem.id)
            imservis.putExtra("cat", currentItem.cat)
            imservis.putExtra("personel", currentItem.personel)
            imservis.putExtra("adi", currentItem.adi)
            imservis.putExtra("soyadi", currentItem.soyadi)
            currentItem.context.startActivity(imservis)
        }


    }

    override fun getItemCount(): Int {
        return newsList.size
        //newsList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val txthizmettipi : TextView = itemView.findViewById(com.selpar.vibrentlog.R.id.txthizmettipi)
        val txtatarihi : TextView = itemView.findViewById(com.selpar.vibrentlog.R.id.txtatarihi)
        val txtasaati : TextView = itemView.findViewById(com.selpar.vibrentlog.R.id.txtasaati)
        val txtnereden : TextView = itemView.findViewById(com.selpar.vibrentlog.R.id.txtnereden)
        val txtnereye : TextView = itemView.findViewById(com.selpar.vibrentlog.R.id.txtnereye)
        val btn_baslat : Button = itemView.findViewById(com.selpar.vibrentlog.R.id.btn_baslat)


    }
}