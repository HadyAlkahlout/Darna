package com.raiyansoft.darnaapp.ui.fragments.storeManger.main

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
    private lateinit var adapter: ProductsAdapter
    private var currentPage = 1
    private var totalAvailablePages = 1
    private var loading = false
    private var open = 1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        adapter = ProductsAdapter(false, ArrayList(), this)
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
                    if (response.status && response.code == 200) {
                        totalAvailablePages = response.data.pages
                        if (open > 1 && currentPage == 1){
                            adapter.data.clear()
                            adapter.notifyDataSetChanged()
                        }
                        if (response.data != null) {
                            var oldCount = adapter.data.size
                            adapter.data.addAll(response.data.data)
                            adapter.notifyItemRangeInserted(oldCount, adapter.data.size)
                        }
                        open++
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
        val bundle = bundleOf("id" to "$id", "edit" to "yes")
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