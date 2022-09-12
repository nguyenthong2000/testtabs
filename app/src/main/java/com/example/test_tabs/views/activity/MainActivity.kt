package com.example.test_tabs.views.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.test_tabs.R
import com.example.test_tabs.models.Tabs
import com.example.test_tabs.views.fragment.WebFragment
import com.example.test_tabs.database.WebDAO
import com.example.test_tabs.database.WebDataBase

class MainActivity : AppCompatActivity() {
    private var db: WebDataBase? = null
    private var webDAO: WebDAO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        db = WebDataBase.getInstance(this)
        webDAO = db?.webDAO()
        val listTabs = webDAO?.selectAllTabs()
        if (listTabs!!.isNotEmpty()){
            for (tab in listTabs){
                if (tab.visibility){
                    supportFragmentManager.beginTransaction().replace(R.id.fcv, WebFragment(tab.id, tab.url)).commit()
                    break
                }
            }
            return
        }
        val id = System.currentTimeMillis()
        webDAO?.insertTab(
            Tabs(id,"", "https://www.google.com/", true)
        )
        supportFragmentManager.beginTransaction().replace(R.id.fcv, WebFragment(id, "https://www.google.com/")).commit()

    }
}