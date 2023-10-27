package com.selpar.vibrentlog

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.volley.toolbox.Volley
import com.selpar.vibrentlog.ui.HomeActivity
import org.json.JSONArray
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.selpar.vibrentlog.model.BildirimModel
import com.selpar.vibrentlog.servis.BroadcastReceiver
import com.selpar.vibrentlog.servis.KonumReceiver
import com.selpar.vibrentlog.servis.MyWorker
import com.selpar.vibrentlog.servis.SellerFirebaseService
import com.selpar.vibrentlog.ui.GorevEkleActivity
import com.selpar.vibrentlog.ui.IsBilgiActivity
import java.util.HashMap

class MainActivity : AppCompatActivity() {
    lateinit var buttonLogin:Button
    lateinit var editTextUsername:EditText
    lateinit var editTextPassword:EditText
    lateinit var kullaniciAdi:String
    lateinit var sifre:String
     var adi:String=""
     var soyadi:String=""
     var personel:String=""
    private lateinit var auth: FirebaseAuth
    lateinit var servis: SellerFirebaseService
    private var myBroadcastReceiver: BroadcastReceiver? = null
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        onBaslat()
        createNotification(this,"7")
        auth = Firebase.auth
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val token = task.result
                    Log.d("TOKEN22:",token)
                    Log.d("Personel:",personel)

                  //  updateToken(token)
                    // Use the device token as needed
                    // For example, send it to your server or store it locally
                } else {
                    // Handle token retrieval error
                }
            }
        val database=FirebaseDatabase.getInstance()
        val myRef=database.getReference("bildirim")
        val bildirim= BildirimModel("","Viprentlog","İçerik")
        myRef.push().setValue(bildirim)
        /*
        // Create a ChildEventListener
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Handle new child added event
                //val childData = dataSnapshot.getValue()

                // Perform operations with child data
               dataSnapshot.children.forEach() {
                    val childData = dataSnapshot.children.iterator()
                    // Perform operations with child data
                    Log.d("FCM", childData.toString())
                    sendNotification(this@MainActivity,"7",76,"ankara","İstanbul")
                }

            }


            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Handle child changed event
                val updatedChildData = dataSnapshot.getValue(BildirimModel::class.java)
                // Perform operations with updated child data
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                // Handle child removed event
                val removedChildData = dataSnapshot.getValue(BildirimModel::class.java)
                // Perform operations with removed child data
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                // Handle child moved event
                val movedChildData = dataSnapshot.getValue(BildirimModel::class.java)
                // Perform operations with moved child data
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
                println("Database Error: ${databaseError.message}")
            }
        }
*/
        // Create a ValueEventListener
        val database2 = FirebaseDatabase.getInstance().reference
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method will be called whenever the data at the specified database reference changes.
                // You can retrieve the updated values from the dataSnapshot parameter.
                //val value = dataSnapshot.getValue(String::class.java)
                // Do something with the retrieved value
               // println("Value updated: $value")
                sendNotification(this@MainActivity,"7",76,"ankara","İstanbul")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // This method will be called if the operation is canceled
                // You can handle the error here
                println("Error: ${databaseError.message}")
            }
        }

// Add the ValueEventListener to your database reference
        val databaseReference = database2.child("bildirim")
        databaseReference.addValueEventListener(valueEventListener)
