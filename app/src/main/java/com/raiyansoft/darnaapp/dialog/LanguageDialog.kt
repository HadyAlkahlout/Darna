package com.raiyansoft.darnaapp.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.raiyansoft.darnaapp.databinding.DialogeLanguageBinding
import com.raiyansoft.darnaapp.uitl.Commons

class LanguageDialog() : DialogFragment() {

    private lateinit var binding: DialogeLanguageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DialogeLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        var lang = Commons.getSharedPreferences(requireContext()).getString(Commons.LANGUAGE, "")
        when(lang){
            "ar" -> {
                binding.rbArabic.isChecked = true
                binding.rbEnglish.isChecked = false
            }
            "en" -> {
                binding.rbEnglish.isChecked = true
                binding.rbArabic.isChecked = false
            }
        }
        binding.btnPickLang.setOnClickListener {
            if (binding.rbEnglish.isChecked) {
                lang = "en"
                Commons.getSharedEditor(requireContext()).putString(Commons.LANGUAGE, "en").apply()
            } else if (binding.rbArabic.isChecked) {
                lang = "ar"
                Commons.getSharedEditor(requireContext()).putString(Commons.LANGUAGE, "ar").apply()
            }
            requireActivity().recreate()
        }
    }
}