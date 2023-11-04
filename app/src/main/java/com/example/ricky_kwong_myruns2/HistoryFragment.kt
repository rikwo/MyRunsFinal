package com.example.ricky_kwong_myruns2

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.google.android.material.internal.ContextUtils.getActivity
import java.util.Date

class HistoryFragment: Fragment() {//private val context: Activity, private val activityType: Array<String>, private val date: Array<Date>, private val distance: Array<Int>, private val duration: Array<Date>) {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

}


