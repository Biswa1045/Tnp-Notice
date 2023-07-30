package com.biswa1045.tnpnotice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File


class PDFViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfview)
        // Create the page renderer for the PDF document.
        val documentFile= intent.getStringExtra("uri")
        Toast.makeText(this, "$documentFile", Toast.LENGTH_SHORT).show()
       Log.e("pdf","$documentFile")

        val path = File(filesDir, "dl")
        val file = File(path, documentFile)

        val uri = FileProvider.getUriForFile(this, "com.biswa1045.tnpnotice" + ".fileprovider", file)
        val mime = contentResolver.getType(uri)

        // Open file with user selected app

        // Open file with user selected app
        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.setDataAndType(uri, mime)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        startActivity(intent)
       /* try{
            val file = File(documentFile.toString())
            val fileDescriptor = ParcelFileDescriptor.open( file, ParcelFileDescriptor.MODE_READ_ONLY)
            val pdfRenderer = PdfRenderer(fileDescriptor)
            val page = pdfRenderer.openPage(1)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pdfRenderer.close()
            val img = findViewById<ImageView>(R.id.pdf);
            img.setImageBitmap(bitmap)
        }catch (e:Exception){*/



    }

    fun back(view: View) {}
    fun share(view: View) {}
}