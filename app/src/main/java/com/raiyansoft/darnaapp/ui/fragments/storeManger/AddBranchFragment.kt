package com.raiyansoft.darnaapp.ui.fragments.storeManger

import android.os.Bundle
import android.util.Log
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
import com.raiyansoft.darnaapp.databinding.FragmentAddBranchBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.dialog.MapDialog
import com.raiyansoft.darnaapp.listeners.MapDialogListener
import com.raiyansoft.darnaapp.model.city.City
import com.raiyansoft.darnaapp.ui.viewmodel.BranchesViewModel
import com.raiyansoft.darnaapp.ui.viewmodel.CityViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody

class AddBranchFragment : Fragment(), MapDialogListener {

    private lateinit var binding: FragmentAddBranchBinding
    private val loading by lazy {
        LoadingDialog()
    }
    private val cityViewModel by lazy {
        ViewModelProvider(this)[CityViewModel::class.java]
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[BranchesViewModel::class.java]
    }
    private val cities = ArrayList<City>()
    private val regions = ArrayList<City>()
    private var cityId = 0
    private var regionId = 0
    private var lat = 0.0
    private var long = 0.0
    var map = MapDialog(0.0, 0.0, this)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBranchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        loading.isCancelable = false
        loading.show(requireActivity().supportFragmentManager, "loading")
        fillCities()
        setListeners()
        binding.buttonAddLocation.setOnClickListener {
            map = MapDialog(0.0, 0.0, this)
            map.show(requireActivity().supportFragmentManager, "Map")
        }
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.buttonAdd.setOnClickListener {
            addClick()
        }
        loading.dismiss()
    }

    private fun setListeners() {
        binding.spBranchCity.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in cities) {
                        if (binding.spBranchCity.selectedItem.toString() == i.title) {
                            cityId = i.id
                            fillRegions(i.id)
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }

        binding.spBranchRegion.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in regions) {
                        if (binding.spBranchRegion.selectedItem.toString() == i.title) {
                            regionId = i.id
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

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
                    loading.dismiss()
                    Snackbar.make(requireView(), response.message, 5000).show()
                }
            })
        val adapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_spinner_item, names
        )
        binding.spBranchCity.adapter = adapter
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
                            requireContext(), android.R.layout.simple_spinner_dropdown_item, names
                        )
                        binding.spBranchRegion.adapter = adapter
                    } else {
                        Snackbar.make(requireView(), response.message, 5000).show()
                    }
                })
        }
    }

    private fun addClick() {
        if (binding.editTextMobile.text!!.isEmpty() || lat == 0.0 || long == 0.0 || cityId == 0 || regionId == 0) {
            Snackbar.make(requireView(), getString(R.string.empty_fields), 5000).show()
        } else {
            loading.show(requireActivity().supportFragmentManager, "loading")
            val map: MutableMap<String, RequestBody> = HashMap()
            map["mobile_number"] = toRequestBody(binding.editTextMobile.text.toString())
            map["city_id"] = toRequestBody(cityId.toString())
            map["region_id"] = toRequestBody(regionId.toString())
            map["lat"] = toRequestBody(lat.toString())
            map["lng"] = toRequestBody(long.toString())
            viewModel.add(map)
            viewModel.dataAdd.observe(viewLifecycleOwner,
                { response ->
                    if (response.status && response.code == 200) {
                        loading.dismiss()
                        Snackbar.make(requireView(), getString(R.string.add_result), 5000).show()
                        viewModel.getData()
                        findNavController().navigateUp()
                    } else {
                        loading.dismiss()
                        Snackbar.make(requireView(), response.message, 5000).show()
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