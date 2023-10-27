package com.selpar.vibrentlog

import android.os.Bundle
import android.os.CancellationSignal
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import java.io.*


public class PdfDocumentAdapter : PrintDocumentAdapter() {

    override fun onLayout(
        printAtributes1: PrintAttributes?,
        printAtributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        layoutResultcall: LayoutResultCallback?,
        p4: Bundle?
    ) {
        if(cancellationSignal!!.isCanceled){
            layoutResultcall!!.onLayoutCancelled()
        }
        else{
            val builder=PrintDocumentInfo.Builder("pdf ismi")
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .build()
            layoutResultcall!!.onLayoutFinished(builder.build(),printAtributes1!!.equals(printAtributes))
        }
    }

    override fun onWrite(
        p0: Array<out PageRange>?,
        parcelFileDescriptor: ParcelFileDescriptor?,
        cansellationSignal: CancellationSignal?,
        writeResultCallback: WriteResultCallback?
    ) {

        var inn:InputStream
        var out:OutputStream
        try{
            val file= File(Environment.getExternalStorageDirectory(), "GOREV.pdf")
            inn=FileInputStream(file)

            val mInput: InputStream = FileInputStream(file)
            out=FileOutputStream(parcelFileDescriptor!!.fileDescriptor)
            val buff = ByteArray(16384)
            var size:Int

            while (mInput.read(buff).also { size = it } > 0 && !cansellationSignal!!.isCanceled) {
                out.write(buff,0,size)
            }

            if(cansellationSignal!!.isCanceled)
                writeResultCallback!!.onWriteCancelled()
            else{
                writeResultCallback!!.onWriteFinished(arrayOf(PageRange.ALL_PAGES))
            }
        }catch (e:Exception){}
    }
}