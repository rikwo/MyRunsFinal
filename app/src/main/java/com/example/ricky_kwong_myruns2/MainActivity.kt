package com.example.ricky_kwong_myruns2

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Spinner
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager2: ViewPager2
    private lateinit var startFragment: StartFragment
    private lateinit var historyFragment: HistoryFragment
    private lateinit var settingsFragment: SettingsFragment
    private lateinit var tabLayout: TabLayout
    private lateinit var fragmentStateAdapter: FragmentStateAdapter
    private lateinit var tabConfigurationStrat: TabConfigurationStrategy
    private lateinit var tabMediator: TabLayoutMediator
    private lateinit var fragmentList: ArrayList<Fragment>
    private val tabNames = arrayOf("Start", "History", "Settings")

    fun checkPermission() {
        if (Build.VERSION.SDK_INT < 23) return
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 0)
    }

    //fragments implementation from XD actiontabs demo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermission()

        viewPager2 = findViewById(R.id.viewpager2)
        tabLayout = findViewById(R.id.tabLayout)

        startFragment = StartFragment()
        historyFragment = HistoryFragment()
        settingsFragment = SettingsFragment(this)

        fragmentList = ArrayList()
        fragmentList.add(startFragment)
        fragmentList.add(historyFragment)
        fragmentList.add(settingsFragment)

        fragmentStateAdapter = FragmentStateAdapter(this, fragmentList)
        viewPager2.adapter = fragmentStateAdapter

        tabConfigurationStrat = TabConfigurationStrategy {
                tab: TabLayout.Tab, position: Int->
            tab.text = tabNames[position] }
        tabMediator = TabLayoutMediator(tabLayout, viewPager2, tabConfigurationStrat)
        tabMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}