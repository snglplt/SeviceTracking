package com.selpar.vibrentlog.servis

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.selpar.vibrentlog.ui.IsBilgiActivity
import org.json.JSONArray
import java.util.Calendar
import java.util.Timer
import java.util.TimerTask

class BroadcastReceiver : BroadcastReceiver() {
    private val timer = Timer()
    val nereden = ArrayList<String>()
    val nereye = ArrayList<String>()
    val id = ArrayList<Int>()
    val cat = ArrayList<String>()
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_TIME_CHANGED) {
            // Saat değiştiğinde yapılacak işlemler burada gerçekleştirilir


                    var url =
                        "https://viprentlog.com/mobil/vib_mobil.php?tur=tum_isler"
                    val queue: RequestQueue = Volley.newRequestQueue(context)
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
                                        context,
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
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)


        }
    }
    private fun Bildirim(intent:Intent,context:Context,cat: ArrayList<String>, id: ArrayList<Int>, nereden: ArrayList<String>, nereye: ArrayList<String>) {
        for (i in cat.indices){
            createNotification(context,cat[i])
            sendNotification(intent,context,cat[i],id[i],nereden[i],nereye[i])
        }


    }
    @SuppressLint("MissingPermission", "UnspecifiedImmutableFlag")
    private fun sendNotification(intent:Intent, context:Context, cat:String,id:Int,nereden:String,nereye:String) {
        val i=Intent(context, IsBilgiActivity::class.java)
        i.addFlags(
            Intent.FLAG_ACTIVITY_CLEAR_TOP or
                    Intent.FLAG_ACTIVITY_SINGLE_TOP or
                    Intent.FLAG_ACTIVITY_NEW_TASK
        )
        i.putExtra("id",id.toString())
        i.putExtra("personel",id.toString())
        i.putExtra("cat",cat)
        val pendingIntent = PendingIntent.getActivity(context, 0, i,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)


        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
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
            val notificationManageri: NotificationManager =context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager
            notificationManageri.createNotificationChannel(channel)
            notificationManageri.cancel(0)
        }



    }


