package com.raiyansoft.darnaapp.ui.fragments.storeManger

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.adapters.ProductsAdapter
import com.raiyansoft.darnaapp.databinding.FragmentProductsBinding
import com.raiyansoft.darnaapp.model.product.Product
import com.raiyansoft.darnaapp.ui.viewmodel.ProductViewModel
import com.raiyansoft.darnaapp.uitl.OnScrollListener

class ProductsFragment : Fragment(), ProductsAdapter.ProductClick {

    private lateinit var binding: FragmentProductsBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }
    private val products = ArrayList<Product>()
    private val adapter by lazy {
        ProductsAdapter(products, this)
    }
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.imageViewBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.recyclerProducts.setHasFixedSize(false)
        binding.recyclerProducts.adapter = adapter
        binding.recyclerProducts.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerProducts.addOnScrollListener(onScrollListener)
        getProducts()
    }

    private val onScrollListener = OnScrollListener {
        if (binding.recyclerProducts.canScrollVertically(1)){
            if (currentPage <= totalAvailablePages){
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
        if (currentPage == 1){
            if (binding.isLoading != null){
                binding.isLoading = loading
            }else{
                binding.isLoading = true
            }
        } else {
            if (binding.isLoadingMore != null){
                binding.isLoadingMore = loading
            }else{
                binding.isLoadingMore = true
            }
        }
    }

    override fun productClick(id: Int) {
        Toast.makeText(requireContext(), id.toString(), Toast.LENGTH_SHORT).show()
    }

}