package com.example.test_tabs.views.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.dingmouren.layoutmanagergroup.echelon.EchelonLayoutManager
import com.example.test_tabs.*
import com.example.test_tabs.database.WebDAO
import com.example.test_tabs.database.WebDataBase
import com.example.test_tabs.interfaces.OnClickTabs
import com.example.test_tabs.models.Tabs
import com.example.test_tabs.views.adapter.RecyclerViewAdapter
import com.example.test_tabs.viewmodels.WebViewModel


class TabsFragment : Fragment(), OnClickTabs {
    private val webViewModel: WebViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    lateinit var recyclerViewAdapter: RecyclerViewAdapter
    lateinit var echelonLayoutManager: EchelonLayoutManager
    private var db: WebDataBase? = null
    private var webDAO: WebDAO? = null
    private lateinit var btnTabs: Button
    var listItem: MutableList<Tabs> = ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tabs, container, false)

        db = WebDataBase.getInstance(requireContext())
        webDAO = db?.webDAO()

        listItem = webDAO?.selectAllTabs() as MutableList<Tabs>
        btnTabs = view.findViewById(R.id.btnTabs)

        btnTabs.setOnClickListener {
            if (listItem.isEmpty()){
                return@setOnClickListener
            }
            for (tab in listItem){
                if (tab.visibility){
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fcv, WebFragment(tab.id, tab.url)).commit()
                    break
                }
            }
        }

        view.findViewById<Button>(R.id.btnAdd).setOnClickListener {
            val id = System.currentTimeMillis()
            val fragment = WebFragment(id,"https://www.google.com/")
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fcv,fragment).commit()
            webDAO?.insertTab(Tabs(id, "", fragment.url, true))
        }



        recyclerView = view.findViewById(R.id.recyclerView)
        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT){
                    webDAO?.deleteTab(listItem[position])
                    listItem.removeAt(position)
                    recyclerViewAdapter.setList(listItem)
                    btnTabs.text = listItem.size.toString()
                }
            }
        })
        itemTouchHelper.attachToRecyclerView(recyclerView)
        recyclerView.apply {
            echelonLayoutManager = EchelonLayoutManager((context))
            layoutManager = echelonLayoutManager
            recyclerViewAdapter = RecyclerViewAdapter(listItem, this@TabsFragment)
            recyclerViewAdapter.setList(listItem)
            recyclerView.adapter = recyclerViewAdapter
        }

        webViewModel.getList().observe(requireActivity()) { it ->
            recyclerViewAdapter.setList(it)
        }

        //supportFragmentManager.beginTransaction().replace(R.id.fcv, BlankFragment()).commit()
        ///data/user/0/com.example.test_tabs/files/screenshot/1662951804341.jpg
        ///data/user/0/com.example.test_tabs/files/screenshot/1662951857203.jpg
        ///data/user/0/com.example.test_tabs/files/screenshot/1662951881086.jpg
        return view
    }

    override fun onClick(tab: Tabs) {
        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fcv, WebFragment(tab.id, tab!!.url)).commit()
    }

    override fun onResume() {
        super.onResume()
        btnTabs.text = listItem.size.toString()
    }

}