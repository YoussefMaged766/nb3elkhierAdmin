package com.devYoussef.nb3elkhieradmin.ui.home.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

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
    }


}