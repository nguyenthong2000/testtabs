package com.example.test_tabs.views.fragment

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import androidx.core.view.drawToBitmap
import androidx.fragment.app.Fragment
import com.example.test_tabs.R
import com.example.test_tabs.models.Tabs
import com.example.test_tabs.database.WebDAO
import com.example.test_tabs.database.WebDataBase
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class WebFragment(val id: Long,val url: String) : Fragment() {
    private var db: WebDataBase? = null
    private var webDAO: WebDAO? = null
    private var webView: WebView? = null
    lateinit var viewWeb: View
    lateinit var btnTabsWeb: Button
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewWeb = inflater.inflate(R.layout.fragment_web, container, false)


        db = WebDataBase.getInstance(requireContext())
        webDAO = db?.webDAO()

        btnTabsWeb = viewWeb.findViewById(R.id.btnTabsWeb)
        btnTabsWeb.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fcv, TabsFragment()).commit()

        }

        webView = viewWeb.findViewById(R.id.webView)
        webView!!.settings.javaScriptEnabled = true
        webView!!.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                view?.loadUrl(request?.url.toString())
                return false
            }

        }
        webView!!.loadUrl(url)



        return viewWeb
    }

    private fun addBorder(bmp: Bitmap): Bitmap? {
        val width : Int = bmp.width
        val height : Int = bmp.height

        val bmpWithBorder =
           Bitmap.createBitmap(width+10, height+10, bmp.config)
        val canvas = Canvas(bmpWithBorder)
        val paint = Paint()
        paint.strokeWidth = 5f
        paint.color = Color.BLACK
        canvas.drawLine(0f,0f,width.toFloat(), 0f, paint)
        canvas.drawLine(width.toFloat(), 0f, width.toFloat(), height.toFloat(), paint)
        canvas.drawLine(width.toFloat(),height.toFloat(),0f, height.toFloat(),paint)
        canvas.drawLine(0f, height.toFloat(),0f,0f,paint)
        canvas.drawBitmap(bmp,5f,5f,null)
        return bmpWithBorder
    }


    private fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    private fun saveImage(id: Long, view: View): String?{
        val bitmap = addBorder(webView!!.drawToBitmap()) //Bitmap.createBitmap(addBorder(view.drawToBitmap())!!, 0,dpToPx(60),view.width+10, view.height+10 -dpToPx(60))
        val folder = File(requireContext().filesDir.path +"/image")
        if (!folder.exists()){
            folder.mkdir()
        }
        val file = File(folder, "${System.currentTimeMillis()}.jpg")
        //final File file = new File(Environment.getExternalStorageDirectory() + "/" + this.cameraDevice.getId() + "_pic.jpg");
        try {
            val out = FileOutputStream(file)
            bitmap?.compress(Bitmap.CompressFormat.JPEG,100,out)
            Log.e(javaClass.name,file.path)
            return file.path

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return ""

    }

    override fun onResume() {
        super.onResume()
        Log.e(javaClass.name, "onResume: ", )
        webDAO?.updateTab(Tabs(id,"", url, true))
        btnTabsWeb.text = webDAO!!.selectAllTabs().size.toString()

    }

    override fun onPause() {
        super.onPause()
        val path = saveImage(id, requireView())
        val url = webView?.url
        webDAO?.updateTab(Tabs(id,path!!, url!!, false))
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(javaClass.name, "onDestroy: ", )
    }
}