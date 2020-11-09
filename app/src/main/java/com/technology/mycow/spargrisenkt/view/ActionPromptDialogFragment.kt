package com.technology.mycow.spargrisenkt.view

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.technology.mycow.spargrisenkt.R
import com.technology.mycow.spargrisenkt.model.ConstantCollection
import java.lang.ClassCastException

class ActionPromptDialogFragment : DialogFragment() {

    internal lateinit var listener: ActionPromptDialogListener

    interface ActionPromptDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment)
        fun onDialogNegativeClick(dialog: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        activity?.window?.decorView?.systemUiVisibility =
                View.SYSTEM_UI_FLAG_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE

        return activity.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater

            var dialogTitle = arguments?.getInt(ConstantCollection.DIALOG_TITLE_ID)
            var dialogMessage = arguments?.getInt(ConstantCollection.DIALOG_MESSAGE_ID)

            builder.setView(inflater.inflate(R.layout.action_prompt_dialog_fragment, null))
                .setTitle(resources.getString(dialogTitle!!))
                .setMessage(resources.getString(dialogMessage!!))
                .setPositiveButton(
                    R.string.action_prompt_positive,
                    DialogInterface.OnClickListener { dialog, which ->
                        listener.onDialogPositiveClick(this)
                    })
                .setNegativeButton(
                    R.string.action_prompt_negative,
                    DialogInterface.OnClickListener { dialog, which ->
                        listener.onDialogNegativeClick(this)
                    })
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")

    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        listener.onDialogNegativeClick(this)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = targetFragment as ActionPromptDialogListener
        } catch (e: ClassCastException){
            throw ClassCastException((context.toString()) + " must implement ActionPromptDialogListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        Log.d(LOG_MGS, "Detached")
    }

    companion object {
        private const val LOG_MGS = "Action_Prompt_Dialog_Fragment"

    }

}