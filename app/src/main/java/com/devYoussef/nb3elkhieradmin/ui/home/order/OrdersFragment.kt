package com.devYoussef.nb3elkhieradmin.ui.home.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.databinding.FragmentOrdersBinding
import com.devYoussef.nb3elkhieradmin.ui.adapter.ViewPagerAdapter
import com.devYoussef.nb3elkhieradmin.ui.home.order.cancel.CancelOrderFragment
import com.devYoussef.nb3elkhieradmin.ui.home.order.current.CurrentOrderFragment
import com.devYoussef.nb3elkhieradmin.ui.home.order.past.PastOrderFragment
import com.devYoussef.nb3elkhieradmin.utils.FadeOutTransformation
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    private val fadeOutTransformation = FadeOutTransformation()
    private var currentTabIndex: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViewPager()

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    findNavController().navigate(R.id.homeFragment)
                }
            })

    }

    private fun setUpViewPager() {

        val adapter = ViewPagerAdapter(
            supportFragmentManager = requireActivity().supportFragmentManager,
            lifecycle = lifecycle
        )
        adapter.addFragment(CurrentOrderFragment(), "الطلبات الحاليه")
        adapter.addFragment(PastOrderFragment(), "الطلبات السابقه")
        adapter.addFragment(CancelOrderFragment(), "الطلبات الملغيه")
        binding.viewpager.isSaveEnabled = false
        binding.viewpager.setPageTransformer(fadeOutTransformation)
        binding.viewpager.adapter = adapter
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
            binding.viewpager.setCurrentItem(tab.position, true)
        }.attach()

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    currentTabIndex = it.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                // Not needed for this example
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
                // Not needed for this example
            }
        })
    }



}