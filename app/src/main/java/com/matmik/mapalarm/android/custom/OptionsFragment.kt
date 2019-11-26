package com.matmik.mapalarm.android.custom

import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.matmik.mapalarm.android.R

class OptionsFragment:DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dialog?.window?.setGravity(Gravity.TOP or Gravity.RIGHT)
        dialog?.setCanceledOnTouchOutside(true)
        return inflater.inflate(R.layout.options_fragment, container,false)
    }
}