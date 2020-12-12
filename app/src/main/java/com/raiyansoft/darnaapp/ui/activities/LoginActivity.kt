package com.raiyansoft.darnaapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.uitl.Commons

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setLang()
    }

    private fun setLang() {
        val lang = Commons.getSharedPreferences(this).getString(Commons.LANGUAGE, "ar")
        Commons.setLocale(lang!!, this)
    }
}