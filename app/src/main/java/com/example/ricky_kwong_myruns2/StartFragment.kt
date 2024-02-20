package com.example.ricky_kwong_myruns2

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class StartFragment: Fragment() {

    lateinit var inputResult: String
    lateinit var activityResult: String
    var inputResultPosition: Int = -1
    var activityResultPosition: Int = -1



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {


        var view: View = inflater.inflate(R.layout.start_fragment, container, false)
        var inputSpinner: Spinner = view.findViewById(R.id.inputTypeSpinner)
        ArrayAdapter.createFromResource(requireContext(), R.array.InputType, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            inputSpinner.adapter = adapter
        }

        inputResult = inputSpinner.selectedItem.toString()
        inputResultPosition = inputSpinner.getSelectedItemPosition()

        var activitySpinner: Spinner = view.findViewById(R.id.activityTypeSpinner)
        ArrayAdapter.createFromResource(requireContext(), R.array.ActivityType, android.R.layout.simple_spinner_item).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            activitySpinner.adapter = adapter
        }

        activityResult = activitySpinner.selectedItem.toString()
        activityResultPosition = activitySpinner.getSelectedItemPosition()

        var startBtn: Button = view.findViewById(R.id.startButton)

        startBtn.setOnClickListener {
            inputResultPosition = inputSpinner.getSelectedItemPosition()

            if (inputResultPosition == 0) {
                val intent = Intent(requireContext(), ManualEntryActivity::class.java)
                intent.putExtra("activityType", activityResult)
                intent.putExtra("entryType", inputResult)
                startActivity(intent)
            }
            else if (inputResultPosition == 1) {
                val serviceIntent = Intent(requireContext(), LocationTrackingService::class.java)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requireContext().startForegroundService(serviceIntent)
                }
                else {
                    requireContext().startService(serviceIntent)
                }

                val intent = Intent(requireContext(), MapActivity::class.java)
                startActivity(intent)
            }
            else {
                val serviceIntent = Intent(requireContext(), LocationTrackingService::class.java)


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    requireContext().startForegroundService(serviceIntent)
                }
                else {
                    requireContext().startService(serviceIntent)
                }

                val intent = Intent(requireContext(), MapActivity::class.java)
                startActivity(intent)
            }
        }

        return view
    }

}
