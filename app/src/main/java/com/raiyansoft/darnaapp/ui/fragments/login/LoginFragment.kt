package com.raiyansoft.darnaapp.ui.fragments.login

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentLoginBinding
import com.raiyansoft.darnaapp.dialog.CustomFragmentDialog
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.listeners.CustomDialogListener
import com.raiyansoft.darnaapp.ui.viewmodel.RegisterViewModel
import com.raiyansoft.darnaapp.uitl.Commons
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class LoginFragment : Fragment(){

    private lateinit var binding: FragmentLoginBinding
    private lateinit var loading: LoadingDialog
    private val viewModel by lazy {
        ViewModelProvider(this)[RegisterViewModel::class.java]
    }
    private var deviceToken = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun loginClick() {
        if (binding.editTextMobile.text.isEmpty() || binding.editTextCode.text.isEmpty()) {
            Snackbar.make(requireView(), getString(R.string.empty_fields), 5000).show()
        } else {
            if (binding.editTextMobile.text.toString().length != 8) {
                Snackbar.make(requireView(), getString(R.string.wrong_phone), 5000).show()
            } else {
                if (binding.editTextCode.text.toString() == "1234" ||
                    binding.editTextCode.text.toString() == "12345" ||
                    binding.editTextCode.text.toString() == "123456" ||
                    binding.editTextCode.text.toString() == "1234567"
                ) {
                    loading.show(requireActivity().supportFragmentManager, "loading")
                    var type = 0
                    when(binding.editTextCode.text.toString()){
                        "1234" ->{
                            type = 1
                        }
                        "12345" ->{
                            type = 2
                        }
                        "123456" ->{
                            type = 3
                        }
                        "1234567" ->{
                            type = 4
                        }
                    }
                    Commons.getSharedEditor(requireContext()).putInt(Commons.UserType, type).apply()
                    if (binding.checkBoxTerms.isChecked) {
                        val map: MutableMap<String, RequestBody> = HashMap()
                        map["mobile_number"] = toRequestBody(binding.editTextMobile.text.toString())
                        map["code"] = toRequestBody(binding.editTextCode.text.toString())
                        map["device_token"] = toRequestBody(deviceToken)
                        viewModel.makeAccount(map)
                        viewModel.dataRegister.observe(viewLifecycleOwner,
                            { response ->
                                if (response.status && response.code == 200) {
                                    loading.dismiss()
                                    Commons.getSharedEditor(requireContext()).putString(
                                        Commons.UserToken,
                                        "Bearer ${response.data.token}"
                                    ).apply()
                                    Commons.getSharedEditor(requireContext())
                                        .putInt(Commons.UserId, response.data.user_id).apply()
                                    findNavController().navigate(R.id.action_loginFragment_to_activationFragment)
                                } else {
                                    loading.dismiss()
                                    Snackbar.make(
                                        requireView(),
                                        response.message,
                                        5000
                                    ).show()
                                }
                            })
                    } else {
                        loading.dismiss()
                        Snackbar.make(requireView(), getString(R.string.terms_approved), 5000).show()
                    }
                } else {
                    Snackbar.make(requireView(), getString(R.string.wrong_code), 5000).show()
                }

            }
        }
    }

    private fun doInitialization() {
        loading = LoadingDialog()
        loading.isCancelable = false
        binding.buttonLogin.setOnClickListener {
            loginClick()
        }

        binding.textViewTerms.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_termsFragment2)
        }
    }

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

}