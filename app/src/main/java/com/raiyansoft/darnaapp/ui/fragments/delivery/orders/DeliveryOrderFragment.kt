package com.raiyansoft.darnaapp.ui.fragments.delivery.orders

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.adapters.DriverAdapter
import com.raiyansoft.darnaapp.adapters.ProductsAdapter
import com.raiyansoft.darnaapp.databinding.FragmentDeliveryOrderBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.model.driver.Driver
import com.raiyansoft.darnaapp.ui.viewmodel.DeliveryOrdersViewModel
import com.raiyansoft.darnaapp.ui.viewmodel.DriverViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class DeliveryOrderFragment : Fragment(), DriverAdapter.DriverClick {

    private lateinit var binding: FragmentDeliveryOrderBinding
    private var orderId: Int = 0
    private var status: Int = 0
    private var assignedOrderId: Int = 0
    private val viewModel by lazy {
        ViewModelProvider(this)[DeliveryOrdersViewModel::class.java]
    }
    private val driverViewModel by lazy {
        ViewModelProvider(this)[DriverViewModel::class.java]
    }
    private val loading by lazy {
        LoadingDialog()
    }
    private lateinit var adapter: DriverAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeliveryOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        orderId = requireArguments().getInt("id")
        status = requireArguments().getInt("status")
        binding.isWaiting = status == 2
        loading.isCancelable = false
        adapter = DriverAdapter(true, this)
        binding.rcDrivers.adapter = adapter
        binding.rcDrivers.layoutManager = LinearLayoutManager(requireContext())
        getOrderDetails(orderId)
        getDrivers()
        binding.btnReceipt.setOnClickListener {
            changeOrderStatus()
        }
        binding.btnTransfer.setOnClickListener {
            assignDriver()
        }
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getOrderDetails(orderId: Int) {
        loading.show(requireActivity().supportFragmentManager, "loading")
        viewModel.orderDetails(orderId)
        viewModel.dataDetails.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    binding.orderDetails = response.data
                    when (response.data.status_id) {
                        1 -> {
                            binding.cardStatus.setCardBackgroundColor(Color.parseColor("#AA3513"))
                        }
                        2 -> {
                            binding.cardStatus.setCardBackgroundColor(Color.parseColor("#008DFF"))
                        }
                        3 -> {
                            binding.cardStatus.setCardBackgroundColor(Color.parseColor("#008DFF"))
                        }
                        4 -> {
                            binding.cardStatus.setCardBackgroundColor(Color.parseColor("#008DFF"))
                        }
                        5 -> {
                            binding.cardStatus.setCardBackgroundColor(Color.parseColor("#29B44E"))
                        }
                        6 -> {
                            binding.cardStatus.setCardBackgroundColor(Color.parseColor("#AA3513"))
                        }
                        7 -> {
                            binding.cardStatus.setCardBackgroundColor(Color.parseColor("#AA3513"))
                        }
                    }
                    loading.dismiss()
                } else {
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 3000).show()
                }
            })
    }

    private fun getDrivers() {
        driverViewModel.dataDrivers.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    if (response.data != null) {
                        if (response.data.isNotEmpty()) {
                            adapter.data.clear()
                            adapter.data.addAll(response.data)
                            adapter.notifyDataSetChanged()
                            binding.isFill = true
                        } else {
                            binding.isFill = false
                        }
                    }
                    loading.dismiss()
                } else {
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 3000).show()
                }
            })
    }

    private fun changeOrderStatus() {
        loading.show(requireActivity().supportFragmentManager, "loading")
        val map: MutableMap<String, RequestBody> = HashMap()
        map["order_id"] = toRequestBody(orderId.toString())
        if (status == 2 && assignedOrderId == 0) {
            loading.dismiss()
            Snackbar.make(requireView(), getString(R.string.no_assign), 3000).show()
        } else {
            map["driver_id"] = toRequestBody(assignedOrderId.toString())
            viewModel.changeOrderStatus(map)
            viewModel.dataStatus.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        loading.dismiss()
                        Snackbar.make(requireView(), getString(R.string.done_successfully), 3000).show()
                        requireActivity().recreate()
                    } else {
                        loading.dismiss()
                        Snackbar.make(requireView(), response.message, 3000).show()
                    }
                })
        }

    }

    private fun assignDriver() {
        if (assignedOrderId == 0) {
            loading.dismiss()
            Snackbar.make(requireView(), getString(R.string.no_assign), 3000).show()
        } else {
            val map: MutableMap<String, RequestBody> = HashMap()
            map["order_id"] = toRequestBody(orderId.toString())
            map["driver_id"] = toRequestBody(assignedOrderId.toString())
            viewModel.assignDriver(map)
            viewModel.dataAssign.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        loading.dismiss()
                        Snackbar.make(requireView(), getString(R.string.done_successfully), 3000).show()
                    } else {
                        loading.dismiss()
                        Snackbar.make(requireView(), response.message, 3000).show()
                    }
                })
        }
    }

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

    override fun deleteDriver(id: Int) {

    }

    override fun assignDriver(id: Int) {
        assignedOrderId = id
    }

}