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
import com.raiyansoft.darnaapp.databinding.FragmentCallBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.model.setting.CallData
import com.raiyansoft.darnaapp.ui.viewmodel.SettingsViewModel

class CallFragment : Fragment() {

    private lateinit var binding: FragmentCallBinding
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
        binding = FragmentCallBinding.inflate(inflater, container, false)
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
        binding.buttonSend.setOnClickListener {
            send()
        }
    }

    private fun send() {
        if (binding.edName.text!!.isEmpty() || binding.edMobile.text!!.isEmpty() ||
            binding.edEmail.text!!.isEmpty() || binding.edMessage.text!!.isEmpty()){
            Snackbar.make(requireView(), getString(R.string.empty_fields), 5000).show()
        }else{
            loading.show(requireActivity().supportFragmentManager, "loading")
            val call = CallData(binding.edName.text.toString(), binding.edMobile.text.toString(),
                binding.edEmail.text.toString(), binding.edMessage.text.toString())
            viewModel.sendCall(call)
            viewModel.dataCall.observe(viewLifecycleOwner,
                {response->
                    if (response.status && response.code == 200){
                        loading.dismiss()
                        Snackbar.make(requireView(), getString(R.string.send_result), 5000).show()
                        findNavController().navigateUp()
                    }else{
                        loading.dismiss()
                        Snackbar.make(requireView(), response.message, 5000).show()
                    }
                })
        }
    }

}