package com.raiyansoft.darnaapp.ui.fragments.delivery.driver

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
import com.raiyansoft.darnaapp.adapters.LocationAdapter
import com.raiyansoft.darnaapp.databinding.FragmentDriversBinding
import com.raiyansoft.darnaapp.databinding.FragmentLocationsBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.ui.viewmodel.DriverViewModel
import com.raiyansoft.darnaapp.ui.viewmodel.LocationViewModel

class DriversFragment : Fragment(), DriverAdapter.DriverClick {

    private lateinit var binding: FragmentDriversBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[DriverViewModel::class.java]
    }
    private val loading by lazy {
        LoadingDialog()
    }
    private val adapter by lazy {
        DriverAdapter(false, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriversBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.rcDrivers.adapter = adapter
        binding.rcDrivers.layoutManager = LinearLayoutManager(requireContext())
        loading.isCancelable = false
        loading.show(requireActivity().supportFragmentManager, "Loading")
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAddDriver.setOnClickListener {
            findNavController().navigate(R.id.action_driversFragment_to_addDriverFragment)
        }
    }

    private fun getDrivers() {
        viewModel.dataDrivers.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    if (response.data != null) {
                        if (response.data.isNotEmpty()) {
                            adapter.data.clear()
                            adapter.data.addAll(response.data)
                            adapter.notifyDataSetChanged()
                            binding.isFill = true
                        }else{
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

    override fun deleteDriver(id: Int) {
        loading.show(requireActivity().supportFragmentManager, "Loading")
        viewModel.deleteDriver(id)
        viewModel.dataDelete.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    viewModel.getData()
                    getDrivers()
                    loading.dismiss()
                    Snackbar.make(requireView(), getString(R.string.done_successfully), 3000).show()
                } else {
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 3000).show()
                }
            })
    }

    override fun assignDriver(id: Int) {

    }

    override fun onResume() {
        super.onResume()
        getDrivers()
    }

}