package com.example.ricky_kwong_myruns2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MapActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val saveBtn: Button = findViewById(R.id.mapSave)
        saveBtn.setOnClickListener {
            finish()
        }

        val cancelBtn: Button = findViewById(R.id.mapCancel)
        cancelBtn.setOnClickListener {
            finish()
        }
    }
}