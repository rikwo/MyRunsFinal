package com.example.ricky_kwong_myruns2

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import java.sql.Time
import java.time.LocalDateTime

class ManualEntryActivity : AppCompatActivity() {

    var durationInput: String = ""
    var distanceInput: String = ""
    var caloriesInput: String = ""
    var heartRateInput: String = ""
    var commentInput: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manual_entry)
        //below calendardialog implementation from https://stackoverflow.com/questions/45842167/how-to-use-datepickerdialog-in-kotlin
        var calendar = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener {view, year, monthOfYear, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
        }

        val dateTextView: TextView = findViewById(R.id.date)
        dateTextView.setOnClickListener {
            DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val timeSetListener = TimePickerDialog.OnTimeSetListener {view, hour, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
        }

        val timeTextView: TextView = findViewById(R.id.time)
        timeTextView.setOnClickListener {
            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show()
        }
        val durationTextView: TextView = findViewById(R.id.duration)
        durationTextView.setOnClickListener {
            val durationAlert = AlertDialog.Builder(this)
            durationAlert.setTitle("Duration")
            val inflater = layoutInflater
            val durationLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
            val durationEditText: EditText = durationLayout.findViewById(R.id.editText)
            durationEditText.inputType = InputType.TYPE_CLASS_NUMBER
            durationAlert.setView(durationLayout)
            durationAlert.setPositiveButton("Save") { dialogInterface, i ->
                durationInput = durationEditText.text.toString()
            }
            durationAlert.setNegativeButton("Cancel") { dialogInterface, i->
                dialogInterface.dismiss()
            }
            durationAlert.show()
        }
        val distanceTextView: TextView = findViewById(R.id.distance)
        distanceTextView.setOnClickListener {
            val distanceAlert = AlertDialog.Builder(this)
            distanceAlert.setTitle("Distance")
            val inflater = layoutInflater
            val distanceLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
            val distanceEditText: EditText = distanceLayout.findViewById(R.id.editText)
            distanceEditText.inputType = InputType.TYPE_CLASS_NUMBER
            distanceAlert.setView(distanceLayout)
            distanceAlert.setPositiveButton("Save") { dialogInterface, i ->
                distanceInput = distanceEditText.text.toString()
            }
            distanceAlert.setNegativeButton("Cancel") { dialogInterface, i->
                dialogInterface.dismiss()
            }
            distanceAlert.show()
        }
        val caloriesTextView: TextView = findViewById(R.id.calories)
        caloriesTextView.setOnClickListener {
            val caloriesAlert = AlertDialog.Builder(this)
            caloriesAlert.setTitle("Calories")
            val inflater = layoutInflater
            val caloriesLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
            val caloriesEditText: EditText = caloriesLayout.findViewById(R.id.editText)
            caloriesEditText.inputType = InputType.TYPE_CLASS_NUMBER
            caloriesAlert.setView(caloriesLayout)
            caloriesAlert.setPositiveButton("Save") { dialogInterface, i ->
                caloriesInput = caloriesEditText.text.toString()
            }
            caloriesAlert.setNegativeButton("Cancel") { dialogInterface, i->
                dialogInterface.dismiss()
            }
            caloriesAlert.show()
        }
        val heartRateTextView: TextView = findViewById(R.id.heartRate)
        heartRateTextView.setOnClickListener {
            val heartRateAlert = AlertDialog.Builder(this)
            heartRateAlert.setTitle("Heart Rate")
            val inflater = layoutInflater
            val heartRateLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
            val heartRateEditText: EditText = heartRateLayout.findViewById(R.id.editText)
            heartRateEditText.inputType = InputType.TYPE_CLASS_NUMBER
            heartRateAlert.setView(heartRateLayout)
            heartRateAlert.setPositiveButton("Save") { dialogInterface, i ->
                heartRateInput = heartRateEditText.text.toString()
            }
            heartRateAlert.setNegativeButton("Cancel") { dialogInterface, i->
                dialogInterface.dismiss()
            }
            heartRateAlert.show()
        }
        val commentTextView: TextView = findViewById(R.id.manualComment)
        commentTextView.setOnClickListener {
            val commentAlert = AlertDialog.Builder(this)
            commentAlert.setTitle("Comments")
            val inflater = layoutInflater
            val commentLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
            val commentEditText: EditText = commentLayout.findViewById(R.id.editText)
            commentAlert.setView(commentLayout)
            commentAlert.setPositiveButton("Save") { dialogInterface, i ->
                commentInput = commentEditText.text.toString()
            }
            commentAlert.setNegativeButton("Cancel") { dialogInterface, i->
                dialogInterface.dismiss()
            }
            commentAlert.show()
        }

        val manualSave: Button = findViewById(R.id.manualSave)
        manualSave.setOnClickListener {
            finish()
        }

        val manualCancel: Button = findViewById(R.id.manualCancel)
        manualCancel.setOnClickListener {
            finish()
        }
    }
}