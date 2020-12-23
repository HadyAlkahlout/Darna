package com.raiyansoft.darnaapp.ui.fragments.driver.orders

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
import com.raiyansoft.darnaapp.databinding.FragmentDriverOrdersBinding
import com.raiyansoft.darnaapp.ui.viewmodel.DriverOrdersViewModel
import com.raiyansoft.darnaapp.uitl.OnScrollListener

class DriverOrdersFragment : Fragment(), OrderAdapter.OrderClick  {

    private lateinit var binding: FragmentDriverOrdersBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[DriverOrdersViewModel::class.java]
    }
    private lateinit var adapter: OrderAdapter
    private var currentPage = 1
    private var totalAvailablePages = 1
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriverOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.imageViewBack.setOnClickListener {
            findNavController().navigateUp()
        }
        doWork()
    }

    private fun doWork() {
        currentPage = 1
        totalAvailablePages = 1
        adapter = OrderAdapter(this)
        binding.rcOrders.setHasFixedSize(false)
        binding.rcOrders.adapter = adapter
        binding.rcOrders.layoutManager = LinearLayoutManager(requireContext())
        binding.rcOrders.addOnScrollListener(onScrollListener)
        getOrders()
    }

    private val onScrollListener = OnScrollListener {
        if (binding.rcOrders.canScrollVertically(1)) {
            if (currentPage <= totalAvailablePages) {
                currentPage += 1
                getOrders()
            }
        }
    }

    private fun getOrders() {
        loading = true
        toggleLoading()
        viewModel.getOrders(currentPage)
        viewModel.dataOrders.observe(viewLifecycleOwner,
            { response ->
                loading = false
                toggleLoading()
                if (response != null) {
                    if (response.status && response.code == 200) {
                        totalAvailablePages = response.data.pages
                        if (response.data != null) {
                            var oldCount = adapter.data.size
                            adapter.data.addAll(response.data.data)
                            adapter.notifyItemRangeInserted(oldCount, adapter.data.size)
                        }
                    } else {
                        binding.isLoading = false
                        Snackbar.make(requireView(), response.message, 3000).show()
                    }
                }
            })
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

    override fun orderClick(id: Int, status: Int) {
        val bundle = bundleOf("id" to id, "status" to status)
        findNavController().navigate(
            R.id.action_driverOrdersFragment_to_driverOrderFragment,
            bundle
        )
    }

}