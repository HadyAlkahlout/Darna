package com.raiyansoft.darnaapp.ui.fragments.splash

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentLanguageBinding
import com.raiyansoft.darnaapp.ui.activities.LoginActivity
import com.raiyansoft.darnaapp.uitl.Commons

class LanguageFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentLanguageBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLanguageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.cardArabic.setOnClickListener(this)
        binding.cardEnglish.setOnClickListener(this)

    }

    private fun setLang(lang: String){
        Commons.getSharedEditor(requireContext()).putString(Commons.LANGUAGE, lang).apply()
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.cardArabic -> {
                setLang("ar")
            }
            R.id.cardEnglish -> {
                setLang("en")
            }
        }

        requireActivity().startActivity(Intent(requireContext(), LoginActivity::class.java))
        requireActivity().finish()
    }
}