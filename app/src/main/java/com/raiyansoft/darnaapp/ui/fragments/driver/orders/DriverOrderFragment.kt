package com.raiyansoft.darnaapp.ui.fragments.driver.orders

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
import com.raiyansoft.darnaapp.databinding.FragmentDriverOrderBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.ui.viewmodel.DriverOrdersViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class DriverOrderFragment : Fragment() {

    private lateinit var binding: FragmentDriverOrderBinding
    private var orderId: Int = 0
    private var status: Int = 0
    private val viewModel by lazy {
        ViewModelProvider(this)[DriverOrdersViewModel::class.java]
    }
    private val loading by lazy {
        LoadingDialog()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriverOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        orderId = requireArguments().getInt("id")
        loading.isCancelable = false
        getOrderDetails(orderId)
        binding.btnReceived.setOnClickListener {
            changeOrderStatus(4)
        }
        binding.btnDelivered.setOnClickListener {
            changeOrderStatus(5)
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

    private fun changeOrderStatus(status: Int) {
        loading.show(requireActivity().supportFragmentManager, "loading")
        val map: MutableMap<String, RequestBody> = HashMap()
        map["order_id"] = toRequestBody(orderId.toString())
        map["status"] = toRequestBody(status.toString())
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

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

}