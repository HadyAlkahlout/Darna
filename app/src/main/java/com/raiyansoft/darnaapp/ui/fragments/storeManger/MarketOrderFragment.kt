package com.raiyansoft.darnaapp.ui.fragments.storeManger

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
import com.raiyansoft.darnaapp.adapters.ProductsAdapter
import com.raiyansoft.darnaapp.databinding.FragmentMarketOrderBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.model.product.Product
import com.raiyansoft.darnaapp.ui.viewmodel.MarketOrdersViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class MarketOrderFragment : Fragment(), ProductsAdapter.ProductClick  {

    private lateinit var binding: FragmentMarketOrderBinding
    private var orderId: Int = 0
    private val viewModel by lazy {
        ViewModelProvider(this)[MarketOrdersViewModel::class.java]
    }
    private val loading by lazy {
        LoadingDialog()
    }
    private lateinit var products: ArrayList<Product>
    private lateinit var adapter: ProductsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMarketOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        orderId = requireArguments().getInt("id")
        loading.isCancelable = false
        products = ArrayList()
        adapter = ProductsAdapter(true, products, this)
        binding.rcProducts.adapter = adapter
        binding.rcProducts.layoutManager = LinearLayoutManager(requireContext())
        getOrderDetails(orderId)
        binding.btnReceipt.setOnClickListener {
            changeOrderStatus(2)
        }
        binding.btnRejection.setOnClickListener {
            changeOrderStatus(6)
        }
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun getOrderDetails(orderId: Int) {
        loading.show(requireActivity().supportFragmentManager, "loading")
        viewModel.marketOrderDetails(orderId)
        viewModel.dataDetails.observe(viewLifecycleOwner,
            {response->
                if (response.status && response.code == 200){
                    binding.orderNo = "${getString(R.string.order)} ${response.data.id}"
                    binding.order = response.data
                    adapter.data.addAll(response.data.products)
                    adapter.notifyDataSetChanged()
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
            {response->
                if (response.status && response.code == 200){
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

    override fun productClick(id: Int) {

    }

    override fun deleteClick(id: Int) {

    }

}