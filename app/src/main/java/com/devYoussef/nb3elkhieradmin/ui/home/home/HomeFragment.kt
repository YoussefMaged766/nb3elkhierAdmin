package com.devYoussef.nb3elkhieradmin.ui.home.home

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.dataStore
import com.devYoussef.nb3elkhieradmin.data.local.DataStoreRepository
import com.devYoussef.nb3elkhieradmin.databinding.FragmentHomeBinding
import com.devYoussef.nb3elkhieradmin.utils.NotificationPermissionHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var permissionHandler: NotificationPermissionHandler
    private lateinit var dataStoreRepository: DataStoreRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    requireActivity().finish()
                }
            })
        dataStoreRepository = DataStoreRepository(requireContext().dataStore)
        lifecycleScope.launch {
            dataStoreRepository.savePageNumber("position", 0)
            dataStoreRepository.savePageNumber("page", 1)
        }
        binding.imageViewArrowProduct.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_productsFragment)
        }

        binding.imageViewArrowPromoCode.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_promoCodesFragment)
        }

        binding.imageViewArrowBanner.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_bannersFragment)
        }

        binding.imageViewArrowOrders.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_ordersFragment)
        }

        binding.imageViewArrowStatistics.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_statisticsFragment)
        }

        binding.imageViewArrowRegions.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_regionsFragment)
        }

        binding.imageViewArrowBlocks.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_blocksFragment)
        }
        binding.imageViewArrowUsers.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_usersFragment)
        }

        permissionHandler = NotificationPermissionHandler(this) { isGranted ->
            if (!isGranted) {
                showNotificationPermissionDialog()

            }
        }

        permissionHandler.checkAndRequestPermission()

    }

    private fun showNotificationPermissionDialog() {
        val builder = AlertDialog.Builder(requireContext())

        val message = "هذا التطبيق يحتاج الى الاذن بالتنبيهات ليتمكن من ارسال التنبيهات لك"
        builder.setMessage(message)

        builder.setPositiveButton("نعم") { _: DialogInterface, _: Int ->
            openAppSettings()
        }

        builder.setNegativeButton("لا") { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }

        builder.create().show()
    }

    private fun openAppSettings() {
        Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
            Uri.parse("package:${requireContext().packageName}")
        ).also {
            startActivity(it)
        }
    }

}