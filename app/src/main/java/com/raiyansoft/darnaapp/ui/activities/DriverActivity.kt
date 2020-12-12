package com.raiyansoft.darnaapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raiyansoft.darnaapp.databinding.ActivityDriverBinding
import com.raiyansoft.darnaapp.uitl.Commons

class DriverActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDriverBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setLang()
    }

    private fun setLang() {
        val lang = Commons.getSharedPreferences(this).getString(Commons.LANGUAGE, "ar")
        Commons.setLocale(lang!!, this)
    }
}