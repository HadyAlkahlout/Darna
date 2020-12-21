package com.raiyansoft.darnaapp.ui.fragments.delivery.reports

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.adapters.ReportsDeliveryPagerAdapter
import com.raiyansoft.darnaapp.databinding.FragmentMainDeliveryReportsBinding

class MainDeliveryReportsFragment : Fragment() {

    private lateinit var binding: FragmentMainDeliveryReportsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainDeliveryReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.viewPager.adapter = ReportsDeliveryPagerAdapter(requireActivity())
        val tabMediator = TabLayoutMediator(
            binding.tabLayout,
            binding.viewPager
        ) { tab, position ->
            when(position){
                0 -> {
                    tab.text = getString(R.string.complete_orders)
                }
                1 -> {
                    tab.text = getString(R.string.canceled_orders)
                }
                2 -> {
                    tab.text = getString(R.string.refused_orders)
                }
            }
        }
        tabMediator.attach()
    }

}