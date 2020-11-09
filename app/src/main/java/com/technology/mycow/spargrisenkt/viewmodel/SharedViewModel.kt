package com.technology.mycow.spargrisenkt.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.technology.mycow.spargrisenkt.DataRepository
import com.technology.mycow.spargrisenkt.Spargrisen

class SharedViewModel: ViewModel() {

    var fragmentTag = MutableLiveData<String>()

    fun setFragmentTag(fragTag: String){
        fragmentTag.postValue(fragTag)
    }

    fun getFragmentTag() : String? {
        return fragmentTag.value
    }




    companion object {
        private const val LOG_MSG = "SHARED_VIEWMODEL"
    }

}