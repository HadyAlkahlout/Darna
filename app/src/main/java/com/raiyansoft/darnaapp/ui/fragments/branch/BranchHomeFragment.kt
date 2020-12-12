package com.raiyansoft.darnaapp.ui.fragments.branch

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
import com.raiyansoft.darnaapp.ui.viewmodel.ProfileViewModel

class BranchHomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentBranchHomeBinding
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
                    binding.storeName = response.data.branch_name
                    binding.orders = response.data.orders.toString()
                    binding.image = response.data.avatar
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
                Snackbar.make(requireView(), getString(R.string.store_status), 5000).show()
            }
            R.id.buttonIncomingOrders -> {
                Snackbar.make(requireView(), getString(R.string.orders), 5000).show()
            }
            R.id.buttonBranch -> {
                Snackbar.make(requireView(), getString(R.string.branch), 5000).show()
            }
            R.id.imageViewSettings -> {
                findNavController().navigate(R.id.action_branchHomeFragment_to_settingsFragment)
            }
        }
    }

}