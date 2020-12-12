package com.raiyansoft.darnaapp.ui.fragments.settings

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentSettingsBinding
import com.raiyansoft.darnaapp.dialog.LanguageDialog
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.ui.activities.LoginActivity
import com.raiyansoft.darnaapp.ui.viewmodel.RegisterViewModel
import com.raiyansoft.darnaapp.uitl.Commons

class SettingsFragment : Fragment(), View.OnClickListener{

    private lateinit var binding: FragmentSettingsBinding
    private val loading by lazy {
        LoadingDialog()
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        loading.isCancelable = false
        binding.imgBack.setOnClickListener(this)
        binding.tvLanguage.setOnClickListener(this)
        binding.tvFaq.setOnClickListener(this)
        binding.tvUsePolicy.setOnClickListener(this)
        binding.tvUseTerms.setOnClickListener(this)
        binding.tvAboutUs.setOnClickListener(this)
        binding.tvCallUs.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            R.id.imgBack -> {
                findNavController().navigateUp()
            }
            R.id.tvLanguage -> {
                val language = LanguageDialog()
                language.show(requireActivity().supportFragmentManager, "language dialog")
            }
            R.id.tvFaq -> {
                findNavController().navigate(R.id.action_settingsFragment_to_faqFragment)
            }
            R.id.tvUsePolicy -> {
                findNavController().navigate(R.id.action_settingsFragment_to_policyFragment)
            }
            R.id.tvUseTerms -> {
                findNavController().navigate(R.id.action_settingsFragment_to_termsFragment)
            }
            R.id.tvAboutUs -> {
                findNavController().navigate(R.id.action_settingsFragment_to_aboutFragment)
            }
            R.id.tvCallUs -> {
                findNavController().navigate(R.id.action_settingsFragment_to_callFragment)
            }
            R.id.btnLogout -> {
                loading.show(requireActivity().supportFragmentManager, "Loading")
                viewModel.exitAccount()
                viewModel.dataLogout.observe(viewLifecycleOwner,
                    {response ->
                        if (response.status && response.code == 200) {
                            Commons.getSharedEditor(requireContext()).clear().apply()
                            loading.dismiss()
                            val intent = Intent(requireContext(), LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            requireActivity().startActivity(intent)
                            requireActivity().finish()
                        } else {
                            loading.dismiss()
                            Snackbar.make(requireView(), response.message, 5000).show()
                        }
                    }
                )
            }
        }
    }


}