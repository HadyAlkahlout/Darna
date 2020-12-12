package com.raiyansoft.darnaapp.ui.fragments.storeManger

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentMangerHomeBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.dialog.StoreStatusDialog
import com.raiyansoft.darnaapp.listeners.CustomDialogListener
import com.raiyansoft.darnaapp.ui.viewmodel.ProfileViewModel
import com.raiyansoft.darnaapp.uitl.Commons

class MangerHomeFragment : Fragment(), View.OnClickListener, CustomDialogListener {

    private lateinit var binding: FragmentMangerHomeBinding
    private val loading by lazy {
        LoadingDialog()
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private val statusDialog by lazy {
        StoreStatusDialog(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMangerHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        doInitialization()
    }

    private fun doInitialization() {
        loading.isCancelable = false
        loading.show(requireActivity().supportFragmentManager, "loading")
        viewModel.dataProfile.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    binding.storeName = response.data.market_name
                    binding.branch = response.data.branch_name
                    binding.orders = response.data.orders.toString()
                    binding.image = response.data.avatar
                    Commons.getSharedEditor(requireContext())
                        .putInt(Commons.StoreStatus, response.data.market_status).apply()
                    loading.dismiss()
                    if (response.data.name == "" || response.data.name == null) {
                        findNavController().navigate(R.id.action_mangerHomeFragment_to_registerFragment)
                    }
                } else {
                    Snackbar.make(requireView(), response.message, 5000).show()
                }
            })
        binding.buttonStatus.setOnClickListener(this)
        binding.buttonAddProduct.setOnClickListener(this)
        binding.buttonOurProducts.setOnClickListener(this)
        binding.buttonEditStore.setOnClickListener(this)
        binding.buttonIncomingOrders.setOnClickListener(this)
        binding.buttonReports.setOnClickListener(this)
        binding.buttonBranch.setOnClickListener(this)
        binding.buttonBranches.setOnClickListener(this)
        binding.imageViewSettings.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.buttonStatus -> {
                statusDialog.show(requireActivity().supportFragmentManager, "status dialog")
            }
            R.id.buttonAddProduct -> {
                Snackbar.make(requireView(), getString(R.string.add_product), 5000).show()
            }
            R.id.buttonOurProducts -> {
                findNavController().navigate(R.id.action_mangerHomeFragment_to_productsFragment)
            }
            R.id.buttonEditStore -> {
                findNavController().navigate(R.id.action_mangerHomeFragment_to_editStoreFragment)
            }
            R.id.buttonIncomingOrders -> {
                Snackbar.make(requireView(), getString(R.string.orders), 5000).show()
            }
            R.id.buttonReports -> {
                Snackbar.make(requireView(), getString(R.string.reports), 5000).show()
            }
            R.id.buttonBranch -> {
                Snackbar.make(requireView(), getString(R.string.branch), 5000).show()
            }
            R.id.buttonBranches -> {
                findNavController().navigate(R.id.action_mangerHomeFragment_to_branchesFragment)
            }
            R.id.imageViewSettings -> {
                findNavController().navigate(R.id.action_mangerHomeFragment_to_settingsFragment)
            }
        }
    }

    override fun onClick(type: Int) {
        statusDialog.dismiss()
    }

}