package com.selpar.vibrentlog.ui

import android.R
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RemoteViews
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder: NotificationCompat.Builder
    private val channelId = "notifications"
    private val notificationId = 101
    private val description = "Test notification"
    lateinit var btn_bildirim:Button
    lateinit var personel:String
    @SuppressLint("MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.selpar.vibrentlog.R.layout.activity_home)
        personel=intent.getStringExtra("personel").toString()
        Toast.makeText(this,"personel: "+intent.getStringExtra("personel"),Toast.LENGTH_LONG).show()

        btn_bildirim=findViewById(com.selpar.vibrentlog.R.id.btn_bildirim)
        val bottomNavigationView: BottomNavigationView
        bottomNavigationView = findViewById<View>(com.selpar.vibrentlog.R.id.bottomNavigationView) as BottomNavigationView
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@HomeActivity)
        val menu = bottomNavigationView.menu
        this.onNavigationItemSelected(menu.findItem(com.selpar.vibrentlog.R.id.ic_home))
        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotification()
        btn_bildirim.setOnClickListener { sendNotification() }


    }

    @SuppressLint("MissingPermission", "UnspecifiedImmutableFlag")
    private fun sendNotification() {
        val intent=Intent(this,HomeActivity::class.java).apply {
            flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
       // val pendingIntent = PendingIntent.getActivity(this.applicationContext, 0, intent, PendingIntent.FLAG_ONE_SHOT)


        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val builder=NotificationCompat.Builder(this,channelId)
                .setSmallIcon(com.selpar.vibrentlog.R.drawable.img)
                .setContentTitle("afsfsdg")
                .setContentText("denemememem")
            with(NotificationManagerCompat.from(this)) {
                notify(notificationId, builder.build())
            }

        }else{
      val builder=NotificationCompat.Builder(this)
            .setSmallIcon(com.selpar.vibrentlog.R.drawable.img)
            .setContentTitle("afsfsdg")
            .setContentText("denemememem")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    with(NotificationManagerCompat.from(this)) {
        notify(notificationId, builder.build())
    }
}
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.selpar.vibrentlog.R.id.ic_job -> {
                val i = Intent(this, GorevEkleActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

                i.putExtra("personel",personel)
                i.putExtra("id",personel)

                i.putExtra("adi",intent.getStringExtra("adi"))
                i.putExtra("soyadi",intent.getStringExtra("soyadi"))
                this.startActivity(i)
                overridePendingTransition(0, 0)
                finish()
                true
            }
            com.selpar.vibrentlog.R.id.ic_profil -> {
                val i = Intent(this, ProfilActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
                i.putExtra("personel",personel)
                i.putExtra("id",personel)
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
                i.putExtra("id",personel)
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
                i.putExtra("id",personel)
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
    @SuppressLint("RemoteViewLayout")
    fun createNotification(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val name="Notification title"
            val descriptionText="Notification Description"
            val importance=NotificationManager.IMPORTANCE_DEFAULT
            val channel=NotificationChannel(channelId,name,importance).apply {
                description=descriptionText
            }
            val notificationManageri:NotificationManager=getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManageri.createNotificationChannel(channel)
        }

    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun Not(){
        val descriptionText = description
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val mChannel = NotificationChannel("1", "test", importance)
        mChannel.description = descriptionText
        // Register the channel with the system. You can't change the importance
        // or other notification behaviors after this.
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(mChannel)
    }

    }

