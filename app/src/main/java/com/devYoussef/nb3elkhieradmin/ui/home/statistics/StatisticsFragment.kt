package com.devYoussef.nb3elkhieradmin.ui.home.statistics

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentStatisticsBinding
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StatisticsFragment : Fragment() {
    private lateinit var binding: FragmentStatisticsBinding
    private val viewModel: StatisticsViewModel by viewModels()
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
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModel.getStatistics()
//        collectState()
//        setupWebView()
        addMenu()
        setupWebView(viewModel.getLink().value)
        lifecycleScope.launch {
            viewModel.getLink().collect{
                setupWebView(it)
            }
        }

    }

    private fun collectState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.error != null -> {
                        requireContext().showToast(it.error)
                        loadDialogBar.hide()
                    }

                    it.success == "Python script exited" -> {
//                        Glide.with(requireContext()).load(it.statistics?.data?.image?.url)
//                            .into(binding.imageViewStatistics)
                        loadDialogBar.hide()
                    }
                }
            }
        }
    }

    private fun setupWebView(link: String) {
        binding.webViewStatistics.loadData(link, "text/html", "utf-8")
        binding.webViewStatistics.webChromeClient = WebChromeClient()
        val webSettings = binding.webViewStatistics.settings
        webSettings.javaScriptEnabled = true
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.allowFileAccess = true

        // Enable zoom
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = true

    }

    private fun addMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.statstics_menu, menu)

            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.action_30Days -> {
                        viewModel.setLink("<iframe id=\"igraph\" scrolling=\"no\" style=\"border:none;\" seamless=\"seamless\" src=\"https://chart-studio.plotly.com/~youssefmaged/14.embed\" height=\"525\" width=\"100%\"></iframe>\n")
                        return true
                    }

                    R.id.action_revenue -> {
                        viewModel.setLink("<iframe id=\"igraph\" scrolling=\"no\" style=\"border:none;\" seamless=\"seamless\" src=\"https://chart-studio.plotly.com/~youssefmaged/16.embed\" height=\"525\" width=\"100%\"></iframe>\n")
                        return true
                    }

                    R.id.action_Category -> {
                        viewModel.setLink("<iframe id=\"igraph\" scrolling=\"no\" style=\"border:none;\" seamless=\"seamless\" src=\"https://chart-studio.plotly.com/~youssefmaged/19.embed\" height=\"525\" width=\"100%\"></iframe>\n")
                        return true
                    }

                    R.id.action_Selling -> {
                        viewModel.setLink("<iframe id=\"igraph\" scrolling=\"no\" style=\"border:none;\" seamless=\"seamless\" src=\"https://chart-studio.plotly.com/~youssefmaged/21.embed\" height=\"525\" width=\"100%\"></iframe>")
                        return true
                    }


                }
                return false
            }

        } ,viewLifecycleOwner, Lifecycle.State.RESUMED)
    }


}