package com.raiyansoft.darnaapp.ui.fragments.storeManger

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
import com.raiyansoft.darnaapp.databinding.FragmentBranchesBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.dialog.MapDialog
import com.raiyansoft.darnaapp.listeners.MapDialogListener
import com.raiyansoft.darnaapp.ui.viewmodel.BranchesViewModel

class BranchesFragment : Fragment(), BranchAdapter.BranchClick, MapDialogListener {

    private lateinit var binding: FragmentBranchesBinding
    private val viewModel by lazy {
        ViewModelProvider(this)[BranchesViewModel::class.java]
    }
    private val loading by lazy {
        LoadingDialog()
    }
    private val adapter by lazy {
        BranchAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBranchesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.recyclerBranches.adapter = adapter
        binding.recyclerBranches.layoutManager = LinearLayoutManager(requireContext())
        loading.isCancelable = false
        loading.show(requireActivity().supportFragmentManager, "Loading")
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonAddBranch.setOnClickListener {
            findNavController().navigate(R.id.action_branchesFragment_to_addBranchFragment)
        }
    }

    private fun getBranches() {
        viewModel.dataBranches.observe(viewLifecycleOwner,
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

    override fun locationClick(lat: Double, long: Double) {
        val map = MapDialog(lat, long, this)
        map.isCancelable = true
        map.show(requireActivity().supportFragmentManager, "Map")
    }

    override fun deleteClick(id: Int) {
        loading.show(requireActivity().supportFragmentManager, "Loading")
        viewModel.delete(id)
        viewModel.dataDelete.observe(viewLifecycleOwner,
            {response->
                if (response.status && response.code == 200) {
                    loading.dismiss()
                    viewModel.getData()
                    getBranches()
                    Snackbar.make(requireView(), getString(R.string.delete_result), 3000).show()
                } else {
                    loading.dismiss()
                    Snackbar.make(requireView(), getString(R.string.something_wrong), 3000).show()
                }
            })
    }

    override fun onClick(lat: Double, long: Double) {

    }

    override fun onResume() {
        super.onResume()
        viewModel.getData()
        getBranches()
    }

}