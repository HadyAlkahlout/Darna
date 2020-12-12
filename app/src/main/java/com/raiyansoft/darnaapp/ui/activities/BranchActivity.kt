package com.raiyansoft.darnaapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raiyansoft.darnaapp.databinding.ActivityBranchBinding
import com.raiyansoft.darnaapp.uitl.Commons

class BranchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBranchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBranchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLang()
    }

    private fun setLang() {
        val lang = Commons.getSharedPreferences(this).getString(Commons.LANGUAGE, "ar")
        Commons.setLocale(lang!!, this)
    }
}