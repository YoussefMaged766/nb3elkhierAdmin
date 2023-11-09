package com.devYoussef.nb3elkhieradmin.ui.home.banner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentBannersBinding
import com.devYoussef.nb3elkhieradmin.model.BannerResponse
import com.devYoussef.nb3elkhieradmin.ui.adapter.BannerAdapter
import com.devYoussef.nb3elkhieradmin.ui.home.product.ProductsViewModel
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import com.devYoussef.nb3elkhieradmin.utils.getFilePathFromUri
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

@AndroidEntryPoint
class BannersFragment : Fragment(), BannerAdapter.OnItemClickListener {
    private lateinit var binding: FragmentBannersBinding
    private val viewModel2: ProductsViewModel by viewModels()
    private val viewModel: BannerViewModel by viewModels()
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>
    private var imgBitmap: Bitmap? = null
    private var imgUri: Uri? = null
    private lateinit var loadFileGallery: ActivityResultLauncher<String>
    private lateinit var loadFileCamera: ActivityResultLauncher<Intent>
    private val adapter: BannerAdapter by lazy {
        BannerAdapter(this)
    }
    private val loadDialogBar: LoadDialogBar by lazy {
        LoadDialogBar(requireContext())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBannersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
        collectGetAllBannersStates()
        collectAddBannersStates()
        collectDeleteBannersStates()
        viewModel.getAllBanners()
        loadFileGallery = registerForActivityResult(ActivityResultContracts.GetContent()) {
            if (it != null) {
                imgUri = it
                viewModel.addBanners(
                    requireContext(),
                    imgUri!!,
                    getFilePathFromUri(requireContext(), imgUri!!, viewModel2).toString()
                )

            }

        }
        loadFileCamera =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    imgBitmap = data?.extras?.get("data") as Bitmap
                    imgUri = bitmapToUri(imgBitmap!!)

                    viewModel.addBanners(
                        requireContext(),
                        imgUri!!,
                        getFilePathFromUri(requireContext(), imgUri!!, viewModel2).toString()
                    )

                }
            }
        binding.btnAddBanner.setOnClickListener {
            handelPermission()
        }
    }

    private fun collectGetAllBannersStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateGetAllBanners.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        if (it.banner?.data?.size == 0) {
                            binding.imgNoOrder.visibility = View.VISIBLE
                        } else {
                            binding.imgNoOrder.visibility = View.GONE
                            binding.bannersRecyclerView.visibility = View.VISIBLE
                            adapter.submitList(it.banner?.data)
                            binding.bannersRecyclerView.adapter = adapter
                        }

                    }
                }
            }
        }
    }

    private fun collectAddBannersStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateAddBanners.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.success.toString())
                        viewModel.getAllBanners()

                    }
                }
            }
        }
    }

    private fun collectDeleteBannersStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateDeleteBanners.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.success.toString())
                        viewModel.getAllBanners()
                    }
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

    private fun setUpDeleteBottomSheet(id: String) {

        val dialog = BottomSheetDialog(requireContext())
        val view = layoutInflater.inflate(R.layout.bottom_sheet_banner, null)
        val deleteLinear = view.findViewById<LinearLayout>(R.id.linearDelete)

        deleteLinear.setOnClickListener {
            viewModel.deleteBanners(id)
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

    override fun onItemClick(data: BannerResponse.Data) {
        setUpDeleteBottomSheet(data._id.toString())
    }


}