package com.raiyansoft.darnaapp.ui.fragments.driver.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentDriverHomeBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.ui.viewmodel.ProfileViewModel

class DriverHomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentDriverHomeBinding
    private val loading by lazy {
        LoadingDialog()
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDriverHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        loading.isCancelable = false
        loading.show(requireActivity().supportFragmentManager, "loading")
        viewModel.dataProfile.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    binding.profile = response.data
                    loading.dismiss()
                } else {
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 5000).show()
                }
            })
        binding.buttonIncomingOrders.setOnClickListener(this)
        binding.imageViewSettings.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttonIncomingOrders -> {
                findNavController().navigate(R.id.action_driverHomeFragment_to_driverOrdersFragment)
            }
            R.id.imageViewSettings -> {
                findNavController().navigate(R.id.action_driverHomeFragment_to_settingsFragment)
            }
        }
    }

}