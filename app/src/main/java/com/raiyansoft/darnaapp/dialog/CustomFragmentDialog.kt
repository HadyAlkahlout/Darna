package com.raiyansoft.darnaapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.DialogCustomBinding
import com.raiyansoft.darnaapp.listeners.CustomDialogListener

class CustomFragmentDialog(
    var title: String,
    var message: String,
    var action: String,
    var type: Int,
    var click: CustomDialogListener
) : DialogFragment() {

    private lateinit var binding: DialogCustomBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogCustomBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.title = title
        binding.message = message
        binding.action = action
        binding.executePendingBindings()
        binding.btnAction.setOnClickListener {
            click.onClick(type)
        }

    }
}