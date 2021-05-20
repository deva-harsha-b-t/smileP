package com.dtl.smilep

import android.widget.TextView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.dtl.smilep.R
import com.dtl.smilep.LCOfaceDetection
import android.content.Intent
import android.view.View
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.dtl.smilep.showListActivity

class resultDialog : DialogFragment() {
    private var okButton: Button? = null
    private var cancelButton: Button? = null
    private var resultText: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.result_dialog, container, false)
        okButton = view.findViewById(R.id.okButton)
        cancelButton = view.findViewById(R.id.cancelButton)
        resultText = view.findViewById(R.id.resultText)
        val bundle = arguments
        val ResultText = bundle!!.getString(LCOfaceDetection.RESULT_TEXT)
        resultText?.text = ResultText
        okButton?.setOnClickListener(View.OnClickListener {v ->
            val intent = Intent(context,showListActivity::class.java)
            dismiss()
            startActivity(intent)
        })
        cancelButton?.setOnClickListener{v ->
            dismiss() }
        return view
    }
}