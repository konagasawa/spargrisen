package com.technology.mycow.spargrisenkt.model

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class FACreateUser: AppCompatTextView {

    constructor(context: Context?) : super(context!!) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) :
            super(context!!, attrs, defStyle){
        init()
    }

    fun init(){
        val typeface = Typeface.createFromAsset(context.assets, "fonts/fa-solid-900.ttf")
        setTypeface(typeface)
    }
}