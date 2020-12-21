package com.raiyansoft.darnaapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.raiyansoft.darnaapp.ui.fragments.delivery.reports.CancelDeliveryReportFragment
import com.raiyansoft.darnaapp.ui.fragments.delivery.reports.CompleteDeliveryReportFragment
import com.raiyansoft.darnaapp.ui.fragments.delivery.reports.RefuseDeliveryReportFragment

class ReportsDeliveryPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> CompleteDeliveryReportFragment()
            1 -> CancelDeliveryReportFragment()
            else -> RefuseDeliveryReportFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }

}