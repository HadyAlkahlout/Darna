package com.raiyansoft.darnaapp.ui.fragments.driver

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentDriverHomeBinding

class DriverHomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentDriverHomeBinding

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
        binding.buttonIncomingOrders.setOnClickListener(this)
        binding.imageViewSettings.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttonIncomingOrders -> {
                Snackbar.make(requireView(), getString(R.string.orders), 5000).show()
            }
            R.id.imageViewSettings -> {
                findNavController().navigate(R.id.action_driverHomeFragment_to_settingsFragment)
            }
        }
    }

}