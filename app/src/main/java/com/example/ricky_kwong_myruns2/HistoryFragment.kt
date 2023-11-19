package com.example.ricky_kwong_myruns2

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.internal.ContextUtils.getActivity
import java.util.Date

class HistoryFragment: Fragment() {//private val context: Activity, private val activityType: Array<String>, private val date: Array<Date>, private val distance: Array<Int>, private val duration: Array<Date>) {

    private lateinit var runListView: ListView

    private lateinit var arrayList: ArrayList<Run>
    private lateinit var arrayAdapter: HistoryListAdapter

    private lateinit var database: RunDatabase
    private lateinit var runDao: RunDao
    private lateinit var repository: RunRepository
    private lateinit var viewModelFactory: RunViewModelFactory
    private lateinit var runViewModel: RunViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.history_fragment, container, false)
        runListView = view.findViewById(R.id.historyList)
        arrayList = ArrayList()
        arrayAdapter = HistoryListAdapter(requireActivity(), arrayList)
        runListView.adapter = arrayAdapter

        database = RunDatabase.getInstance(requireActivity())
        runDao = database.runDao
        repository = RunRepository(runDao)
        viewModelFactory = RunViewModelFactory(repository)
        runViewModel = ViewModelProvider(requireActivity(), viewModelFactory).get(RunViewModel::class.java)

        runViewModel.allRunsLiveData.observe(requireActivity(), Observer { it ->
            arrayAdapter.replace(it)
            arrayAdapter.notifyDataSetChanged()
        })


        return view
    }

}


