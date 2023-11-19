package com.example.ricky_kwong_myruns2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlin.properties.Delegates

class RunEditActivity: AppCompatActivity() {
    lateinit var inputType: TextView
    lateinit var activityType: TextView
    lateinit var dateTime: TextView
    lateinit var duration: TextView
    lateinit var distance: TextView
    lateinit var calories: TextView
    lateinit var heartRate: TextView
    lateinit var comment: TextView

    lateinit var runViewModel : RunViewModel
    var id by Delegates.notNull<Long>()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.run_edit)

        val database = RunDatabase.getInstance(this)
        val databaseDao = database.runDao
        val runRepository = RunRepository(databaseDao)
        val viewModelFactory = RunViewModelFactory(runRepository)
        runViewModel = ViewModelProvider(this, viewModelFactory).get(RunViewModel::class.java)


        inputType = findViewById(R.id.inputType)
        activityType = findViewById(R.id.activityType)
        dateTime = findViewById(R.id.dateTime)
        duration = findViewById(R.id.duration)
        distance = findViewById(R.id.distance)
        calories = findViewById(R.id.calories)
        heartRate = findViewById(R.id.heartRate)
        comment = findViewById(R.id.comment)

        val extras = intent.extras
        if(extras != null){
            id = extras.getLong("runEntry")
            runViewModel.allRunsLiveData.observe(this, Observer{ changedList ->  //NOTE: HAVE to access list through observe
                val entry = changedList.find { item -> item.id == id }

                val sharedPref = getSharedPreferences("settings", Context.MODE_PRIVATE)
                val units = sharedPref.getInt("unit", -1)

                if (entry != null) {
                    val distanceInUnitsPreferred = when (units) {
                        0,-1 -> {
                            entry.distance
                        }
                        else -> {
                            Util.milesToKm(entry.distance)
                        }
                    }
                    val unitString = when (units) {
                        0,-1 -> {
                            "miles"
                        }
                        else -> {
                            "kilometers"
                        }
                    }

                    inputType.text = entry.entryType
                    activityType.text = entry.activityType
                    dateTime.text = entry.dateTime
                    duration.text = "${Util.minutes(entry.duration)} min, ${Util.seconds(entry.duration)} sec"
                    distance.text = "${String.format("%.2f", distanceInUnitsPreferred)} $unitString"
                    calories.text = "${entry.calories} cals"
                    heartRate.text = "${entry.heartRate} bpm"
                    comment.text = entry.comment
                }
            })
        }


    }

    fun onDeleteClicked(view: View) {

        runViewModel.delete(id)
        finish()
    }

    fun onBackClicked(view: View){
        finish()
    }
}
