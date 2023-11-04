package com.example.ricky_kwong_myruns2

// class taken from XD actiontabs implementation
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import java.util.ArrayList

class FragmentStateAdapter(activity: FragmentActivity, var list: ArrayList<Fragment>)
    : FragmentStateAdapter(activity){

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }
}