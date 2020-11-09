package com.technology.mycow.spargrisenkt.model

import android.os.Bundle
import androidx.fragment.app.Fragment

class SetArgumentsToFragment constructor(fragment: Fragment, args: Bundle, value: String, key: String) : Fragment(){

    var mFragment = Fragment()
    var mNewFragment = Fragment()
    private var mArgs = Bundle()
    private var mValue: String? = null
    private var mKey: String? = null

    init {
        mFragment = fragment
        mArgs = args
        mValue = value
        mKey = key
        mNewFragment = setArguments()
    }

    fun setArguments() : Fragment {

        mArgs.putString(mKey, mValue)
        mFragment.arguments = mArgs
        return mFragment

//        when (mKey) {
//            ConstantCollection.USER_BUTTON_ID -> {
//                mArgs!!.putInt(ConstantCollection.USER_BUTTON_ID, mId!!)
//                mFragment.arguments = mArgs
//                return mFragment
//            }
//            ConstantCollection.FRAGMENT_ID -> {
//                mArgs!!.putInt(ConstantCollection.FRAGMENT_ID, mId!!)
//                mFragment.arguments = mArgs
//                return mFragment
//            }
//            else -> {
//                return mFragment
//            }
//        }
    }

    companion object {
        private const val LOG_MSG = "SET_ARGUMENTS_FRAG"
        //private val sInstance: SelectUserFragment? = null

    }


}