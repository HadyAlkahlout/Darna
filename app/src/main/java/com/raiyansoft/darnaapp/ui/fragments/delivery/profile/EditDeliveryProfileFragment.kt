package com.raiyansoft.darnaapp.ui.fragments.delivery.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentEditDeliveryProfileBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.ui.viewmodel.DeliveryProfileViewModel
import com.raiyansoft.darnaapp.ui.viewmodel.ProfileViewModel
import com.raiyansoft.darnaapp.uitl.Commons
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.HashMap

class EditDeliveryProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditDeliveryProfileBinding
    private val loading by lazy {
        LoadingDialog()
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[DeliveryProfileViewModel::class.java]
    }
    private val profileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditDeliveryProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        loading.isCancelable = false
        loading.show(requireActivity().supportFragmentManager, "loading")
        fillData()
        binding.imgEditPhoto.setOnClickListener {
            uploadClick()
        }
        binding.btnSave.setOnClickListener {
            saveClick()
        }
    }

    private fun fillData() {
        profileViewModel.dataProfile.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    binding.companyName = response.data.company_name
                    binding.email = response.data.email
                    binding.image = response.data.avatar
                    loading.dismiss()
                } else {
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 5000).show()
                }
            })
    }

    private fun uploadClick() {
        Dexter.withContext(requireContext())
            .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    val i = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(i, 100)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Snackbar.make(requireView(), "Task Cancelled", 3000).show()
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    token!!.continuePermissionRequest()
                }
            }).check()
    }

    private fun getRealPathFromURI(contentUri: Uri): String {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(requireContext(), contentUri, proj, null, null, null)
        val cursor = loader.loadInBackground()
        val columnIndex: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result: String = cursor.getString(columnIndex)
        cursor.close()
        return result
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data!!.data != null) {
            loading.show(requireActivity().supportFragmentManager, "loading")
            val fileUri = data.data
            val imageFile = File(getRealPathFromURI(fileUri!!))
            val reqBody = RequestBody.create("avatar".toMediaTypeOrNull(), imageFile)
            val partImage = MultipartBody.Part.createFormData("avatar", imageFile.name, reqBody)
            viewModel.updateAvatar(partImage)
            viewModel.dataAvatar.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        loading.dismiss()
                        Snackbar.make(requireView(), getString(R.string.done_successfully), 5000)
                            .show()
                        binding.imageViewStore.setImageURI(fileUri)
                    } else {
                        loading.dismiss()
                        Snackbar.make(requireView(), response.message, 5000).show()
                    }
                })
        }
    }

    private fun saveClick() {
        if (binding.edDeliveryName.text!!.isEmpty() || binding.edDeliveryEmail.text!!.isEmpty()) {
            Snackbar.make(requireView(), getString(R.string.empty_fields), 5000).show()
        } else {
            loading.show(requireActivity().supportFragmentManager, "loading")
            val map: MutableMap<String, RequestBody> = HashMap()
            map["company_name"] = toRequestBody(binding.edDeliveryName.text.toString())
            map["email"] = toRequestBody(binding.edDeliveryEmail.text.toString())
            viewModel.updateProfile(map)
            viewModel.dataUpdate.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        loading.dismiss()
                        Snackbar.make(requireView(), getString(R.string.done_successfully), 5000)
                            .show()
                        profileViewModel.getData()
                        findNavController().navigateUp()
                    } else {
                        loading.dismiss()
                        Snackbar.make(requireView(), response.message, 5000).show()
                    }
                })
        }
    }

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

}