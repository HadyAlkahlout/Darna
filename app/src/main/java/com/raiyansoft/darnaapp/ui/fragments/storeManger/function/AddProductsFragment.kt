package com.raiyansoft.darnaapp.ui.fragments.storeManger.function

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
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.raiyansoft.darnaapp.adapters.FiltersAdapter
import com.raiyansoft.darnaapp.adapters.ImageAdapter
import com.raiyansoft.darnaapp.adapters.OptionAdapter
import com.raiyansoft.darnaapp.databinding.FragmentAddProductsBinding
import com.raiyansoft.darnaapp.dialog.LoadingDialog
import com.raiyansoft.darnaapp.model.categories.Category
import com.raiyansoft.darnaapp.model.productDetails.Filter
import com.raiyansoft.darnaapp.model.productDetails.Image
import com.raiyansoft.darnaapp.model.productDetails.Option
import com.raiyansoft.darnaapp.ui.viewmodel.CategoryViewModel
import com.raiyansoft.darnaapp.ui.viewmodel.ProductViewModel
import com.raiyansoft.darnaapp.uitl.Commons
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

class AddProductsFragment : Fragment(), ImageAdapter.ImageClick, FiltersAdapter.FilterCancel, OptionAdapter.OptionCancel{

    private lateinit var binding: FragmentAddProductsBinding
    private val images: ArrayList<Uri> = ArrayList()
    private val filters: ArrayList<Filter> = ArrayList()
    private val categories: ArrayList<Category> = ArrayList()
    private val options = ArrayList<Option>()
    private val adapter by lazy {
        OptionAdapter(this)
    }
    private val imageAdapter by lazy {
        ImageAdapter(this)
    }
    private val filterAdapter by lazy {
        FiltersAdapter(this)
    }
    private val loading by lazy {
        LoadingDialog()
    }
    private val categoryViewModel by lazy {
        ViewModelProvider(this)[CategoryViewModel::class.java]
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }
    private var catId = 0
    private var edit = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        doInitialization()
    }

    private fun doInitialization() {
        loading.isCancelable = false
        binding.imgBack.setOnClickListener {
            findNavController().navigateUp()
        }
        binding.imageAdd.setOnClickListener {
            uploadClick()
        }
        binding.recyclerImage.adapter = imageAdapter
        binding.recyclerImage.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rcOptions.adapter = adapter
        binding.rcOptions.layoutManager = LinearLayoutManager(requireContext())
        binding.quantityType = 1
        fillInternalCategories()
        binding.recyclerFilter.adapter = filterAdapter
        binding.recyclerFilter.layoutManager = LinearLayoutManager(requireContext())
        binding.buttonExpired.setOnClickListener {
            expiredQuantity()
        }
        binding.buttonAdd.setOnClickListener {
            addProduct()
        }
        binding.btnAddOption.setOnClickListener {
            if (binding.editOptionArabicTitle.text.isEmpty() || binding.editOptionEnglishTitle.text.isEmpty() || binding.editOptionPrice.text.isEmpty()) {
                Snackbar.make(requireView(), getString(R.string.empty_fields), 3000).show()
            } else {
                var option = Option(
                    options.size,
                    binding.editOptionPrice.text.toString(),
                    binding.editOptionArabicTitle.text.toString(),
                    binding.editOptionArabicTitle.text.toString(),
                    binding.editOptionEnglishTitle.text.toString()
                )
                options.add(option)
                adapter.data.add(option)
                adapter.notifyDataSetChanged()
                binding.isFillOption = true
                binding.editOptionArabicTitle.setText("")
                binding.editOptionEnglishTitle.setText("")
                binding.editOptionPrice.setText("")
            }
        }
        binding.buttonAddFilter.setOnClickListener {
            if (binding.editTextFilterArabicTitle.text!!.isEmpty() || binding.editTextFilterEnglishTitle.text!!.isEmpty() ){
                Snackbar.make(requireView(), getString(R.string.empty_fields), 3000).show()
            } else {
                var multiSelect = if (binding.rbPermissible.isChecked){ "multi_select" }else{ "select" }
                val myOptions = ArrayList<Option>()
                myOptions.clear()
                myOptions.addAll(options)
                val filter = Filter(
                    0,
                    myOptions,
                    binding.editTextFilterArabicTitle.text.toString(),
                    binding.editTextFilterArabicTitle.text.toString(),
                    binding.editTextFilterEnglishTitle.text.toString(),
                    multiSelect
                )
                binding.isFillOption = false
                filters.add(filter)
                val lang = Commons.getSharedPreferences(requireContext()).getString(Commons.LANGUAGE, "")
                if (lang == "ar") {
                    filterAdapter.data.add(filter.title_ar)
                }else{
                    filterAdapter.data.add(filter.title_en)
                }
                filterAdapter.notifyDataSetChanged()
                binding.isFillFilter = true
                binding.editTextFilterArabicTitle.setText("")
                binding.editTextFilterEnglishTitle.setText("")
                options.clear()
                adapter.data.clear()
            }
        }
        if (requireArguments().getString("edit") != "no"){
            edit = true
            binding.isEdit = true
            fillData(requireArguments().getString("id")!!.toInt())
        }
    }

    private fun fillData(id: Int){
        loading.show(requireActivity().supportFragmentManager, "loading")
        viewModel.getProductDetails(id)
        viewModel.dataProductDetails.observe(viewLifecycleOwner,
            {response->
                if (response.status && response.code == 200){
                    if (response.data.images.isNotEmpty()){
                        imageAdapter.data.addAll(response.data.images)
                        imageAdapter.notifyDataSetChanged()
                        binding.isFillImage = true
                    }
                    binding.editTextProductName.setText(response.data.title)
                    binding.editNote.setText(response.data.note)
                    binding.editProcessingTime.setText(response.data.time.toString())
                    binding.editTextPrice.setText(response.data.price)
                    binding.editTextQuantity.setText(response.data.quantity.toString())
                    if (response.data.quantity_type == 1){
                        binding.rbDaily.isChecked = true
                        binding.rbFixed.isChecked = false
                    }else{
                        binding.rbDaily.isChecked = false
                        binding.rbFixed.isChecked = true
                    }
                    if (response.data.filters.isNotEmpty()){
                        for (i in response.data.filters){
                            filterAdapter.data.add(i.title)
                            filterAdapter.notifyDataSetChanged()
                        }
                        filters.addAll(response.data.filters)
                        binding.isFillFilter = true
                    }
                } else {
                    Snackbar.make(requireView(), response.message, 3000).show()
                }
                loading.dismiss()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK && data!!.data != null) {
            val fileUri = data.data!!
            images.add(fileUri)
            var image = Image(0, fileUri.toString())
            imageAdapter.data.add(image)
            imageAdapter.notifyDataSetChanged()
            binding.isFillImage = true
        }
    }

    override fun deleteImage(position: Int, id: Int) {
        loading.show(requireActivity().supportFragmentManager, "loading")
        if (id == 0){
            images.removeAt(position)
            imageAdapter.data.removeAt(position)
            imageAdapter.notifyDataSetChanged()
            loading.dismiss()
        }else{
            viewModel.deleteImage(id)
            viewModel.dataDeleteImage.observe(viewLifecycleOwner,
                {response->
                    if (response.status && response.code == 200) {
                        imageAdapter.data.removeAt(position)
                        imageAdapter.notifyDataSetChanged()
                        Snackbar.make(
                            requireView(),
                            getString(R.string.done_successfully),
                            3000
                        ).show()
                        loading.dismiss()
                    } else {
                        loading.dismiss()
                        Snackbar.make(requireView(), response.message, 3000).show()
                    }
                })
        }
        if (imageAdapter.data.size == 0){
            binding.isFillImage = false
        }
    }

    private fun fillInternalCategories() {
        categoryViewModel.dataInternalCategories.observe(viewLifecycleOwner,
            { response ->
                if (response.status && response.code == 200) {
                    val names = ArrayList<String>()
                    for (i in response.data) {
                        names.add(i.cat)
                        categories.add(i)
                    }
                    val adapter = ArrayAdapter(
                        requireContext(), android.R.layout.simple_spinner_dropdown_item, names
                    )
                    binding.spInternalCategory.adapter = adapter
                } else {
                    Snackbar.make(requireView(), response.message, 3000).show()
                }
            })
        binding.spInternalCategory.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    for (i in categories) {
                        if (binding.spInternalCategory.selectedItem.toString() == i.cat) {
                            catId = i.id
                            break
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
    }

    override fun cancelClick(position: Int) {
        filters.removeAt(position)
        filterAdapter.data.removeAt(position)
        filterAdapter.notifyDataSetChanged()
        if (filterAdapter.data.size == 0){
            binding.isFillFilter = false
        }
    }

    private fun expiredQuantity() {
        val id = requireArguments().getString("id")!!.toInt()
        viewModel.emptyQuantity(id)
        viewModel.dataQuantity.observe(viewLifecycleOwner,
            {response->
                if (response.status && response.code == 200){
                    Snackbar.make(requireView(), getString(R.string.done_successfully), 3000).show()
                    findNavController().navigateUp()
                } else {
                    Snackbar.make(requireView(), response.message, 3000).show()
                }
            })
    }

    private fun addProduct() {
        if (binding.editTextProductName.text!!.isEmpty() || binding.editTextQuantity.text!!.isEmpty()
            || binding.editTextPrice.text!!.isEmpty() || binding.editNote.text!!.isEmpty()){
            Snackbar.make(requireView(), getString(R.string.empty_fields), 3000).show()
        }else{
            loading.show(requireActivity().supportFragmentManager, "loading")
            val quantityType = if (binding.rbDaily.isChecked){ 1 } else { 2 }
            val map: MutableMap<String, RequestBody> = HashMap()
            val mapImage = ArrayList<MultipartBody.Part>()
            if (images.isNotEmpty()) {
                for ((i, image) in images.withIndex()) {
                    val imageFile = File(getRealPathFromURI(image))
                    val reqBody = RequestBody.create("images[$i]".toMediaTypeOrNull(), imageFile)
                    val partImage: MultipartBody.Part =
                        MultipartBody.Part.createFormData("images[$i]", imageFile.name, reqBody)
                    mapImage.add(partImage)
                }
            }
            map["title"] = toRequestBody(binding.editTextProductName.text.toString())
            map["note"] = toRequestBody(binding.editNote.text.toString())
            map["cat_id"] = toRequestBody(catId.toString())
            map["price"] = toRequestBody(binding.editTextPrice.text.toString())
            map["quantity_type"] = toRequestBody(quantityType.toString())
            map["quantity"] = toRequestBody(binding.editTextQuantity.text.toString())
            map["time"] = toRequestBody(binding.editProcessingTime.text.toString())
            if (filters.isNotEmpty()){
                for ((i, filter) in filters.withIndex()){
                    map["filters[$i][type]"] = toRequestBody(filter.type)
                    map["filters[$i][title_ar]"] = toRequestBody(filter.title_ar)
                    map["filters[$i][title_en]"] = toRequestBody(filter.title_en)
                    if (filter.options.isNotEmpty()){
                        for ((y, option) in filter.options.withIndex()){
                            map["filters[$i][options][$y][title_ar]"] = toRequestBody(option.title_ar)
                            map["filters[$i][options][$y][title_en]"] = toRequestBody(option.title_en)
                            map["filters[$i][options][$y][price]"] = toRequestBody(option.price)
                        }
                    }
                }
            }

            if (edit){
                map["id"] = toRequestBody(requireArguments().getString("id")!!)
                viewModel.updateProduct(map, mapImage)
                viewModel.dataUpdate.observe(viewLifecycleOwner,
                    { response ->
                        loading.dismiss()
                        if (response.status && response.code == 200) {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.done_successfully),
                                3000
                            ).show()
                            findNavController().navigateUp()
                        } else {
                            Snackbar.make(requireView(), response.message, 3000).show()
                        }
                    })
            }else {
                viewModel.createProduct(map, mapImage)
                viewModel.dataCreate.observe(viewLifecycleOwner,
                    { response ->
                        loading.dismiss()
                        if (response.status && response.code == 200) {
                            Snackbar.make(
                                requireView(),
                                getString(R.string.done_successfully),
                                3000
                            ).show()
                            findNavController().navigateUp()
                        } else {
                            Snackbar.make(requireView(), response.message, 3000).show()
                        }
                    })
            }
        }
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

    private fun toRequestBody(value: String): RequestBody {
        return RequestBody.create("text/plain".toMediaTypeOrNull(), value)
    }

    override fun optionCancel(position: Int) {
        options.removeAt(position)
        adapter.data.removeAt(position)
        adapter.notifyDataSetChanged()
        if (options.isEmpty()){
            binding.isFillOption = false
        }
    }

}