// Attach the ChildEventListener to your DatabaseReference
      //  myRef.addChildEventListener(childEventListener)
        val sharedPreferens = getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)

        val getUsername = sharedPreferens.getString("kadi", "")
        val getPassword = sharedPreferens.getString("sifre", "")
        personel= sharedPreferens.getString("personel", "")!!
        val getpersonel = sharedPreferens.getString("personel", "")
        if (getUsername != "" && getPassword != "") {
            val i = Intent(this, GorevEkleActivity::class.java)
            i.putExtra("kadi", sharedPreferens.getString("kadi", "").toString())
            i.putExtra("sifre", sharedPreferens.getString("sifre", "").toString())
            i.putExtra("personel", sharedPreferens.getString("personel", "").toString())
            i.putExtra("adi", sharedPreferens.getString("adi", "").toString())
            i.putExtra("soyadi", sharedPreferens.getString("soyadi", "").toString())

            startActivity(i)
            finish()
            myBroadcastReceiver = BroadcastReceiver()

            val filter = IntentFilter()
            filter.addAction("android.intent.action.TIME_TICK")

            registerReceiver(myBroadcastReceiver, filter)
            val intent = Intent("android.intent.action.TIME_TICK")
// Ekstra veri eklemek için intent.putExtra() kullanabilirsiniz
            //sendBroadcast(intent)
        }
        buttonLogin.setOnClickListener {
            giris_yapin()

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED ||
                checkSelfPermission(
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
                ||
                checkSelfPermission(
                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_DENIED
                ||
                checkSelfPermission(
                    android.Manifest.permission.MANAGE_DOCUMENTS
                ) == PackageManager.PERMISSION_DENIED
                ||
                checkSelfPermission(
                    android.Manifest.permission_group.STORAGE
                ) == PackageManager.PERMISSION_DENIED
                ||
                checkSelfPermission(
                    android.provider.Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION
                ) == PackageManager.PERMISSION_DENIED
                ||
                checkSelfPermission(
                    android.os.storage.StorageManager.ACTION_MANAGE_STORAGE
                ) == PackageManager.PERMISSION_DENIED
                ||
                checkSelfPermission(
                    Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                ) == PackageManager.PERMISSION_DENIED
            ) {
                val permission = arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.MANAGE_DOCUMENTS,
                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE,
                    Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION,
                    StorageManager.ACTION_MANAGE_STORAGE,
                    android.Manifest.permission_group.STORAGE,
                    Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                )

                requestPermissions(permission, 1)
            } else {
                // openCamera()
            }

        } else {
            //api<23
            // openCamera()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R
            && !Environment.isExternalStorageManager()
        ) {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            val uri = Uri.fromParts("package", packageName, null)
            intent.data = uri
            startActivity(intent)
        }
    }

    private fun updateToken(token: String,personel:String) {
        val queue = Volley.newRequestQueue(this)
        var url = "https://viprentlog.com/mobil/vib_mobil.php"
        val postRequest: StringRequest = @SuppressLint("RestrictedApi")
        object : StringRequest(
            Method.POST, url,
            com.android.volley.Response.Listener { response -> // response
                Log.d("Response", response!!)
               Toast.makeText(this,"Token güncelleme başarılı",Toast.LENGTH_LONG).show()

            },
            com.android.volley.Response.ErrorListener {
                Log.d("Response", "HATALI")
            }
        ) {
            override fun getParams(): Map<String, String>? {
                val params: MutableMap<String, String> = HashMap()
                params["personel"] = personel
                params["token"] =token
                params["tur"] = "token_guncelle"
                return params
            }
        }
        queue.add(postRequest)

    }

    @SuppressLint("MissingPermission", "UnspecifiedImmutableFlag")
    private fun sendNotification( context:Context, cat:String,id:Int,nereden:String,nereye:String) {
        val i=Intent(this, IsBilgiActivity::class.java)
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
            val notificationManageri: NotificationManager =context.getSystemService(
                NOTIFICATION_SERVICE
            ) as NotificationManager
            notificationManageri.createNotificationChannel(channel)
            notificationManageri.cancel(0)
        }



    }

    override fun onStart() {
        val i=Intent(this,KonumReceiver::class.java)
        //startService(i)
       // ContextCompat.startForegroundService(this, i)

        super.onStart()
    }

    override fun onResume() {
        val i=Intent(this,KonumReceiver::class.java)
        //startService(i)
       // ContextCompat.startForegroundService(this, i)
        super.onResume()
    }

    override fun onDestroy() {
        val i=Intent(this,KonumReceiver::class.java)

        i.putExtra("personel",personel)
        i.putExtra("adi",adi)
        i.putExtra("soyadi",soyadi)
       // ContextCompat.startForegroundService(this, i)
        //startService(i)

       // val compressionWork = OneTimeWorkRequestBuilder<MyWorker>().build()
        ///WorkManager.getInstance(this).enqueue(compressionWork)

        super.onDestroy()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun giris_yapin() {
        if (editTextUsername.getText().toString().isNotEmpty() && editTextPassword.getText().toString()
                .isNotEmpty()
        ) {
            giris_yap(editTextUsername.getText().toString(), editTextPassword.getText().toString())

        } else {
            Toast.makeText(this, "Lütfen gerekli alanları doldurunuz!..", Toast.LENGTH_SHORT).show()
        }
    }   // Do something in response to button


    private fun onBaslat() {
        buttonLogin=findViewById(R.id.buttonLogin)
        editTextUsername=findViewById(R.id.editTextUsername)
        editTextPassword=findViewById(R.id.editTextPassword)
    }
    @SuppressLint("SuspiciousIndentation")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun giris_yap(k: String, s: String) {

        val urlsb = "&kadi=" + k + "&sifre=" + s
        var url = "https://viprentlog.com/mobil/vib_mobil.php?tur=giris" + urlsb
        Log.d("KABULBULLL: ", url)
        val queue: RequestQueue = Volley.newRequestQueue(this)
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val json = response["giris"] as JSONArray

                    for (i in 0 until json.length()) {
                        val item = json.getJSONObject(i)
                        kullaniciAdi = item.getString("kadi").toString()
                        sifre = item.getString("sifre").toString()
                        personel = item.getString("personel").toString()
                        adi = item.getString("adi").toString()
                        soyadi = item.getString("soyadi").toString()



                    }
                    auth.createUserWithEmailAndPassword(k,s)
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w("TAG", "getInstanceId failed", task.exception)
                                return@OnCompleteListener
                            }

                            // Get new Instance ID token
                            val token = task.result.credential

                            // Log and toast
                           // val msg = getString(R.string.msg_token_fmt, token)
                            Log.d("token", token.toString())
                        })
                   // val token = task.result?.token
                    val user = FirebaseAuth.getInstance().currentUser
                    if (user != null) {
                        user.getIdToken(

                            true)
                            .addOnCompleteListener { task ->


                                if (task.isSuccessful) {
                                    val token = task.result?.token
                                    Log.d("TOKEN: ",token.toString())
                                    updateToken(token.toString(),personel)
                                } }}

                } catch (e: Exception) {
                    Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
                }
                if (kullaniciAdi !="null") {
                              // Kullanıcı başarıyla oturum açtı
                                val sharedPreferens =
                                    getSharedPreferences("MY_PRE", Context.MODE_PRIVATE)
                    val editor = sharedPreferens.edit()
                                editor.putString("kadi", kullaniciAdi)
                                editor.putString("sifre", sifre)
                                editor.putString("personel", personel)
                                editor.putString("adi", adi)
                                editor.putString("soyadi", soyadi)

                                editor.apply()
                    val i=Intent(this,HomeActivity::class.java)
                    i.putExtra("personel",personel)
                    i.putExtra("adi",adi)
                    i.putExtra("soyadi",soyadi)
                    startActivity(i)
                    finish()




                } else {
            Log.d("API", "başarısız")
            Toast.makeText(this, R.string.kullanici_oturum_acmis, Toast.LENGTH_SHORT).show()


        }


            }, { error ->
                Log.e("TAG", "RESPONSE IS $error")
                // in this case we are simply displaying a toast message.
                Toast.makeText(
                    this,
                    error.message+"hata",
                    Toast.LENGTH_SHORT
                ).show()
                //Toast.makeText(this,"Böyle bir kullanıcı yok",Toast.LENGTH_LONG).show()

            }
        )
        val timeout = 10000 // 10 seconds in milliseconds
        request.retryPolicy = DefaultRetryPolicy(
            timeout,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        queue.add(request)
    }

}