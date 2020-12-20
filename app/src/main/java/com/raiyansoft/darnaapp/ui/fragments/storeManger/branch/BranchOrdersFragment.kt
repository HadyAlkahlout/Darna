package com.raiyansoft.darnaapp.ui.fragments.storeManger.branch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.adapters.OrderAdapter
import com.raiyansoft.darnaapp.databinding.FragmentBranchOrdersBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.ui.viewmodel.BranchOrdersViewModel

class BranchOrdersFragment : Fragment(), OrderAdapter.OrderClick {

    private lateinit var binding: FragmentBranchOrdersBinding
    private lateinit var todayAdapter: OrderAdapter
    private lateinit var tomorrowAdapter: OrderAdapter
    private lateinit var twoDaysAdapter: OrderAdapter
    private lateinit var threeDaysAdapter: OrderAdapter
    private val viewHolder by lazy {
        ViewModelProvider(this)[BranchOrdersViewModel::class.java]
    }
    private val loading by lazy {
        LoadingDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBranchOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        loading.isCancelable = false
        loading.show(requireActivity().supportFragmentManager, "loading")
        todayAdapter = OrderAdapter(this)
        tomorrowAdapter = OrderAdapter(this)
        twoDaysAdapter = OrderAdapter(this)
        threeDaysAdapter = OrderAdapter(this)
        binding.rcTodayOrders.adapter = todayAdapter
        binding.rcTodayOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rcTomorrowOrders.adapter = tomorrowAdapter
        binding.rcTomorrowOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rcTwoDaysOrders.adapter = twoDaysAdapter
        binding.rcTwoDaysOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rcThreeDaysOrders.adapter = threeDaysAdapter
        binding.rcThreeDaysOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        getOrders()
    }

    private fun getOrders() {
        viewHolder.dataOrders.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    if (response.data.today.isNotEmpty()) {
                        todayAdapter.data.addAll(response.data.today)
                        todayAdapter.notifyDataSetChanged()
                        binding.todayFill = true
                    } else {
                        binding.todayFill = false
                    }
                    if (response.data.tomorrow.isNotEmpty()) {
                        tomorrowAdapter.data.addAll(response.data.tomorrow)
                        tomorrowAdapter.notifyDataSetChanged()
                        binding.tomorrowFill = true
                    } else {
                        binding.tomorrowFill = false
                    }
                    if (response.data.two_days.isNotEmpty()) {
                        twoDaysAdapter.data.addAll(response.data.two_days)
                        twoDaysAdapter.notifyDataSetChanged()
                        binding.twoDayFill = true
                    } else {
                        binding.twoDayFill = false
                    }
                    if (response.data.three_days.isNotEmpty()) {
                        threeDaysAdapter.data.addAll(response.data.three_days)
                        threeDaysAdapter.notifyDataSetChanged()
                        binding.threeDayFill = true
                    } else {
                        binding.threeDayFill = false
                    }
                    loading.dismiss()
                } else {
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 3000).show()
                }
            })
    }

    override fun orderClick(id: Int) {
        val bundle = bundleOf("id" to id)
        findNavController().navigate(
            R.id.action_branchOrdersFragment_to_marketOrderFragment2,
            bundle
        )
    }

}