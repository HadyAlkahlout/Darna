package com.raiyansoft.darnaapp.ui.fragments.storeManger.reports

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.adapters.ReportOrderAdapter
import com.raiyansoft.darnaapp.databinding.FragmentCompleteReportsBinding
import com.raiyansoft.darnaapp.ui.viewmodel.MarketReportsViewModel
import com.raiyansoft.darnaapp.uitl.OnScrollListener

class CompleteReportsFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var binding: FragmentCompleteReportsBinding
    private val dateDialog by lazy {
        com.raiyansoft.darnaapp.dialog.DatePickerDialog(this)
    }
    private var type = 0
    private val viewModel by lazy {
        ViewModelProvider(this)[MarketReportsViewModel::class.java]
    }
    private lateinit var adapter: ReportOrderAdapter
    private var currentPage = 1
    private var totalAvailablePages = 1
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCompleteReportsBinding.inflate(inflater, container, false)
        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.tvFromDate.setOnClickListener {
            type = 1
            dateDialog.show(requireActivity().supportFragmentManager, "Date Dialog")
        }
        binding.tvToDate.setOnClickListener {
            type = 2
            dateDialog.show(requireActivity().supportFragmentManager, "Date Dialog")
        }
        currentPage = 1
        totalAvailablePages = 1
        adapter = ReportOrderAdapter()
        binding.rcCompleteReports.setHasFixedSize(false)
        binding.rcCompleteReports.adapter = adapter
        binding.rcCompleteReports.layoutManager = LinearLayoutManager(requireContext())
        binding.rcCompleteReports.addOnScrollListener(onScrollListener)
        binding.imgSearch.setOnClickListener {
            if (binding.tvFromDate.text.isEmpty() || binding.tvToDate.text.isEmpty()){
                Snackbar.make(requireView(), getString(R.string.empty_fields), 3000).show()
            }else{
                getOrders()
            }
        }
    }

    private val onScrollListener = OnScrollListener {
        if (binding.rcCompleteReports.canScrollVertically(1)) {
            if (currentPage <= totalAvailablePages) {
                currentPage += 1
                getOrders()
            }
        }
    }

    private fun getOrders(){
        loading = true
        toggleLoading()
        viewModel.completeOrder(binding.tvFromDate.text.toString(), binding.tvToDate.text.toString(), currentPage)
        viewModel.dataComplete.observe(viewLifecycleOwner,
            {response->
                loading = false
                toggleLoading()
                if (response.status && response.code == 200){
                    totalAvailablePages = response.data.pages
                    if (response.data != null) {
                        var oldCount = adapter.data.size
                        adapter.data.addAll(response.data.data)
                        adapter.notifyItemRangeInserted(oldCount, adapter.data.size)
                        if (response.data.data.isEmpty()){
                            Snackbar.make(requireView(), getString(R.string.empty_reports), 3000).show()
                        }
                    }
                } else {
                    binding.isLoading = false
                    Snackbar.make(requireView(), response.message, 3000).show()
                }
            })
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        var editMonth = month + 1
        if (type == 1){
            binding.tvFromDate.text = "$year-$editMonth-$dayOfMonth"
        }else if (type == 2){
            binding.tvToDate.text = "$year-$editMonth-$dayOfMonth"
        }
        dateDialog.dismiss()
    }

    private fun toggleLoading() {
        if (currentPage == 1) {
            if (binding.isLoading != null) {
                binding.isLoading = loading
            } else {
                binding.isLoading = true
            }
        } else {
            if (binding.isLoadingMore != null) {
                binding.isLoadingMore = loading
            } else {
                binding.isLoadingMore = true
            }
        }
    }

}