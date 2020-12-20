package com.raiyansoft.darnaapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raiyansoft.darnaapp.ui.fragments.storeManger.reports.CanceledReportsFragment
import com.raiyansoft.darnaapp.ui.fragments.storeManger.reports.CompleteReportsFragment
import com.raiyansoft.darnaapp.ui.fragments.storeManger.reports.RefusedReportsFragment

class ReportsPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CompleteReportsFragment()
            1 -> CanceledReportsFragment()
            else -> RefusedReportsFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

}