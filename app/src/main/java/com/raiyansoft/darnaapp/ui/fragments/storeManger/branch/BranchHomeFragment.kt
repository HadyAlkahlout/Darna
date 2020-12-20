package com.raiyansoft.darnaapp.ui.fragments.storeManger.branch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentBranchHomeBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.dialog.StoreStatusDialog
import com.raiyansoft.darnaapp.listeners.CustomDialogListener
import com.raiyansoft.darnaapp.ui.viewmodel.ProfileViewModel
import com.raiyansoft.darnaapp.uitl.Commons

class BranchHomeFragment : Fragment(), View.OnClickListener, CustomDialogListener {

    private lateinit var binding: FragmentBranchHomeBinding
    private val loading by lazy {
        LoadingDialog()
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private val statusDialog by lazy {
        StoreStatusDialog(1, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBranchHomeBinding.inflate(inflater, container, false)
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
            {response->
                if (response.status && response.code == 200){
                    binding.storeName = response.data.market_name
                    binding.branch = response.data.branch_name
                    binding.orders = response.data.orders.toString()
                    binding.image = response.data.avatar
                    Commons.getSharedEditor(requireContext())
                        .putInt(Commons.StoreStatus, response.data.market_status).apply()
                    loading.dismiss()
                }else{
                    Snackbar.make(requireView(), response.message, 5000).show()
                }
            })
        binding.buttonStatus.setOnClickListener(this)
        binding.buttonIncomingOrders.setOnClickListener(this)
        binding.buttonBranch.setOnClickListener(this)
        binding.imageViewSettings.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttonStatus -> {
                statusDialog.show(requireActivity().supportFragmentManager, "status dialog")
            }
            R.id.buttonIncomingOrders -> {
                findNavController().navigate(R.id.action_branchHomeFragment_to_branchOrdersFragment)
            }
            R.id.imageViewSettings -> {
                findNavController().navigate(R.id.action_branchHomeFragment_to_settingsFragment)
            }
        }
    }

    override fun onClick(type: Int) {
        statusDialog.dismiss()
    }

}