package com.raiyansoft.darnaapp.ui.fragments.storeManger

import android.Manifest
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TimePicker
import androidx.lifecycle.ViewModelProvider
import androidx.loader.content.CursorLoader
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.adapters.InternalCategoriesAdapter
import com.raiyansoft.darnaapp.databinding.FragmentEditStoreBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.dialog.MapDialog
import com.raiyansoft.darnaapp.listeners.MapDialogListener
import com.raiyansoft.darnaapp.model.categories.Category
import com.raiyansoft.darnaapp.model.city.City
import com.raiyansoft.darnaapp.ui.viewmodel.CategoryViewModel
import com.raiyansoft.darnaapp.ui.viewmodel.CityViewModel
import com.raiyansoft.darnaapp.ui.viewmodel.CompleteRegisterViewModel
import com.raiyansoft.darnaapp.ui.viewmodel.ProfileViewModel
import com.raiyansoft.darnaapp.uitl.Commons
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class EditStoreFragment : Fragment(), InternalCategoriesAdapter.CategoryCancel,
    TimePickerDialog.OnTimeSetListener,
    MapDialogListener {

    private lateinit var binding: FragmentEditStoreBinding
    private var image: MultipartBody.Part? = null
    private val loading by lazy {
        LoadingDialog()
    }
    private val categoryViewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }
    private val cityViewModel by lazy {
        ViewModelProvider(this)[CityViewModel::class.java]
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[CompleteRegisterViewModel::class.java]
    }
    private val profileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private val category = ArrayList<Category>()
    private val cities = ArrayList<City>()
    private val regions = ArrayList<City>()
    private val internalCategory = ArrayList<String>()
    private val days = ArrayList<String>()
    private val adapter by lazy {
        InternalCategoriesAdapter(this)
    }
    private var sectionId = 0
    private var cityId = 0
    private var regionId = 0
    private var clickType = 0
    private var fromDay = 0
    private var toDay = 0
    private var map = MapDialog(0.0, 0.0, this)
    private var lat = 0.0
    private var long = 0.0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditStoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loading.isCancelable = false
        loading.show(requireActivity().supportFragmentManager, "loading")
        doInitialization()
    }

    private fun doInitialization() {
        binding.imageViewBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.imageViewEditPhoto.setOnClickListener {
            uploadClick()
        }
        fillSections()
        fillCities()
        setListeners()
        binding.recyclerInternalCategories.adapter = adapter
        binding.recyclerInternalCategories.layoutManager = LinearLayoutManager(requireContext())
        binding.imageViewAddCategory.setOnClickListener {
            if (binding.editTextInternalCategory.text!!.isEmpty()) {
                Snackbar.make(requireView(), getString(R.string.empty_fields), 3000).show()
            } else {
                internalCategory.add(binding.editTextInternalCategory.text.toString())
                adapter.data.clear()
                adapter.data.addAll(internalCategory)
                adapter.notifyDataSetChanged()
                binding.isFill = true
                binding.editTextInternalCategory.setText("")
            }
        }
        val timeDialog = com.raiyansoft.darnaapp.dialog.TimePickerDialog(this)
        binding.textFromTime.setOnClickListener {
            clickType = 0
            timeDialog.show(requireActivity().supportFragmentManager, "time picker")
        }
        binding.textToTime.setOnClickListener {
            clickType = 1
            timeDialog.show(requireActivity().supportFragmentManager, "time picker")
        }
        days.add(getString(R.string.saturday))
        days.add(getString(R.string.sunday))
        days.add(getString(R.string.monday))
        days.add(getString(R.string.tuesday))
        days.add(getString(R.string.wednesday))
        days.add(getString(R.string.thursday))
        days.add(getString(R.string.friday))
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, days
        )
        binding.spinnerFrom.adapter = adapter
        binding.spinnerTo.adapter = adapter
        binding.buttonAddLocation.setOnClickListener {
            map = MapDialog(0.0, 0.0, this)
            map.isCancelable = false
            map.show(requireActivity().supportFragmentManager, "Map")
        }
        binding.buttonSave.setOnClickListener {
            saveClick()
        }
        loading.dismiss()
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
            val fileUri = data.data
            binding.imageViewStore.setImageURI(fileUri)
            val imageFile = File(getRealPathFromURI(fileUri!!))
            val reqBody = RequestBody.create("image".toMediaTypeOrNull(), imageFile)
            val partImage = MultipartBody.Part.createFormData("image", imageFile.name, reqBody)
            image = partImage
        }
    }

    private fun setListeners() {
        binding.spinnerSetions.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in category) {
                        if (binding.spinnerSetions.selectedItem.toString() == i.title) {
                            sectionId = i.id
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
        binding.spCity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in cities) {
                        if (binding.spCity.selectedItem.toString() == i.title) {
                            cityId = i.id
                            fillRegions(i.id)
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
        binding.spRegion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in regions) {
                        if (binding.spRegion.selectedItem.toString() == i.title) {
                            regionId = i.id
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
        binding.spinnerFrom.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    fromDay = position + 1
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
        binding.spinnerTo.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    toDay = position + 1
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }
    }

    private fun fillSections() {
        val names = ArrayList<String>()
        names.add(getString(R.string.category))
        category.add(Category(0, getString(R.string.category), 0, ""))
        categoryViewModel.dataCategory.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    for (i in response.data) {
                        category.add(i)
                        names.add(i.title)
                    }
                } else {
                    Snackbar.make(requireView(), response.message, 5000).show()
                }
            })
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, names
        )
        binding.spinnerSetions.adapter = adapter
    }

    private fun fillCities() {
        val names = ArrayList<String>()
        names.add(getString(R.string.city))
        cities.add(City(0, getString(R.string.city)))
        cityViewModel.dataCity.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    for (i in response.data) {
                        cities.add(i)
                        names.add(i.title)
                    }
                } else {
                    Snackbar.make(requireView(), response.message, 5000).show()
                }
            })
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, names
        )
        binding.spCity.adapter = adapter
    }

    private fun fillRegions(cityId: Int) {
        val names = ArrayList<String>()
        if (cityId != 0) {
            cityViewModel.region(cityId)
            cityViewModel.dataRegions.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        regions.clear()
                        regions.add(City(0, getString(R.string.region)))
                        names.clear()
                        names.add(getString(R.string.region))
                        for (i in response.data) {
                            regions.add(i)
                            names.add(i.title)
                        }
                        val adapter = ArrayAdapter(
                            requireContext(), android.R.layout.simple_spinner_item, names
                        )
                        binding.spRegion.adapter = adapter
                    } else {
                        Snackbar.make(requireView(), response.message, 5000)
                            .show()
                    }
                })
        }
    }

    override fun cancelClick(position: Int) {
        internalCategory.removeAt(position)
        adapter.data.addAll(internalCategory)
        adapter.notifyDataSetChanged()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        if (clickType == 0) {
            binding.textFromTime.text = "$hourOfDay:$minute"
        } else {
            binding.textToTime.text = "$hourOfDay:$minute"
        }
    }

    override fun onClick(lat: Double, long: Double) {
        this.lat = lat
        this.long = long
        map.dismiss()
    }

    private fun saveClick() {
        if (image == null || binding.editTextStoreName.text!!.isEmpty() || binding.editTextAbout.text!!.isEmpty()
            || binding.editTextEmail.text!!.isEmpty() || binding.editTextPhone.text!!.isEmpty()
            || sectionId == 0 || cityId == 0 || regionId == 0 || internalCategory.isEmpty()
            || binding.textFromTime.text!!.isEmpty() || binding.textToTime.text!!.isEmpty()
            || fromDay == 0 || toDay == 0 || lat == 0.0 || long == 0.0
        ) {
            Snackbar.make(requireView(), getString(R.string.empty_fields), 3000).show()
        } else {
            loading.show(requireActivity().supportFragmentManager, "loading")
            val map: MutableMap<String, RequestBody> = HashMap()
            map["market_name"] = toRequestBody(binding.editTextStoreName.text.toString())
            map["note"] = toRequestBody(binding.editTextAbout.text.toString())
            map["email"] = toRequestBody(binding.editTextEmail.text.toString())
            map["phone"] = toRequestBody(binding.editTextPhone.text.toString())
            map["cat_id"] = toRequestBody(sectionId.toString())
            map["city_id"] = toRequestBody(cityId.toString())
            map["region_id"] = toRequestBody(regionId.toString())
            map["lat"] = toRequestBody(lat.toString())
            map["lng"] = toRequestBody(long.toString())
            for ((i, category) in internalCategory.withIndex()) {
                map["subCats[$i]"] = toRequestBody(category)
            }
            map["open_from"] = toRequestBody(binding.textFromTime.toString())
            map["open_to"] = toRequestBody(binding.textToTime.toString())
            map["day_from"] = toRequestBody(fromDay.toString())
            map["day_to"] = toRequestBody(toDay.toString())
            viewModel.updateMarketInfo(map, image!!)
            viewModel.dataUpdate.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        profileViewModel.getData()
                        loading.dismiss()
                        if (Commons.getSharedPreferences(requireContext())
                                .getInt(Commons.OpenLevel, 0) == 0
                        ) {
                            Commons.getSharedEditor(requireContext()).putInt(Commons.OpenLevel, 1)
                                .apply()
                            findNavController().navigate(R.id.action_editStoreFragment_to_mangerHomeFragment)
                        } else {
                            findNavController().navigateUp()
                        }
                    } else {
                        loading.dismiss()
                        Snackbar.make(requireView(), response.message, 3000).show()
                    }
                })
        }
    }

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }
}