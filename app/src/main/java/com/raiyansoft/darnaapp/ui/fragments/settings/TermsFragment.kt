package com.raiyansoft.darnaapp.ui.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentTermsBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.ui.viewmodel.SettingsViewModel

class TermsFragment : Fragment() {

    private lateinit var binding: FragmentTermsBinding
    private val loading by lazy {
        LoadingDialog()
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTermsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        loading.isCancelable = false
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        loading.show(requireActivity().supportFragmentManager, "loading")
        viewModel.dataConditions.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    binding.terms = response.data.conditions
                    loading.dismiss()
                } else {
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 5000).show()
                }
            })
    }

}