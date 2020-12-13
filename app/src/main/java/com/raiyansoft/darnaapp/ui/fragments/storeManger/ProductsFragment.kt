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
import com.raiyansoft.darnaapp.databinding.FragmentProductsBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.model.product.Product
import com.raiyansoft.darnaapp.ui.viewmodel.ProductViewModel
import com.raiyansoft.darnaapp.uitl.OnScrollListener

class ProductsFragment : Fragment(), ProductsAdapter.ProductClick {

    private lateinit var binding: FragmentProductsBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }
    private val loadingDialoge by lazy {
        LoadingDialog()
    }
    private lateinit var products: ArrayList<Product>
    private lateinit var adapter: ProductsAdapter
    private var currentPage = 1
    private var totalAvailablePages = 1
    private var loading = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        doInitialization()
    }

    private fun doInitialization() {
        loadingDialoge.isCancelable = false
        binding.imageViewBack.setOnClickListener {
            findNavController().navigateUp()
        }
        doWork()
    }

    private fun doWork() {
        currentPage = 1
        totalAvailablePages = 1
        products = ArrayList()
        adapter = ProductsAdapter(products, this)
        binding.recyclerProducts.setHasFixedSize(false)
        binding.recyclerProducts.adapter = adapter
        binding.recyclerProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerProducts.addOnScrollListener(onScrollListener)
        getProducts()
    }

    private val onScrollListener = OnScrollListener {
        if (binding.recyclerProducts.canScrollVertically(1)) {
            if (currentPage <= totalAvailablePages) {
                currentPage += 1
                getProducts()
            }
        }
    }

    private fun getProducts() {
        loading = true
        toggleLoading()
        viewModel.getProducts(currentPage)
        viewModel.dataProducts.observe(viewLifecycleOwner,
            { response ->
                loading = false
                toggleLoading()
                if (response != null) {
                    totalAvailablePages = response.data.pages
                    if (response.status && response.code == 200) {
                        if (response.data != null) {
                            var oldCount = products.size
                            adapter.data.addAll(response.data.data)
                            adapter.notifyItemRangeInserted(oldCount, products.size)
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

    override fun productClick(id: Int) {
        val bundle = bundleOf("id" to id, "edit" to true)
        findNavController().navigate(
            R.id.action_productsFragment_to_addProductsFragment,
            bundle
        )
    }

    override fun deleteClick(id: Int) {
        loadingDialoge.show(requireActivity().supportFragmentManager, "Loading")
        viewModel.deleteProduct(id)
        viewModel.dataDelete.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    loadingDialoge.dismiss()
                    Snackbar.make(requireView(), getString(R.string.done_successfully), 3000).show()
                    doWork()
                } else {
                    loadingDialoge.dismiss()
                    Snackbar.make(requireView(), response.message, 3000).show()
                }
            })
    }

}