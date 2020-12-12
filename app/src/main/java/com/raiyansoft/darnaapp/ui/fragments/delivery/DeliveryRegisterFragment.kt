package com.raiyansoft.darnaapp.ui.fragments.delivery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.raiyansoft.darnaapp.R
import com.raiyansoft.darnaapp.databinding.FragmentDeliveryRegisterBinding
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
import okhttp3.RequestBody

class DeliveryRegisterFragment : Fragment(), MapDialogListener {

    private lateinit var binding: FragmentDeliveryRegisterBinding
    private lateinit var loading: LoadingDialog
    private val cityViewModel by lazy {
        ViewModelProvider(this)[CityViewModel::class.java]
    }
    private val profileViewModel by lazy {
        ViewModelProvider(this)[ProfileViewModel::class.java]
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[CompleteRegisterViewModel::class.java]
    }

    private val cities = ArrayList<City>()
    private val regions = ArrayList<City>()
    private var cityId = 0
    private var regionId = 0

    private var lat = 0.0
    private var long = 0.0

    private var map = MapDialog(0.0, 0.0, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeliveryRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        loading = LoadingDialog()
        loading.isCancelable = false
        fillCities()
        setListeners()
        binding.buttonAddLocation.setOnClickListener {
            map = MapDialog(0.0, 0.0, this)
            map.isCancelable = false
            map.show(requireActivity().supportFragmentManager, "Map")
        }
        binding.buttonRegister.setOnClickListener {
            registerClick()
        }
    }

    private fun setListeners() {
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
            requireContext(), android.R.layout.simple_dropdown_item_1line, names
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
                            requireContext(), android.R.layout.simple_dropdown_item_1line, names
                        )
                        binding.spRegion.adapter = adapter
                    } else {
                        Snackbar.make(requireView(), response.message, 5000)
                            .show()
                    }
                })
        }
    }

    private fun registerClick() {
        if (binding.editTextYourName.text!!.isEmpty() || binding.editTextCompanyName.text!!.isEmpty()
            || binding.editTextEmail.text!!.isEmpty() || binding.editTextPhone.text!!.isEmpty()
            || lat == 0.0 || long == 0.0 || cityId == 0 || regionId == 0
        ) {
            Snackbar.make(requireView(), getString(R.string.empty_fields), 5000).show()
        } else {
            loading.show(requireActivity().supportFragmentManager, "loading")
            val map: MutableMap<String, RequestBody> = HashMap()
            map["name"] = toRequestBody(binding.editTextYourName.text.toString())
            map["company_name"] = toRequestBody(binding.editTextCompanyName.text.toString())
            map["email"] = toRequestBody(binding.editTextEmail.text.toString())
            map["phone"] = toRequestBody(binding.editTextPhone.text.toString())
            map["region_id"] = toRequestBody(regionId.toString())
            map["city_id"] = toRequestBody(cityId.toString())
            map["lat"] = toRequestBody(lat.toString())
            map["lng"] = toRequestBody(long.toString())
            viewModel.completeDeliveryRegister(map)
            viewModel.dataDeliveryComplete.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        profileViewModel.getData()
                        Commons.getSharedEditor(requireContext()).putString(Commons.UserName, binding.editTextYourName.text.toString()).apply()
                        findNavController().navigateUp()
                        loading.dismiss()
                    } else {
                        Snackbar.make(requireView(), response.message, 3000)
                            .show()
                    }
                })
        }
    }

    override fun onClick(lat: Double, long: Double) {
        this.lat = lat
        this.long = long
        map.dismiss()
    }

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

}