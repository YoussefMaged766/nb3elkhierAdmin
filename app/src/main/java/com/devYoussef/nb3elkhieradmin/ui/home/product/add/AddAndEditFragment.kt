package com.devYoussef.nb3elkhieradmin.ui.home.product.add

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentAddAndEditBinding
import com.devYoussef.nb3elkhieradmin.model.CategoryResponse
import com.devYoussef.nb3elkhieradmin.ui.home.product.ProductsViewModel
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import com.devYoussef.nb3elkhieradmin.utils.getFilePathFromUri
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class AddAndEditFragment : Fragment() {
    private lateinit var binding: FragmentAddAndEditBinding
    private val viewModel: ProductsViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var imgBitmap: Bitmap? = null
    private var imgUri: Uri? = null
    private lateinit var loadFileGallery: ActivityResultLauncher<String>
    private lateinit var loadFileCamera: ActivityResultLauncher<Intent>
    private val loadDialogBar: LoadDialogBar by lazy {
        LoadDialogBar(requireContext())
    }
    private val args: AddAndEditFragmentArgs by navArgs()
    private lateinit var categoryArrayAdapter: ArrayAdapter<String>
    private var categoryListNames = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddAndEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectCategoryStates()
        if (args.id != "-1") {
            viewModel.getOneProduct(args.id)
            requireActivity().findViewById<TextView>(R.id.txtTitleToolBar).text = "تعديل المنتج"
        }
        collectOneProductStates()
        collectAddProductsState()
        collectUpdateProductsState()
        handelSwitches()
        checkPermission()

        loadFileGallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                imgUri = it
                Glide.with(requireContext()).load(it).into(binding.imgProduct)
            }

        }
        loadFileCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    imgBitmap = data?.extras?.get("data") as Bitmap
                    imgUri = bitmapToUri(imgBitmap!!)
