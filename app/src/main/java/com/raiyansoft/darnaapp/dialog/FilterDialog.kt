package com.raiyansoft.darnaapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.adapters.OptionAdapter
import com.raiyansoft.darnaapp.databinding.DialogeFilterBinding
import com.raiyansoft.darnaapp.listeners.FilterDialogListener
import com.raiyansoft.darnaapp.model.productDetails.Filter
import com.raiyansoft.darnaapp.model.productDetails.Option

class FilterDialog(val listener: FilterDialogListener) : DialogFragment(), OptionAdapter.OptionCancel {

    private lateinit var binding: DialogeFilterBinding
    private val options = ArrayList<Option>()
    private val adapter by lazy {
        OptionAdapter(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogeFilterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        binding.rcOptions.adapter = adapter
        binding.rcOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.btnAddOption.setOnClickListener {
            if (binding.editOptionArabicTitle.text.isEmpty() || binding.editOptionEnglishTitle.text.isEmpty() || binding.editOptionPrice.text.isEmpty()) {
                Snackbar.make(requireView(), getString(R.string.empty_fields), 3000).show()
            } else {
                var option = Option(
                    options.size,
                    binding.editOptionPrice.text.toString(),
                    binding.editOptionArabicTitle.text.toString(),
                    binding.editOptionArabicTitle.text.toString(),
                    binding.editOptionEnglishTitle.text.toString()
                )
                options.add(option)
                adapter.data.add(option)
                adapter.notifyDataSetChanged()
                binding.isFill = true
            }
        }
        binding.btnAddFilter.setOnClickListener {
            if (binding.editTextFilterArabicTitle.text!!.isEmpty() || binding.editTextFilterEnglishTitle.text!!.isEmpty() ){
                Snackbar.make(requireView(), getString(R.string.empty_fields), 3000).show()
            } else {
                var multiSelect = if (binding.rbPermissible.isChecked){ "multi_select" }else{ "select" }
                val filter = Filter(
                    0,
                    options,
                    binding.editTextFilterArabicTitle.text.toString(),
                    binding.editTextFilterArabicTitle.text.toString(),
                    binding.editTextFilterEnglishTitle.text.toString(),
                    multiSelect
                )
                listener.onClick(filter)
            }
        }
    }

    override fun cancelClick(position: Int) {
        options.removeAt(position)
        adapter.data.removeAt(position)
        adapter.notifyDataSetChanged()
        if (options.isEmpty()){
            binding.isFill = false
        }
    }
}