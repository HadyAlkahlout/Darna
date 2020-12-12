package com.raiyansoft.darnaapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.raiyansoft.darnaapp.databinding.DialogLoadingBinding

class LoadingDialog() : DialogFragment() {

    private lateinit var binding: DialogLoadingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogLoadingBinding.inflate(inflater, container, false)
        return binding.root
    }
}