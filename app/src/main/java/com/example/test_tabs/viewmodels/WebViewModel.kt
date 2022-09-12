package com.example.test_tabs.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.test_tabs.models.Tabs

class WebViewModel: ViewModel() {
    var mutableLiveData : MutableLiveData<List<Tabs>> = MutableLiveData()

    init {
        getList()
    }

    fun getList() : MutableLiveData<List<Tabs>>{
        return mutableLiveData
    }
    private fun setList(list: MutableList<Tabs>){
        mutableLiveData.value = list
    }
}