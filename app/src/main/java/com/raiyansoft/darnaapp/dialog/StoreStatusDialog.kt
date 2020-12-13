package com.raiyansoft.darnaapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.databinding.DialogeStoreStatusBinding
import com.raiyansoft.darnaapp.listeners.CustomDialogListener
import com.raiyansoft.darnaapp.ui.viewmodel.CompleteRegisterViewModel
import com.raiyansoft.darnaapp.uitl.Commons
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class StoreStatusDialog(val listener: CustomDialogListener) : DialogFragment() {

    private lateinit var binding: DialogeStoreStatusBinding
    private val loading by lazy {
        LoadingDialog()
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[CompleteRegisterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogeStoreStatusBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        when (Commons.getSharedPreferences(requireContext()).getInt(Commons.StoreStatus, 0)) {
            1 -> {
                binding.rbOpen.isChecked = true
                binding.rbBusy.isChecked = false
            }
            2 -> {
                binding.rbBusy.isChecked = true
                binding.rbOpen.isChecked = false
            }
        }
        binding.btnChangeStatus.setOnClickListener {
            var status = 0
            if (binding.rbOpen.isChecked) {
                Commons.getSharedEditor(requireContext()).putInt(Commons.StoreStatus, 1).apply()
                status = 1
            } else if (binding.rbBusy.isChecked) {
                Commons.getSharedEditor(requireContext()).putInt(Commons.StoreStatus, 2).apply()
                status = 2
            }
            val map: MutableMap<String, RequestBody> = HashMap()
            map["status"] = toRequestBody(status.toString())
            viewModel.changeMarketStatus(map)
            viewModel.dataStatus.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        listener.onClick(0)
                    } else {
                        Snackbar.make(requireView(), response.message, 3000)
                            .show()
                    }
                })
        }
    }

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

}