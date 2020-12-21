package com.raiyansoft.darnaapp.ui.fragments.delivery.location

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
import com.raiyansoft.darnaapp.adapters.BranchAdapter
import com.raiyansoft.darnaapp.adapters.LocationAdapter
import com.raiyansoft.darnaapp.databinding.FragmentLocationsBinding
import com.raiyansoft.darnaapp.databinding.FragmentLoginBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.dialog.MapDialog
import com.raiyansoft.darnaapp.listeners.MapDialogListener
import com.raiyansoft.darnaapp.ui.viewmodel.BranchesViewModel
import com.raiyansoft.darnaapp.ui.viewmodel.LocationViewModel

class LocationsFragment : Fragment(), LocationAdapter.LocationClick, MapDialogListener {

    private lateinit var binding: FragmentLocationsBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[LocationViewModel::class.java]
    }
    private val loading by lazy {
        LoadingDialog()
    }
    private val adapter by lazy {
        LocationAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLocationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.rcLocations.adapter = adapter
        binding.rcLocations.layoutManager = LinearLayoutManager(requireContext())
        loading.isCancelable = false
        loading.show(requireActivity().supportFragmentManager, "Loading")
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.btnAddLocation.setOnClickListener {
            findNavController().navigate(R.id.action_locationsFragment_to_addLocationFragment)
        }
    }

    private fun getLocations() {
        viewModel.dataLocations.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    if (response.data != null) {
                        if (response.data.isNotEmpty()) {
                            adapter.data.clear()
                            adapter.data.addAll(response.data)
                            adapter.notifyDataSetChanged()
                            binding.isFill = true
                        }
                    }
                    loading.dismiss()
                } else {
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 3000).show()
                }
            })
    }

    override fun location(lat: Double, long: Double) {
        val map = MapDialog(lat, long, this)
        map.isCancelable = true
        map.show(requireActivity().supportFragmentManager, "Map")
    }

    override fun delete(id: Int) {
        loading.show(requireActivity().supportFragmentManager, "Loading")
        viewModel.deleteLocation(id)
        viewModel.dataDelete.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    viewModel.getData()
                    getLocations()
                    loading.dismiss()
                    Snackbar.make(requireView(), getString(R.string.done_successfully), 3000).show()
                } else {
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 3000).show()
                }
            })
    }

    override fun onClick(lat: Double, long: Double) {

    }

    override fun onResume() {
        super.onResume()
        getLocations()
    }

}