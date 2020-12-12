package com.raiyansoft.darnaapp.ui.fragments.splash

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageInfo
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentSplashBinding
import com.raiyansoft.darnaapp.dialog.CustomFragmentDialog
import com.raiyansoft.darnaapp.listeners.CustomDialogListener
import com.raiyansoft.darnaapp.ui.activities.*
import com.raiyansoft.darnaapp.ui.viewmodel.SettingsViewModel
import com.raiyansoft.darnaapp.uitl.Commons
import kotlin.system.exitProcess

class SplashFragment : Fragment(), CustomDialogListener {

    private lateinit var binding: FragmentSplashBinding
    private lateinit var customDialog: CustomFragmentDialog
    private val viewModel by lazy {
        ViewModelProvider(this)[SettingsViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        val anim = AnimationUtils.loadAnimation(requireContext(), R.anim.top_move_anim)
        binding.imageView.startAnimation(anim)
        binding.progressBar.startAnimation(anim)
        customDialog = CustomFragmentDialog(
            getString(R.string.attention),
            getString(R.string.something_wrong),
            getString(R.string.ok),
            0,
            this
        )
        customDialog.isCancelable = false
        binding.isLoading = true
        doCheck()
    }

    private fun doCheck() {
        Log.e("hdhd", "doCheck: ${Commons.getSharedPreferences(requireContext())
            .getString(Commons.UserToken, "")}")
        viewModel.dataSettings.observe(viewLifecycleOwner,
            { response ->
                if (response!!.status && response.code == 200) {
                    val pInfo: PackageInfo = requireActivity().packageManager.getPackageInfo(
                        requireActivity().packageName,
                        0
                    )
                    val version = pInfo.versionCode
                    if ((response.data.force_update == "yes" || response.data.force_update == "android") && response.data.android_version == version.toString()) {
                        customDialog.message = getString(R.string.update)
                        customDialog.type = 2
                        customDialog.show(requireActivity().supportFragmentManager, "Update Dialog")
                    } else if (response.data.force_close == "yes" || response.data.force_close == "android") {
                        customDialog.message = getString(R.string.edit)
                        customDialog.type = 1
                        customDialog.show(requireActivity().supportFragmentManager, "Edit Dialog")
                    } else if (response.data.force_update == "no" && response.data.force_close == "no") {
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.SpecialMarket, response.data.special_market).apply()
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.SpecialProduct, response.data.special_product)
                            .apply()
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.MarketUpgrade, response.data.market_upgrade).apply()
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.Facebook, response.data.facebook).apply()
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.Twitter, response.data.twitter).apply()
                        Commons.getSharedEditor(requireContext())
                            .putString(Commons.Instagram, response.data.instagram).apply()
                        val lang = Commons.getSharedPreferences(requireContext())
                            .getString(Commons.LANGUAGE, "")
                        binding.isLoading = false
                        if (lang == "") {
                            findNavController().navigate(R.id.action_splashFragment_to_languageFragment)
                        } else {
                            val id = Commons.getSharedPreferences(requireContext())
                                .getInt(Commons.UserId, 0)
                            if (id == 0) {
                                requireActivity().startActivity(
                                    Intent(
                                        requireContext(),
                                        LoginActivity::class.java
                                    )
                                )
                                requireActivity().finish()
                            } else {
                                when (Commons.getSharedPreferences(requireContext())
                                    .getInt(Commons.UserType, 0)) {
                                    1 -> {
                                        requireActivity().startActivity(
                                            Intent(
                                                requireContext(),
                                                StoreMangerActivity::class.java
                                            )
                                        )
                                    }
                                    2 -> {
                                        requireActivity().startActivity(
                                            Intent(
                                                requireContext(),
                                                BranchActivity::class.java
                                            )
                                        )
                                    }
                                    3 -> {
                                        requireActivity().startActivity(
                                            Intent(
                                                requireContext(),
                                                DeliveryActivity::class.java
                                            )
                                        )
                                    }
                                    4 -> {
                                        requireActivity().startActivity(
                                            Intent(
                                                requireContext(),
                                                DriverActivity::class.java
                                            )
                                        )
                                    }
                                }
                                requireActivity().finish()
                            }
                        }
                    } else {
                        customDialog.show(requireActivity().supportFragmentManager, "Custom Dialog")
                    }
                } else {
                    customDialog.message = response.message
                    customDialog.show(requireActivity().supportFragmentManager, "Custom Dialog")
                }
            }
        )
    }

    override fun onClick(type: Int) {
        when (type) {
            0 -> {
                customDialog.dismiss()
            }
            1 -> {
                customDialog.dismiss()
                exitProcess(0)
            }
            2 -> {
                customDialog.dismiss()
                val appPackageName =
                    requireActivity().packageName

                try {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=$appPackageName")
                        )
                    )
                    requireActivity().finish()
                } catch (anfe: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                    )
                    requireActivity().finish()
                }
            }
        }
    }

}