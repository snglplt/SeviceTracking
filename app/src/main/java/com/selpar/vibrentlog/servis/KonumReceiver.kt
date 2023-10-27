package com.selpar.vibrentlog.servis

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.selpar.vibrentlog.ui.BildirimActivity
import com.selpar.vibrentlog.ui.IsBilgiActivity
import org.json.JSONArray
import java.util.Timer
import java.util.TimerTask


class KonumReceiver() :  Service() {
    private val timer = Timer()
    private var isServiceRunning = false
    val nereden = ArrayList<String>()
    val nereye = ArrayList<String>()
    val id = ArrayList<Int>()
    val cat = ArrayList<String>()


    private fun Bildirim(intent:Intent,context:Context,cat: ArrayList<String>, id: ArrayList<Int>, nereden: ArrayList<String>, nereye: ArrayList<String>) {
        for (i in cat.indices){
            createNotification(context,cat[i])
            sendNotification(intent,context,cat[i],id[i],nereden[i],nereye[i])
        }


    }
    @SuppressLint("MissingPermission", "UnspecifiedImmutableFlag")
    private fun sendNotification(intent:Intent, context:Context, cat:String,id:Int,nereden:String,nereye:String) {
        val i=Intent(this,IsBilgiActivity::class.java)
        i.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK
        )
        i.putExtra("id",id.toString())
        i.putExtra("personel",id.toString())
        i.putExtra("cat",cat)
        val pendingIntent = PendingIntent.getActivity(this.applicationContext, 0, i,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val builder= NotificationCompat.Builder(context,cat)
                .setSmallIcon(com.selpar.vibrentlog.R.drawable.img)
                .setContentTitle("İş Takip")
                .setContentText(nereden+" adresinden "+nereye+" adresine götürülecek")
                .setContentIntent(pendingIntent)
            with(NotificationManagerCompat.from(context)) {
                notify(id, builder.build())
            }


        }else{
            val builder= NotificationCompat.Builder(context)
                .setSmallIcon(com.selpar.vibrentlog.R.drawable.img)
                .setContentTitle(nereden)
                .setContentText(nereye)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            with(NotificationManagerCompat.from(context)) {
                notify(id, builder.build())
            }
        }
    }

    fun createNotification(context:Context,cat:String){
        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name="Notification title"
            val descriptionText="Notification Description"
            val importance= NotificationManager.IMPORTANCE_DEFAULT
            val channel= NotificationChannel(cat,name,importance).apply {
                description=descriptionText
                enableLights(true)
                enableVibration(true)
                vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
            }
            val notificationManageri: NotificationManager =context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManageri.createNotificationChannel(channel)
            notificationManageri.cancel(0)
        }



    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        isServiceRunning = true
/*
        Thread {
            while (isServiceRunning) {
                Log.d("MyBackgroundService", "Service is running")

                var url =
                    "https://pratikhasar.com/netting/vibrentlog/vib_mobil.php?tur=tum_isler"
                val queue: RequestQueue = Volley.newRequestQueue(this.applicationContext)
                Log.d("tum_islerim", url)
                val request = JsonObjectRequest(
                    Request.Method.GET, url, null,
                    { response ->
                        try {

                            val json = response["islerim"] as JSONArray
                            for (i in 0 until json.length()) {

                                val item = json.getJSONObject(i)

                                cat.add(item.getString("cat"))
                                id.add(item.getString("id").toInt())
                                nereden.add(item.getString("nereden"))
                                nereye.add(item.getString("nereye"))
                            }
                            intent?.let {
                                Bildirim(
                                    it,
                                    this.applicationContext,
                                    cat,
                                    id,
                                    nereden,
                                    nereye
                                )
                            }
                        } catch (e: Exception) {
                        }

                    }, { error ->
                        Log.e("TAG", "RESPONSE IS $error")

                    }
                )
                queue.add(request)

                try {
                    // İşlem aralığını belirlemek için bir süre uyutabilirsiniz
                    Thread.sleep(1000) // 1 saniye
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
        }.start()
*/ timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                var url =
                    "https://viprentlog.com/mobil/vib_mobil.php?tur=tum_isler"
                val queue: RequestQueue = Volley.newRequestQueue(applicationContext)
                Log.d("tum_islerim", url)
                val request = JsonObjectRequest(
                    Request.Method.GET, url, null,
                    { response ->
                        try {

                            val json = response["islerim"] as JSONArray
                            for (i in 0 until json.length()) {

                                val item = json.getJSONObject(i)

                                cat.add(item.getString("cat"))
                                id.add(item.getString("id").toInt())
                                nereden.add(item.getString("nereden"))
                                nereye.add(item.getString("nereye"))
                            }
                            intent?.let {
                                Bildirim(
                                    it,
                                    applicationContext,
                                    cat,
                                    id,
                                    nereden,
                                    nereye
                                )
                            }
                        } catch (e: Exception) {
                        }

                    }, { error ->
                        Log.e("TAG", "RESPONSE IS $error")

                    }
                )
                queue.add(request)

                // Arka planda yapılacak işlemleri burada gerçekleştirin
            }
        }, 0, 1000)
        return START_STICKY







        //super.onStartCommand(intent, flags, startId)
    }

    private fun arkaPlanBildirim() {
        TODO("Not yet implemented")
    }

}