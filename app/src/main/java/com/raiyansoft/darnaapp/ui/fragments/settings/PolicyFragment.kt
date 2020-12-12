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
import com.raiyansoft.darnaapp.databinding.FragmentPolicyBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.ui.viewmodel.SettingsViewModel

class PolicyFragment : Fragment() {

    private lateinit var binding: FragmentPolicyBinding
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
        binding = FragmentPolicyBinding.inflate(inflater, container, false)
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
        viewModel.dataPrivacy.observe(viewLifecycleOwner,
            {response->
                if (response.status && response.code == 200){
                    binding.policy = response.data.privacy
                    loading.dismiss()
                }else{
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 5000).show()
                }
            })
    }

}