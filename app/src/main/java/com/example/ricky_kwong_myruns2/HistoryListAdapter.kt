package com.example.ricky_kwong_myruns2

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class HistoryListAdapter(private val context: Context, private var runList: List<Run>): BaseAdapter() {
    override fun getItem(position: Int): Any {
        return runList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return runList.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val sharedPref = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        val units = sharedPref.getInt("unit", -1)
        val unitString = when (units) {
            0, -1 -> {
                "miles"
            }
            else -> {
                "kilometers"
            }
        }
        val unitConversion = when (units) {
            0, -1 -> {
                runList.get(position).distance
            }
            else -> {
                Util.milesToKm(runList.get(position).distance.toFloat()).toString()
            }
        }

        val minutes = Util.minutes(runList.get(position).duration)
        val seconds = Util.seconds(runList.get(position).duration)

        val view: View = View.inflate(context, R.layout.layout_adapter, null)

        val listItemTitle = view.findViewById<TextView>(R.id.title) as TextView
        val textViewDetails = view.findViewById(R.id.details) as TextView

        listItemTitle.text = "${runList.get(position).entryType}: ${runList.get(position).activityType}"
        textViewDetails.text = "${runList.get(position).dateTime}\n${String.format("%.2f", unitConversion)} ${unitString!!}, ${minutes} min ${seconds} sec"

        view.setOnClickListener(object: View.OnClickListener {
            override fun onClick(view: View?) {
                val intent = Intent(context, RunEditActivity::class.java)
                intent.putExtra("runEntry", runList.get(position).id)
                val same = intent.extras?.getLong("id")
                context.startActivity(intent)
            }
        })

        return view
    }

    fun replace(newRunList: List<Run>) {
        runList = newRunList
    }

}