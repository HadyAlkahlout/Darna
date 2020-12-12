package com.raiyansoft.darnaapp.ui.fragments.login

import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.poovam.pinedittextfield.PinField
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentActivationBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.model.register.Activation
import com.raiyansoft.darnaapp.ui.activities.BranchActivity
import com.raiyansoft.darnaapp.ui.activities.DeliveryActivity
import com.raiyansoft.darnaapp.ui.activities.DriverActivity
import com.raiyansoft.darnaapp.ui.activities.StoreMangerActivity
import com.raiyansoft.darnaapp.ui.viewmodel.RegisterViewModel
import com.raiyansoft.darnaapp.uitl.Commons
import org.jetbrains.annotations.NotNull
import java.util.*

class ActivationFragment : Fragment(){

    private lateinit var binding: FragmentActivationBinding
    private var mCountDownTimer: CountDownTimer? = null
    private lateinit var loading: LoadingDialog

    private var mStartTimeInMillis: Long = 0
    private var mTimeLeftInMillis: Long = 0
    private var mEndTime: Long = 0

    private val viewModel by lazy {
        ViewModelProvider(this)[RegisterViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentActivationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        loading = LoadingDialog()
        loading.isCancelable = false

        binding.imageBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.pinCode.onTextCompleteListener = object : PinField.OnTextCompleteListener {
            override fun onTextComplete(@NotNull enteredText: String): Boolean {
                loading.show(requireActivity().supportFragmentManager, "Loading")
                doCheck(enteredText)
                return true
            }
        }

        binding.buttonActivation.setOnClickListener {
            if (binding.pinCode.text!!.isEmpty()){
                Snackbar.make(requireView(), getString(R.string.empty_fields), 3000).show()
            }else{
                doCheck(binding.pinCode.text.toString())
            }
        }

        binding.textViewResnd.setOnClickListener {
            loading.show(requireActivity().supportFragmentManager, "Loading")
            viewModel.resendActivation()
            viewModel.dataResend.observe(viewLifecycleOwner,
                {response->
                    if (response.status && response.code == 200){
                        loading.dismiss()
                        Snackbar.make(requireView(), getString(R.string.resend_message), 3000).show()
                        setTime()
                        startTimer()
                    }else{
                        Snackbar.make(requireView(), response.message, 3000).show()
                    }
                })
        }

    }

    private fun doCheck(code: String) {
        val activation = Activation(code)
        viewModel.activateAccount(activation)
        viewModel.dataActivate.observe(viewLifecycleOwner,
            {response->
                if (response.status && response.code == 200){
                    loading.dismiss()
                    when(Commons.getSharedPreferences(requireContext()).getInt(Commons.UserType, 0)){
                        1 ->{
                            requireActivity().startActivity(Intent(requireContext(), StoreMangerActivity::class.java))
                        }
                        2 ->{
                            requireActivity().startActivity(Intent(requireContext(), BranchActivity::class.java))
                        }
                        3 ->{
                            requireActivity().startActivity(Intent(requireContext(), DeliveryActivity::class.java))
                        }
                        4 ->{
                            requireActivity().startActivity(Intent(requireContext(), DriverActivity::class.java))
                        }
                    }
                    requireActivity().finish()
                }else{
                    loading.dismiss()
                    Snackbar.make(requireView(), getString(R.string.something_wrong), 3000).show()
                }
            })
    }

    override fun onResume() {
        super.onResume()
        binding.isTiming = true
        setTime()
        startTimer()

    }

    private fun setTime() {
        mStartTimeInMillis = 120000
        resetTimer()
    }

    private fun startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis
        mCountDownTimer = object : CountDownTimer(mTimeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                mTimeLeftInMillis = millisUntilFinished
                updateCountDownText()
            }

            override fun onFinish() {
                binding.isTiming = false
            }
        }.start()
        binding.isTiming = true
    }

    private fun resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis
        updateCountDownText()
    }

    private fun updateCountDownText() {
        val hours = (mTimeLeftInMillis / 1000).toInt() / 3600
        val minutes = (mTimeLeftInMillis / 1000 % 3600).toInt() / 60
        val seconds = (mTimeLeftInMillis / 1000).toInt() % 60
        val timeLeftFormatted: String?
        timeLeftFormatted = if (hours > 0) {
            java.lang.String.format(
                Locale.getDefault(),
                "%d:%02d:%02d", hours, minutes, seconds
            )
        } else {
            java.lang.String.format(
                Locale.getDefault(),
                "%02d:%02d", minutes, seconds
            )
        }
        binding.time = timeLeftFormatted ?: ""
    }

    override fun onStop() {
        mCountDownTimer!!.cancel()
        super.onStop()
    }

}