//                    binding.imgProfile.setImageBitmap(imgBitmap)
                    Glide.with(requireContext()).load(imgUri).into(binding.imgProduct)
                }
            }

        binding.CardUploadImg.setOnClickListener {
            handelPermission()
        }
        binding.btnSave.setOnClickListener {
            val name = binding.txtName.text.toString()
            val shortDescription = binding.txtShortDescription.text.toString()
            val isOffered = binding.switchOffer.isChecked
            val quantity = binding.txtQuantity.text.toString()
            val price = binding.txtPrice.text.toString()
            val originalPrice = binding.txtOriginalPrice.text.toString()

            val offerPrice = if (isOffered) binding.txtPriceOffer.text.toString() else "0.0"
            val offerItemNum = if (isOffered) binding.txtOfferNum.text.toString() else "0"

            Log.e("onViewCreated: ", offerPrice.toString())
            if (args.id == "-1") {
                if (validateInputs(
                        name,
                        shortDescription,
                        isOffered,
                        quantity,
                        price,
                        originalPrice,
                        offerPrice,
                        offerItemNum,
                        imgUri
                    ) && isCountrySelected() && isCurrencySelected()
                ) {
                    callApiAddProduct()
                }
            } else {
                if (validateInputs2(
                        name,
                        shortDescription,
                        isOffered,
                        quantity,
                        price,
                        originalPrice,
                        offerPrice,
                        offerItemNum
                    ) && isCountrySelected() && isCurrencySelected(
                    )
                ) {
                    callApiUpdateProduct(args.id)
                }
            }
        }

    }

    private fun handelPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                )
                == PackageManager.PERMISSION_GRANTED

            ) {
                setUpBottomSheet()
            } else {
                val permissions = arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.CAMERA
                )
                permissionLauncher.launch(permissions)
            }
        } else {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                setUpBottomSheet()
            } else {
                val permissions = arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA
                )
                permissionLauncher.launch(permissions)
            }
        }


    }

    private fun bitmapToUri(bitmap: Bitmap): Uri {
        val imageFile = File(requireContext().cacheDir, "temp_image.jpeg")
        val os = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os)
        os.flush()
        os.close()
        return FileProvider.getUriForFile(
            requireContext(),
            "${requireContext().packageName}.provider",
            imageFile
        )
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        loadFileCamera.launch(intent)
    }

    private fun openGalley() {
        loadFileGallery.launch("image/*")
    }

    private fun setUpBottomSheet() {

        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet, null)
        val camera = view.findViewById<LinearLayout>(R.id.linearCamera)
        val gallery = view.findViewById<LinearLayout>(R.id.linearGallery)
        camera.setOnClickListener {
            openCamera()
            dialog.dismiss()
        }
        gallery.setOnClickListener {
            openGalley()
            dialog.dismiss()
        }
        dialog.setCancelable(true)
        dialog.setContentView(view)
        dialog.show()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->

                val isGalleryPermissionGrantedImaged =
                    permissions[Manifest.permission.READ_MEDIA_IMAGES] ?: false

                val isGalleryPermissionGrantedVideos =
                    permissions[Manifest.permission.CAMERA] ?: false

                if (isGalleryPermissionGrantedImaged && isGalleryPermissionGrantedVideos) {

                    setUpBottomSheet()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "الكاميرا والصور مطلوبين",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            permissionLauncher = registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { permissions ->
                val isCameraPermissionGranted = permissions[Manifest.permission.CAMERA] ?: false
                val isGalleryPermissionGranted =
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] ?: false

                if (isGalleryPermissionGranted && isCameraPermissionGranted) {
                    setUpBottomSheet()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "الكاميرا والصور مطلوبين",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

    private fun collectCategoryStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateCategory.collect {
                when {
                    it.isLoading -> {
                    }

                    it.error != null -> {
                        requireContext().showToast(it.error)
                    }

                    it.status == "success" -> {
                        setUpCategorySpinner(it.category?.data)
                    }
                }
            }
        }
    }

    private fun collectOneProductStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateProduct.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.error != null -> {
                        requireContext().showToast(it.error)
                        loadDialogBar.hide()
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        Glide.with(requireContext()).load(it.product?.data?.get(0)?.image?.url)
                            .into(binding.imgProduct)
                        binding.imgUpload.visibility = View.GONE
                        binding.txtUpload.visibility = View.GONE
                        binding.txtName.setText(it.product?.data?.get(0)?.name)
                        binding.txtShortDescription.setText(it.product?.data?.get(0)?.shortDescription)
                        setCategorySpinner(it.product?.data?.get(0)?.category.toString())
                        setCountrySpinner(it.product?.data?.get(0)?.country.toString())
                        binding.switchAvailable.isChecked = it.product?.data?.get(0)?.isAvailable!!
                        binding.switchOffer.isChecked = it.product?.data?.get(0)?.isOffered!!
                        binding.txtQuantity.setText(it.product?.data?.get(0)?.quantity.toString())
                        binding.txtPrice.setText(it.product?.data?.get(0)?.price.toString())
                        setCurrencySpinner(it.product?.data?.get(0)?.priceCurrency.toString())
                        binding.txtOriginalPrice.setText(it.product?.data?.get(0)?.originalPrice.toString())
                        if (it.product?.data?.get(0)?.isOffered == true) {
                            binding.txtPriceOffer.setText(it.product?.data?.get(0)?.offer?.get(0)?.priceOffered.toString())
                            binding.txtOfferNum.setText(it.product?.data?.get(0)?.offer?.get(0)?.itemNum.toString())
                        } else {
                            binding.linearQuantityAndPriceOffer.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    private fun callApiAddProduct() {
        val name = binding.txtName.text.toString()
        val shortDescription = binding.txtShortDescription.text.toString()
        val category = binding.spinnerCategory.selectedItem.toString()
        val country = binding.spinnerCountry.selectedItem.toString()
        val isAvailable = binding.switchAvailable.isChecked
        val isOffered = binding.switchOffer.isChecked
//        val quantity = binding.txtQuantity.text.toString()
        val price = binding.txtPrice.text.toString()
        val priceCurrency = binding.spinnerCurrency.selectedItem.toString()
        val originalPrice = binding.txtOriginalPrice.text.toString()

        val offerPrice = if (isOffered) binding.txtPriceOffer.text.toString() else "0.0"
        val offerItemNum = if (isOffered) binding.txtOfferNum.text.toString() else "0"

        viewModel.addProduct(
            ctx = requireContext(),
            fileUri = imgUri!!,
            fileRealPath = getFilePathFromUri(requireContext(), imgUri!!, viewModel).toString(),
            name = name,
            shortDescription = shortDescription,
            category = category,
            country = country,
            isAvailable = isAvailable,
            isOffered = isOffered,
            quantity = "0.0",
            price = price,
            priceCurrency = priceCurrency,
            originalPrice = originalPrice,
            offerPrice = offerPrice,
            offerItemNum = offerItemNum
        )
    }

    private fun callApiUpdateProduct(id: String) {
        val name = binding.txtName.text.toString()
        val shortDescription = binding.txtShortDescription.text.toString()
        val category = binding.spinnerCategory.selectedItem.toString()
        val country = binding.spinnerCountry.selectedItem.toString()
        val isAvailable = binding.switchAvailable.isChecked
        val isOffered = binding.switchOffer.isChecked
        val quantity = binding.txtQuantity.text.toString()
        val price = binding.txtPrice.text.toString()
        val priceCurrency = binding.spinnerCurrency.selectedItem.toString()
        val originalPrice = binding.txtOriginalPrice.text.toString()

        val offerPrice = if (isOffered) binding.txtPriceOffer.text.toString() else "0.0"
        val offerItemNum = if (isOffered) binding.txtOfferNum.text.toString() else "0"
        if (imgUri != null) {
            viewModel.updateProduct(
                ctx = requireContext(),
                fileUri = imgUri!!,
                fileRealPath = getFilePathFromUri(requireContext(), imgUri!!, viewModel).toString(),
                name = name,
                shortDescription = shortDescription,
                category = category,
                country = country,
                isAvailable = isAvailable,
                isOffered = isOffered,
                quantity = quantity,
                price = price,
                priceCurrency = priceCurrency,
                originalPrice = originalPrice,
                offerPrice = offerPrice,
                offerItemNum = offerItemNum,
                id = id
            )
        } else {
            viewModel.updateProduct(
                ctx = requireContext(),
                name = name,
                shortDescription = shortDescription,
                category = category,
                country = country,
                isAvailable = isAvailable,
                isOffered = isOffered,
                quantity = quantity,
                price = price,
                priceCurrency = priceCurrency,
                originalPrice = originalPrice,
                offerPrice = offerPrice,
                offerItemNum = offerItemNum,
                id = id
            )
        }


    }

    private fun collectAddProductsState() {
        lifecycleScope.launch {
            viewModel.stateAddProduct.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.error != null -> {
                        requireContext().showToast(it.error)
                        loadDialogBar.hide()
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        findNavController().popBackStack()
                        requireContext().showToast(it.success.toString())
                    }
                }
            }
        }
    }

    private fun collectUpdateProductsState() {
        lifecycleScope.launch {
            viewModel.stateUpdateProduct.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.error != null -> {
                        requireContext().showToast(it.error)
                        loadDialogBar.hide()
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        findNavController().popBackStack()
                        requireContext().showToast(it.success.toString())
                    }
                }
                Log.e("collectUpdateProductsState: ", it.toString())
            }
        }
    }

    private fun validateInputs(
        name: String,
        shortDescription: String,
        isOffered: Boolean,
        quantity: String,
        price: String,
        originalPrice: String,
        offerPrice: String,
        offerItemNum: String,
        image: Uri? = null
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.txtNameContainer.error = "مطلوب"
            isValid = false
        }
        if (shortDescription.isEmpty()) {
            binding.txtShortDescriptionContainer.error = "مطلوب"
            isValid = false
        }
        if (quantity.isEmpty()) {
            binding.txtQuantityContainer.error = "مطلوب"
            isValid = false
        }
        if (price.isEmpty()) {
            binding.txtPriceContainer.error = "مطلوب"
            isValid = false
        }
        if (originalPrice.isEmpty()) {
            binding.txtOriginalPriceContainer.error = "مطلوب"
            isValid = false
        }
        if (isOffered) {
            if (offerPrice.isEmpty()) {
                binding.txtPriceOfferContainer.error = "مطلوب"
                isValid = false
            }
            if (offerItemNum.isEmpty()) {
                binding.txtOfferNumContainer.error = "مطلوب"
                isValid = false
            }
        }
        if (image == null) {
            requireContext().showToast("مطلوب اختيار صورة")
            isValid = false
        }

        binding.txtNameContainer.editText?.addTextChangedListener {
            binding.txtNameContainer.error = null
        }
        binding.txtShortDescriptionContainer.editText?.addTextChangedListener {
            binding.txtShortDescriptionContainer.error = null
        }
        binding.txtQuantityContainer.editText?.addTextChangedListener {
            binding.txtQuantityContainer.error = null
        }
        binding.txtPriceContainer.editText?.addTextChangedListener {
            binding.txtPriceContainer.error = null
        }
        binding.txtOriginalPriceContainer.editText?.addTextChangedListener {
            binding.txtOriginalPriceContainer.error = null
        }
        if (isOffered) {
            binding.txtPriceOfferContainer.editText?.addTextChangedListener {
                binding.txtPriceOfferContainer.error = null
            }
            binding.txtOfferNumContainer.editText?.addTextChangedListener {
                binding.txtOfferNumContainer.error = null
            }
        }

        return isValid
    }

    private fun validateInputs2(
        name: String,
        shortDescription: String,
        isOffered: Boolean,
        quantity: String,
        price: String,
        originalPrice: String,
        offerPrice: String,
        offerItemNum: String
    ): Boolean {
        var isValid = true

        if (name.isEmpty()) {
            binding.txtNameContainer.error = "مطلوب"
            isValid = false
        }
        if (shortDescription.isEmpty()) {
            binding.txtShortDescriptionContainer.error = "مطلوب"
            isValid = false
        }
        if (quantity.isEmpty()) {
            binding.txtQuantityContainer.error = "مطلوب"
            isValid = false
        }
        if (price.isEmpty()) {
            binding.txtPriceContainer.error = "مطلوب"
            isValid = false
        }
        if (originalPrice.isEmpty()) {
            binding.txtOriginalPriceContainer.error = "مطلوب"
            isValid = false
        }
        if (isOffered) {
            if (offerPrice.isEmpty()) {
                binding.txtPriceOfferContainer.error = "مطلوب"
                isValid = false
            }
            if (offerItemNum.isEmpty()) {
                binding.txtOfferNumContainer.error = "مطلوب"
                isValid = false
            }
        }
        binding.txtNameContainer.editText?.addTextChangedListener {
            binding.txtNameContainer.error = null
        }
        binding.txtShortDescriptionContainer.editText?.addTextChangedListener {
            binding.txtShortDescriptionContainer.error = null
        }
        binding.txtQuantityContainer.editText?.addTextChangedListener {
            binding.txtQuantityContainer.error = null
        }
        binding.txtPriceContainer.editText?.addTextChangedListener {
            binding.txtPriceContainer.error = null
        }
        binding.txtOriginalPriceContainer.editText?.addTextChangedListener {
            binding.txtOriginalPriceContainer.error = null
        }
        if (isOffered) {
            binding.txtPriceOfferContainer.editText?.addTextChangedListener {
                binding.txtPriceOfferContainer.error = null
            }
            binding.txtOfferNumContainer.editText?.addTextChangedListener {
                binding.txtOfferNumContainer.error = null
            }
        }

        return isValid
    }


    private fun isCountrySelected(): Boolean {
        val text = binding.spinnerCountry.selectedItem.toString()
        return if (text == "الدوله") {
            binding.txtSpinnerCountryError.visibility = View.VISIBLE
            false
        } else {
            binding.txtSpinnerCountryError.visibility = View.GONE
            true
        }
    }

    private fun isCurrencySelected(): Boolean {
        val text = binding.spinnerCurrency.selectedItem.toString()
        return if (text == "العمله") {
            binding.txtSpinnerCurrencyError.visibility = View.VISIBLE
            false
        } else {
            binding.txtSpinnerCurrencyError.visibility = View.GONE
            true
        }
    }

    private fun setCategorySpinner(value: String) {
        if (categoryListNames.isEmpty()) {
            return
        }
        val position = categoryListNames.indexOf(value)
        if (position >= 0) {
            binding.spinnerCategory.setSelection(position)
        }
    }

    private fun setCountrySpinner(value: String) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.countries,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountry.adapter = adapter
        val spinnerArray = resources.getStringArray(R.array.countries)
        val position = spinnerArray.indexOf(value)
        if (position >= 0) {
            binding.spinnerCountry.setSelection(position)
        }
    }

    private fun setCurrencySpinner(value: String) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.currency,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCurrency.adapter = adapter
        val spinnerArray = resources.getStringArray(R.array.currency)
        val position = spinnerArray.indexOf(value)
        if (position >= 0) {
            binding.spinnerCurrency.setSelection(position)
        }
    }

    private fun handelSwitches() {
        binding.switchOffer.setOnCheckedChangeListener() { buttonView, isChecked ->
            if (isChecked) {
                binding.linearQuantityAndPriceOffer.visibility = View.VISIBLE
            } else {
                binding.linearQuantityAndPriceOffer.visibility = View.GONE

            }
        }
        binding.switchAvailable.setOnCheckedChangeListener() { buttonView, isChecked ->
            if (isChecked) {
                binding.CardUploadImg.isEnabled = true
                binding.txtNameContainer.isEnabled = true
                binding.txtShortDescriptionContainer.isEnabled = true
                binding.spinnerCategory.isEnabled = true
                binding.spinnerCountry.isEnabled = true
                binding.spinnerCurrency.isEnabled = true
                binding.txtQuantityContainer.isEnabled = true
                binding.txtPriceContainer.isEnabled = true
                binding.txtOriginalPriceContainer.isEnabled = true
            } else {
                binding.CardUploadImg.isEnabled = false
                binding.txtNameContainer.isEnabled = false
                binding.txtShortDescriptionContainer.isEnabled = false
                binding.spinnerCategory.isEnabled = false
                binding.spinnerCountry.isEnabled = false
                binding.spinnerCurrency.isEnabled = false
                binding.txtQuantityContainer.isEnabled = false
                binding.txtPriceContainer.isEnabled = false
                binding.txtOriginalPriceContainer.isEnabled = false
            }
        }
    }

    private fun setUpCategorySpinner(list: List<CategoryResponse.Data>?) {
        categoryListNames = list!!.map { it.name } as MutableList<String>
        categoryArrayAdapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryListNames)
        categoryArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryArrayAdapter
    }
}