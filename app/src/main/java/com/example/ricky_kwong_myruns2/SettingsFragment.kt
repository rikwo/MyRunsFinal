package com.example.ricky_kwong_myruns2

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.CheckedTextView
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.security.AccessController.getContext

//list view code adapted from https://www.youtube.com/watch?v=EwwdQt3_fFU
class SettingsFragment(val fContext: Context): Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        var view = inflater.inflate(R.layout.settings_fragment, container, false)

        val sharedPreferences: SharedPreferences = fContext.getSharedPreferences("settings", Context.MODE_PRIVATE)

        val userProfile: LinearLayout = view.findViewById(R.id.userProfile)
        val privacySetting: LinearLayout = view.findViewById(R.id.privacySetting)
        val checkBox: CheckBox = view.findViewById(R.id.privacyCheckBox)
        checkBox.isChecked = sharedPreferences.getBoolean("privacy", false)
        val unitPreference: LinearLayout = view.findViewById(R.id.unitPreference)
        var unitIndex = sharedPreferences.getInt("unit", -1)
        val comments: LinearLayout = view.findViewById(R.id.comments)
        var commentsText = sharedPreferences.getString("comment", null)
        val webPage: LinearLayout = view.findViewById(R.id.webpage)

        userProfile.setOnClickListener {
            val intent = Intent(fContext, ProfileActivity::class.java)
            startActivity(intent)

        }

        privacySetting.setOnClickListener {
            checkBox.isChecked = !checkBox.isChecked

        }

        checkBox.setOnCheckedChangeListener { buttonView, isChecked ->
            val sharedPreferences: SharedPreferences = fContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
            with (sharedPreferences.edit()) {
                putBoolean("privacy", isChecked)
                apply()
            }
        }


        //code inspired by https://www.digitalocean.com/community/tutorials/android-alert-dialog-using-kotlin
        unitPreference.setOnClickListener {
            val sharedPreferences: SharedPreferences = fContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val unitAlert = AlertDialog.Builder(fContext)
            unitAlert.setTitle("Unit Preference")
            val units = arrayOf("Metric (Kilometers)", "Imperial (Miles)")
            unitAlert.setSingleChoiceItems(units, unitIndex) { dialogInterface, i ->
                unitIndex = i
                with (sharedPreferences.edit()) {
                    putInt("unit", unitIndex)
                    apply()
                }
                dialogInterface.dismiss()
            }
            unitAlert.setNeutralButton("Cancel") { dialog, which ->
                dialog.cancel()
            }
            unitAlert.show()
        }

        //code inspired by https://www.digitalocean.com/community/tutorials/android-alert-dialog-using-kotlin
        comments.setOnClickListener {
            val commentAlert = AlertDialog.Builder(fContext)
            val sharedPreferences: SharedPreferences = fContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
            val inflater = layoutInflater
            commentAlert.setTitle("Comments")
            val commentLayout = inflater.inflate(R.layout.alert_dialog_with_edittext, null)
            val editText = commentLayout.findViewById<EditText>(R.id.editText)
            editText.setText(commentsText)
            commentAlert.setView(commentLayout)
            commentAlert.setPositiveButton("OK") {dialogInterface, i ->
                commentsText = editText.text.toString()
                with (sharedPreferences.edit()) {
                    putString("comment", commentsText)
                    apply()
                }
            }
            commentAlert.setNegativeButton("Cancel") {dialogInterface, i->
                dialogInterface.dismiss()
            }
            commentAlert.show()
        }

        webPage.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.sfu.ca/computing.html"))
            startActivity(browserIntent)
        }

        return view
    }

}