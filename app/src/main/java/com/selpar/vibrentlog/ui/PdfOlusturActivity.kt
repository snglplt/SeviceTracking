package com.selpar.vibrentlog.ui

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.github.barteksc.pdfviewer.PDFView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.zxing.BarcodeFormat
import com.selpar.vibrentlog.PdfDocumentAdapter
import com.selpar.vibrentlog.R
import org.json.JSONArray
import java.io.File
import com.google.zxing.qrcode.QRCodeWriter
import com.squareup.picasso.Picasso

class PdfOlusturActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {
    private lateinit var myImageView: ImageView
    private lateinit var imagePrint: ImageView
    private lateinit var imgeWhatsapp: ImageView
    private lateinit var image_save: ImageView
    lateinit var Pdfviewer:PDFView
    lateinit var qrcode: Bitmap
    var hizmettipi:String=""
     var atarihi:String=""
     var asaati:String=""
     var nereden:String=""
     var nereye:String=""
     var aractipi:String=""
     var iveren:String=""
     var itutari:String=""
     var odemetipi:String=""
     var neredenkonum:String=""
     var nereyekonum:String=""
     var neredenadres:String=""
     var nereyeadres:String=""
     var firmaid:String=""
     var resimyol:String=""
     var adi:String=""
     var soyadi:String=""
     var telefon:String=""
     var mail:String=""
    var pageHeight = 1120
    var pageWidth = 792
    var PERMISSION_CODE = 101
    lateinit var scaledbmp: Bitmap
    var qrEncoder: QRCodeWriter= QRCodeWriter()
    lateinit var imagelogo:ImageView
    lateinit var aracbmp: Bitmap

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf_olustur)
        myImageView = findViewById(R.id.myImageView)
        Pdfviewer = findViewById(R.id.pdfView)
        imagePrint = findViewById(R.id.imagePrint)
        imgeWhatsapp = findViewById(R.id.imgeWhatsapp)
        image_save = findViewById(R.id.image_save)
        imagelogo = findViewById(R.id.imagelogo)
        alertOdemeTutari()


        // Resmi ekrana getirme
        val imzam=intent.getStringExtra("imzam")
        val imagePath = "/storage/emulated/0/Pictures/"+imzam+".jpg" // Resmin dosya yolu
        val bitmap = BitmapFactory.decodeFile(imagePath)
        myImageView.setImageBitmap(bitmap)
        val bottomNavigationView: BottomNavigationView
        bottomNavigationView = findViewById<View>(com.selpar.vibrentlog.R.id.bottomNavigationView) as BottomNavigationView
        //bottomNavigationView.setOnNavigationItemSelectedListener(myNavigationItemListener)
        bottomNavigationView.setOnNavigationItemSelectedListener(this@PdfOlusturActivity)

        val menu = bottomNavigationView.menu
        imgeWhatsapp.setOnClickListener {
            val path= Environment.getExternalStorageDirectory().path+"/GOREV.pdf"
            // val path= "backup_rules.xml"
            //  val path= getExternalFilesDir(null)!!.absolutePath.toString()+"/HASAR.pdf",
            val file = File(Environment.getExternalStorageDirectory(), "GOREV.pdf")
            val dosyaYolu = File("/storage/emulated/0/GOREV.pdf")
            val dosyaUri = FileProvider.getUriForFile(this, "com.selpar.vibrentlog.fileprovider", dosyaYolu)

            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "application/pdf"
            intent.putExtra(Intent.EXTRA_STREAM, dosyaUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.setPackage("com.whatsapp")

            startActivity(intent)

        }
        image_save.setOnClickListener {
            //val file = File("file:///storage/emulated/0/GOREV.pdf")


            downloadPdfFromLocal()
        }
        imagePrint.setOnClickListener {
            val printmanager: PrintManager = getSystemService(Context.PRINT_SERVICE) as PrintManager

            val printDocumentAdapter= PdfDocumentAdapter()
            printmanager.print("Document",printDocumentAdapter, PrintAttributes.Builder().build())


        }
        if (checkPermissions()) {
            // if permission is granted we are displaying a toast message.
            //  Toast.makeText(this, "İzin verildi..", Toast.LENGTH_SHORT).show()

        } else {
            // if the permission is not granted
            // we are calling request permission method.
            requestPermission()
        }

        //this.onNavigationItemSelected(menu.findItem(com.selpar.vibrentlog.R.id.ic_job))


    }

    private fun alertOdemeTutari() {
        val builder = AlertDialog.Builder(this)
        var cevap=""
        builder.setTitle("Uyarı")
            .setMessage("Ödeme gösterilsin mi?")
            .setPositiveButton("Evet") { dialog, which ->
                cevap="Evet"
                bilgiDoldur(cevap)
                // Positive button click handler
            }
            .setNegativeButton("Hayır") { dialog, which ->
                cevap="Hayır"
                bilgiDoldur(cevap)
            }

        val dialog = builder.create()
        dialog.show()

    }

    private fun downloadPdfFromLocal() {
        //val sourcePath = "/storage/emulated/0/GOREV.pdf" // Yerel PDF dosyasının yolu
        val sourceUri = Uri.fromFile(File("/storage/emulated/0/GOREV.pdf")) // Local PDF file URI
        val destinationPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/GOREV.pdf" // Destination download path

        val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val request = DownloadManager.Request(sourceUri)

        request.setDestinationUri(Uri.parse(destinationPath))
        request.setTitle("PDF Download")
        request.setDescription("Downloading PDF")

        val downloadId = downloadManager.enqueue(request)

        Toast.makeText(this, "PDF download started", Toast.LENGTH_SHORT).show()


    }    fun checkPermissions(): Boolean {
        // on below line we are creating a variable for both of our permissions.

        // on below line we are creating a variable for
        // writing to external storage permission
        var writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

        // on below line we are creating a variable
        // for reading external storage permission
        var readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    // on below line we are creating a function to request permission.
    fun requestPermission() {

        // on below line we are requesting read and write to
        // storage permission for our application.
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PERMISSION_CODE
        )
    }

    @SuppressLint("SuspiciousIndentation")
    private fun bilgiDoldur(cevap: String) {
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
                        if(item.getString("hizmettipi")!="null")
                        hizmettipi=item.getString("hizmettipi")
                        if(item.getString("atarihi")!="null")
                        atarihi=item.getString("atarihi")
                        if(item.getString("asaati")!="null")
                        asaati=item.getString("asaati")
                        if(item.getString("nereden")!="null")
                        nereden=item.getString("nereden")
                        if(item.getString("nereye")!="null")
                        nereye=item.getString("nereye")
                        if(item.getString("aractipi")!="null")
                        aractipi=item.getString("aractipi")
                        if(item.getString("iveren")!="null")
                        iveren=item.getString("iveren")
                        if(item.getString("itutari")!="null")
                        itutari=item.getString("itutari")
                        if(item.getString("odemetipi")!="null")
                        odemetipi=item.getString("odemetipi")
                        if(item.getString("neredenkonum")!="null")
                        neredenkonum=item.getString("neredenkonum")
                        if(item.getString("nereyekonum")!="null")
                        nereyekonum=item.getString("nereyekonum")
                        if(item.getString("neredenadres")!="null")
                        neredenadres=item.getString("neredenadres")
                        if(item.getString("nereyeadres")!="null")
                        nereyeadres=item.getString("nereyeadres")
                        if(item.getString("adi")!="null"){
                            adi=item.getString("adi")
                        }else{
                            adi=item.getString("mbilgileri")
                        }
                        firmaid=item.getString("firmaid")
                        resimyol=item.getString("logo")
                        telefon=item.getString("telefon")
                        mail=item.getString("mail")


                        //soyadi=item.getString("soyadi")






                    }
                    generatePDF(hizmettipi,atarihi,asaati,nereden,nereye,aractipi,iveren,itutari,odemetipi,neredenkonum,nereyekonum,neredenadres,nereyeadres,adi,cevap)
                } catch (e: Exception) {
                    //Toast.makeText(this,e.message,Toast.LENGTH_LONG).show()
                    bilgiDoldur(cevap)
                }

            }, { error ->
                Log.e("TAG", "RESPONSE IS $error")

            }
        )
        queue.add(request)



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
    fun generatePDF(
        hizmettipi: String,
        atarihi: String,
        asaati: String,
        nereden: String,
        nereye: String,
        aractipi: String,
        iveren: String,
        itutari: String,
        odemetipi: String,
        neredenkonum: String,
        nereyekonum: String,
        neredenadres: String,
        nereyeadres: String,
        adi: String,
        cevap: String
    ) {
        Picasso.get().load("https://viprentlog.com/resimler/logo/"+firmaid+"/"+resimyol).into(imagelogo)
        val bitmapDrawable_logo=imagelogo.drawable as BitmapDrawable
        val arababbitmap_logo=bitmapDrawable_logo.bitmap
        aracbmp= Bitmap.createScaledBitmap(arababbitmap_logo, 200, 80, false)



        val bitmapDrawable=myImageView.drawable as BitmapDrawable
        val arababbitmap=bitmapDrawable.bitmap
        scaledbmp = Bitmap.createScaledBitmap(arababbitmap, 250, 100, false)


        // creating an object variable
        // for our PDF document.
        var pdfDocument: PdfDocument = PdfDocument()

        // two variables for paint "paint" is used
        // for drawing shapes and we will use "title"
        // for adding text in our PDF file.
        var paint: Paint = Paint()
        var title: Paint = Paint()
        var plaka: Paint = Paint()
        var arac_bilgi: Paint = Paint()
        var arac_bilgileri: Paint = Paint()
        var arac_sahibi: Paint = Paint()
        var pdf_baslik: Paint = Paint()
        var arac_bilgileri_renk: Paint = Paint()
        var toplubilgi: Paint = Paint()
        var arac_bilgileri2: Paint = Paint()
        var toplu: Paint = Paint()
        var toplu2: Paint = Paint()
        var km: Paint = Paint()
        var textblue: Paint = Paint()
        var usta_bilgi: Paint = Paint()



        // we are adding page info to our PDF file
        // in which we will be passing our pageWidth,
        // pageHeight and number of pages and after that
        // we are calling it to create our PDF.
        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()

        // below line is used for setting
        // start page for our PDF file.
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)

        // creating a variable for canvas
        // from our page of PDF.
        var canvas: Canvas = myPage.canvas

        // below line is used to draw our image on our PDF file.
        // the first parameter of our drawbitmap method is
        // our bitmap
        // second parameter is position from left
        // third parameter is position from top and last
        // one is our variable for paint.
        canvas.drawBitmap(scaledbmp, 510F, 900F, paint)
        // canvas.drawBitmap(bitmap, 600F, 0F, paint)

        // below line is used for adding typeface for
        // our text which we will be adding in our PDF file.
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
        plaka.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        arac_bilgi.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        arac_bilgileri.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
        toplubilgi.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        arac_bilgileri2.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
        toplu.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        km.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))
        usta_bilgi.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
        textblue.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD))




        // below line is used for setting text size
        // which we will be displaying in our PDF file.
        title.textSize = 15F
        plaka.textSize=36F
        arac_bilgi.textSize=12F
        km.textSize=12F
        arac_bilgi.color= Color.RED
        arac_bilgileri.textSize=12F
        arac_bilgileri_renk.textSize=12F
        arac_bilgileri_renk.color= Color.GRAY
        arac_sahibi.textSize=15F
        pdf_baslik.textSize=20f
        toplubilgi.textSize=12F
        arac_bilgileri2.textSize=12F
        toplu.textSize=12F
        toplu2.textSize=12F
        textblue.textSize=12f
        usta_bilgi.textSize=12f
        // qrOlustur()





        // below line is sued for setting color
        // of our text inside our PDF file.
        //title.setColor(ContextCompat.getColor(this, R.color.green))

        // below line is used to draw text in our PDF file.
        // the first parameter is our text, second parameter
        // is position from start, third parameter is position from top
        // and then we are passing our variable of paint which is title.
        canvas.drawBitmap(aracbmp, 150F, 5F, paint)
        canvas.drawText("İŞ KABUL ",450F,30F,pdf_baslik)
        canvas.drawText("HESAP DÖKÜMÜ",420f,60F,pdf_baslik)
        canvas.drawLine(140f,100f,140f,365f,paint)
        canvas.drawLine(40f,100f,40f,365f,paint)
        canvas.drawLine(780f,100f,780f,365f,paint)
        canvas.drawLine(40f,100f,780f,100f,paint)
        canvas.drawText("Hizmet Tipi ",50f,120f,paint)
        canvas.drawText(hizmettipi,150f,120f,paint)
        canvas.drawLine(40f,125f,780f,125f,paint)

        canvas.drawText("Tarih ",50f,140f,paint)

        try{
            val bul_tarih =atarihi.split("-")
            canvas.drawText(bul_tarih[2]+"-"+bul_tarih[1]+"-"+bul_tarih[0],150f,140f,paint)
        }catch (e:Exception){
            canvas.drawText(atarihi,150f,140f,paint)
        }

        canvas.drawLine(40f,145f,780f,145f,paint)
        canvas.drawText("Saat ",50f,160f,paint)
        canvas.drawText(asaati,150f,160f,paint)
        canvas.drawLine(40f,165f,780f,165f,paint)
        canvas.drawText("Nereden ",50f,180f,paint)
        canvas.drawText(nereden,150f,180f,paint)
        canvas.drawLine(40f,185f,780f,185f,paint)
        canvas.drawText("Nereye ",50f,200f,paint)
        canvas.drawText(nereye,150f,200f,paint)
        canvas.drawLine(40f,205f,780f,205f,paint)
        canvas.drawText("Araç Tipi ",50f,220f,paint)
        canvas.drawText(aractipi,150f,220f,paint)
        canvas.drawLine(40f,225f,780f,225f,paint)
        canvas.drawText("İş Veren ",50f,240f,paint)
        canvas.drawText(iveren,150f,240f,paint)
        canvas.drawLine(40f,245f,780f,245f,paint)
        canvas.drawText("İş Tutarı ",50f,260f,paint)
        if(cevap=="Evet"){
            canvas.drawText(itutari,150f,260f,paint)
        }else{
            canvas.drawText("Belirtilmemiş",150f,260f,paint)
        }

        canvas.drawLine(40f,265f,780f,265f,paint)
        canvas.drawText("Ödeme Tipi ",50f,280f,paint)
        canvas.drawText(odemetipi,150f,280f,paint)
        canvas.drawLine(40f,285f,780f,285f,paint)
        canvas.drawText("Nereden Konum ",50f,300f,paint)
        canvas.drawText(neredenkonum,150f,300f,paint)
        canvas.drawLine(40f,305f,780f,305f,paint)
        canvas.drawText("Nereden Adres ",50f,320f,paint)
        canvas.drawText(neredenadres,150f,320f,paint)
        canvas.drawLine(40f,325f,780f,325f,paint)
        canvas.drawText("Nereye Konum ",50f,340f,paint)
        canvas.drawText(nereyekonum,150f,340f,paint)
        canvas.drawLine(40f,345f,780f,345f,paint)
        canvas.drawText("Nereye Adres ",50f,360f,paint)
        canvas.drawText(nereyeadres,150f,360f,paint)
        canvas.drawLine(40f ,365f,780f,365f,paint)
        canvas.drawText("Bizi tercih ettiğiniz için teşekkür ederiz.",100f,400f,paint)
        canvas.drawText("Bir sonraki seferde görüşmek üzere..",310f,400f,paint)


        paint.setTypeface(Typeface.create(Typeface.DEFAULT_BOLD, Typeface.BOLD))

        paint.textSize=50F
        // canvas.drawLine(670f,530F,670f,740f,paint)


        //toplam tutar

        var x_stok=25f
        var y_stok=570f
        var x_aciklama=140f
        var y_aciklama=570f
        var x_miktar=510f
        var y_miktar=570f
        var x_fiyat=570f
        var y_fiyat=570f
        var x_tutar=680f
        var y_tutar=570f
        var x_kdv=640f
        var y_kdv=570f
        var x_line=10F
        var y_line=575F
        //green line
        arac_bilgileri.textAlign = Paint.Align.LEFT
        toplu.textAlign = Paint.Align.RIGHT
        toplu2.textAlign = Paint.Align.RIGHT

        canvas.drawText("Teslim Eden ",100f,900f,arac_bilgileri)
        canvas.drawText("Firma Adi",100f,920f,arac_bilgileri)
        canvas.drawText("Telefon: "+telefon,100f,940f,arac_bilgileri)
        canvas.drawText("Mail: "+mail,100f,960f,arac_bilgileri)
        canvas.drawText("Teslim Alan ",570f,900f,arac_bilgileri)
        canvas.drawText(adi,520f,920f,arac_bilgileri)
       // canvas.drawText(soyadi,570f,920f,arac_bilgileri)
        //canvas.drawText("Teslim Alan ",50f,1000f,arac_bilgileri)
        canvas.drawText("İş bu rapor ",70f,1090f,arac_bilgileri)
        textblue.color= Color.BLUE
        canvas.drawText("www.viprentlog.com",140f,1090f,textblue)
        canvas.drawText("yazılımından bilgi amaçlı olarak alınmıştır. ",260f,1090f,arac_bilgileri)
        canvas.drawText("Kanunen bir değeri yoktur. ",70f,1110f,arac_bilgileri)
        val bitMatrix=qrEncoder.encode("https://viprentlog.com",
            BarcodeFormat.QR_CODE,512,512)
        val width=bitMatrix.width
        val height=bitMatrix.height
        val bmp=Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)
        for(x in 0 until width){
            for(y in 0 until  height){
                bmp.setPixel(x,y,if(bitMatrix[x,y]) Color.BLACK else Color.WHITE)
            }

        }
        qrcode= Bitmap.createScaledBitmap(bmp, 100, 100, false)
        canvas.drawBitmap(qrcode, 600F, 1030f, paint)
        pdfDocument.finishPage(myPage)

        // below line is used to set the name of
        // our PDF file and its path.
        val file: File = File(Environment.getExternalStorageDirectory(), "GOREV.pdf")
        try {
            // after creating a file name we will
            // write our PDF file to that location.

            pdfDocument.writeTo(file.outputStream())
            Pdfviewer.fromFile(file).enableSwipe(true).swipeHorizontal(false).load()

            //pdfDocument.writeTo(FileOutputStream(file))

            // on below line we are displaying a toast message as PDF file generated..
            Toast.makeText(applicationContext, "PDF dosyası oluşturuldu..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            // below line is used
            // to handle error
            e.printStackTrace()
            Log.d("TAG",e.message.toString())

            /* on below line we are displaying a toast message as fail to generate PDF
            Toast.makeText(applicationContext, "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()*/
        }
        // after storing our pdf to that
        // location we are closing our PDF file.
        //pdfDocument.close()
    }
}