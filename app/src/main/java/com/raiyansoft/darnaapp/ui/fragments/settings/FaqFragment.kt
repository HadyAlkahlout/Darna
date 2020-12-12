package com.raiyansoft.darnaapp.ui.fragments.settings

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
import com.raiyansoft.darnaapp.adapters.FaqAdapter
import com.raiyansoft.darnaapp.databinding.FragmentFaqBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.ui.viewmodel.SettingsViewModel

class FaqFragment : Fragment() {

    private lateinit var binding: FragmentFaqBinding
    private lateinit var loading: LoadingDialog
    private val adapter by lazy {
        FaqAdapter()
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFaqBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        loading = LoadingDialog()
        loading.isCancelable = false
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.rcFaq.adapter = adapter
        binding.rcFaq.layoutManager = LinearLayoutManager(requireContext())
        loading.show(requireActivity().supportFragmentManager, "loading")
        getFaq()
    }

    private fun getFaq(){
        viewModel.dataFaq.observe(viewLifecycleOwner,
            {response->
                if (response.status && response.code == 200){
                    if (response.data.data.isNotEmpty()){
                        adapter.data.addAll(response.data.data)
                        adapter.notifyDataSetChanged()
                        binding.rcFaq.visibility = View.VISIBLE
                    }
                    loading.dismiss()
                }else{
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 5000).show()
                }
            })
    }

}