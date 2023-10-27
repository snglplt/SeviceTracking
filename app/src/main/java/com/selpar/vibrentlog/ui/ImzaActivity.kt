package com.selpar.vibrentlog.ui

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.media.MediaScannerConnection
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.selpar.vibrentlog.R
import org.json.JSONArray
import java.io.OutputStream
import java.util.Random

var mPaint: Paint? = null

class ImzaActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {
    lateinit var btn_tikla: Button
    lateinit var silButton: Button
    lateinit var container: LinearLayout
    lateinit var txtmusteri:TextView


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.selpar.vibrentlog.R.layout.activity_imza)


       btn_tikla = findViewById(R.id.saveButton)
        silButton = findViewById(R.id.silButton)
        container = findViewById(R.id.container)
        txtmusteri = findViewById(R.id.txtmusteri)
        bilgiDoldur()
        val view1 = MyView(this)
        var b: Bitmap? = view1.getDrawingCache()
        //setContentView(view1)
        container.addView(view1)
        btn_tikla.setOnClickListener {
            saveDrawing(view1)
        }
        silButton.setOnClickListener {
            view1.clearDrawing(view1)
        }




        mPaint = Paint()
        mPaint!!.setAntiAlias(true)
        mPaint!!.setDither(true)
        mPaint!!.setColor(-0x10000)
        mPaint!!.setStyle(Paint.Style.STROKE)
        mPaint!!.setStrokeJoin(Paint.Join.ROUND)
        // mPaint.setStrokeCap(Paint.Cap.ROUND);
        // mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint!!.setStrokeWidth(10f)
        mPaint!!.color=Color.BLUE
        val bottomNavigationView: BottomNavigationView
        bottomNavigationView = findViewById<View>(com.selpar.vibrentlog.R.id.bottomNavigationView) as BottomNavigationView
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@ImzaActivity)

        val menu = bottomNavigationView.menu
        this.onNavigationItemSelected(menu.findItem(com.selpar.vibrentlog.R.id.ic_job))


    }
    private fun bilgiDoldur() {
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

                        if(item.getString("adi")!="null"){
                            txtmusteri.setText("Sayın "+item.getString("adi")+", imzalamanız gerekiyor")
                        }else{
                            txtmusteri.setText("Sayın "+item.getString("mbilgileri")+", imzalamanız gerekiyor")
                        }


                        //soyadi=item.getString("soyadi")






                    }
                } catch (e: Exception) {
                }

            }, { error ->
                Log.e("TAG", "RESPONSE IS $error")

            }
        )
        queue.add(request)



    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

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



    private fun saveDrawing(view: MyView) {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.BLACK)
        view.draw(canvas)
        val random = Random()
        val randomNumber = random.nextInt()
        val imzam="imzam_"+randomNumber
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, imzam)
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }

        val resolver = contentResolver
        var outputStream: OutputStream? = null
        var imageUri: String? = null

        try {
            val collection =
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            val imageUri = resolver.insert(collection, contentValues)
            outputStream = imageUri?.let { resolver.openOutputStream(it) }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream?.flush()
            Toast.makeText(this, "Çizim kaydedildi.", Toast.LENGTH_SHORT).show()
            val i=Intent(this,PdfOlusturActivity::class.java)
            i.putExtra("cat",intent.getStringExtra("cat"))
            i.putExtra("id",intent.getStringExtra("id"))
            i.putExtra("imzam",imzam)
            i.putExtra("personel",intent.getStringExtra("personel"))
            i.putExtra("adi",intent.getStringExtra("adi"))
            i.putExtra("soyadi",intent.getStringExtra("soyadi"))
            startActivity(i)
        } catch (e: Exception) {
            Toast.makeText(this, "Çizim kaydedilemedi.", Toast.LENGTH_SHORT).show()
        } finally {
            outputStream?.close()

            // Galeriye eklenmesini sağlamak için tarama yapılır
            imageUri?.let {
                MediaScannerConnection.scanFile(this, arrayOf(it), null, null)
            }
        }
    }

    // Bitmap'i kaydetmek için işlemleri gerçekleştirin
    // Örneğin, galeriye kaydetmek için MediaStore kullanabilirsiniz
    // Kaydedildikten sonra kullanıcıya geri bildirim gösterebilirsiniz
}


class MyView(c: Context?) : View(c) {
    lateinit var btn_tikla: Button


    private lateinit var mBitmap: Bitmap
    private var mCanvas: Canvas? = null
    private var mPath: Path
    private var mBitmapPaint: Paint
    private lateinit var canvas: Canvas
    private lateinit var bitmap: Bitmap
    private lateinit var paint: Paint

    init {
        paint = Paint()
        paint.color = Color.BLUE

        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 5f
        /// btn_tikla=findViewById(R.id.btn_tikla)
    }
    fun clearDrawing(view1:MyView) {
        view1.setBackgroundColor(Color.WHITE)

        // CLEAR CANVAS
        view1.mCanvas!!.drawColor(Color.WHITE);
        view1.mCanvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        view1.mCanvas!!.drawColor(Color.TRANSPARENT, PorterDuff.Mode.MULTIPLY)
        view1.draw(Canvas())

        mPath.reset()
        invalidate()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        mCanvas = Canvas(mBitmap)
    }

    @SuppressLint("ResourceAsColor")
    override fun onDraw(canvas: Canvas) {
        canvas.drawColor(Color.WHITE)
        // canvas.drawLine(mX, mY, Mx1, My1, mPaint);
        // canvas.drawLine(mX, mY, x, y, mPaint);
        canvas.drawBitmap(mBitmap!!, 0f, 0f, mBitmapPaint)
        canvas.drawPath(mPath, mPaint!!)
    }

    private var mX = 0f
    private var mY = 0f

    init {
        mPath = Path()
        mBitmapPaint = Paint(Paint.DITHER_FLAG)
    }

    private fun touch_start(x: Float, y: Float) {
        bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8)
        canvas = Canvas(bitmap)
        canvas.drawColor(Color.BLUE)
        canvas.drawPoint(x, y, paint)
        mPath.reset()
        mPath.moveTo(x, y)
        mX = x
        mY = y
    }

    private fun touch_move(x: Float, y: Float) {
        canvas.drawPoint(x, y, paint)
        canvas.drawColor(Color.BLUE)
        val dx = Math.abs(x - mX)
        val dy = Math.abs(y - mY)
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x
            mY = y
        }
    }

    private fun touch_up() {
        mPath.lineTo(mX, mY)
        // commit the path to our offscreen
        mCanvas!!.drawPath(mPath, mPaint!!)
        // kill this so we don't double draw
        mPath.reset()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.action
        val x = event.x
        val y = event.y
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                touch_start(x, y)
                invalidate()
            }

            MotionEvent.ACTION_MOVE -> {
                touch_move(x, y)
                invalidate()
            }

            MotionEvent.ACTION_UP -> {
                touch_up()
                //   Mx1=(int) event.getX();
                //  My1= (int) event.getY();
                invalidate()
            }
        }
        return true
    }

    companion object {
        private const val MINP = 0.25f
        private const val MAXP = 0.75f
        private const val TOUCH_TOLERANCE = 4f
    }
}

