package com.raiyansoft.darnaapp.ui.fragments.delivery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentDeliveryHomeBinding
import com.raiyansoft.darnaapp.ui.viewmodel.ProfileViewModel

class DeliveryHomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentDeliveryHomeBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeliveryHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.buttonCoverageAreas.setOnClickListener(this)
        binding.buttonIncomingOrders.setOnClickListener(this)
        binding.buttonEditAccount.setOnClickListener(this)
        binding.buttonDrivers.setOnClickListener(this)
        binding.buttonReports.setOnClickListener(this)
        binding.imageViewSettings.setOnClickListener(this)
        viewModel.dataProfile.observe(viewLifecycleOwner,
            {response->
                if (response.status && response.code == 200){
                    binding.companyName = response.data.company_name
                    binding.orders = response.data.orders.toString()
                    if (response.data.company_name == ""){
                        findNavController().navigate(R.id.action_deliveryHomeFragment_to_deliveryRegisterFragment)
                    }
                }else{
                    Snackbar.make(requireView(), response.message, 5000).show()
                }
            })
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttonCoverageAreas -> {
                Snackbar.make(requireView(), getString(R.string.coverage_areas), 5000).show()
            }
            R.id.buttonIncomingOrders -> {
                Snackbar.make(requireView(), getString(R.string.orders), 5000).show()
            }
            R.id.buttonEditAccount -> {
                Snackbar.make(requireView(), getString(R.string.edit_account), 5000).show()
            }
            R.id.buttonDrivers -> {
                Snackbar.make(requireView(), getString(R.string.drivers), 5000).show()
            }
            R.id.buttonReports -> {
                Snackbar.make(requireView(), getString(R.string.reports), 5000).show()
            }
            R.id.imageViewSettings -> {
                findNavController().navigate(R.id.action_deliveryHomeFragment_to_settingsFragment)
            }
        }
    }

}