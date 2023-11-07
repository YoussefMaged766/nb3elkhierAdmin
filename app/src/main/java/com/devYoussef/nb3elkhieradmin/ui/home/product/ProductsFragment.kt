package com.devYoussef.nb3elkhieradmin.ui.home.product

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.devYoussef.nab3elkheir.ui.adapter.paging.LoadStateAdapter
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentProductsBinding
import com.devYoussef.nb3elkhieradmin.databinding.ProductItemBinding
import com.devYoussef.nb3elkhieradmin.model.AuthResponse
import com.devYoussef.nb3elkhieradmin.model.ProductResponse
import com.devYoussef.nb3elkhieradmin.ui.adapter.paging.ProductsPagingAdapter
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.search.SearchView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException

@AndroidEntryPoint
class ProductsFragment : Fragment(), ProductsPagingAdapter.OnButton1ClickListener {
    private lateinit var binding: FragmentProductsBinding
    private val viewModel by viewModels<ProductsViewModel>()
    private val productAdapter by lazy { ProductsPagingAdapter(this) }
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
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApi()
        collectProductsState()
        collectDeleteState()
        binding.searchView.addTransitionListener { _, _, end ->
            if (end == SearchView.TransitionState.SHOWN) {
                requireActivity().onBackPressedDispatcher.addCallback(
                    viewLifecycleOwner,
                    object : OnBackPressedCallback(true) {
                        override fun handleOnBackPressed() {
                            binding.searchView.hide()
                        }

                    })
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                    findNavController().navigate(R.id.homeFragment)
                }

            })
        binding.fabAddProduct.setOnClickListener {
            val action =
                ProductsFragmentDirections.actionProductsFragmentToAddAndEditFragment("-1")
            findNavController().navigate(action)
        }



    }

    private fun callApi() {
        binding.searchView.editText.setOnEditorActionListener() { v, actionId, event ->
            Log.e("callApi1: ", v.text.toString())
            val action =
                ProductsFragmentDirections.actionProductsFragmentToSearchResultFragment(v.text.toString())
            findNavController().navigate(action)
            binding.searchView.clearFocusAndHideKeyboard()
            true
        }
        viewModel.getPagingProducts()
        binding.swipeRefreshLayout.setOnRefreshListener {
            productAdapter.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }


    }


    private fun collectDeleteState() {
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

                    it.status == "success" -> {
                        requireContext().showToast("تم الحذف بنجاح")
                        Log.e("collectDeleteState: ", it.success.toString())
                        loadDialogBar.hide()
                        productAdapter.refresh()
                    }
                }
                Log.e("collectDeleteState1: ", it.status.toString())
            }
        }
    }

    private fun collectProductsState() {
        lifecycleScope.launch {
            viewModel.dataProduct.collect {
                productAdapter.submitData(it)
                binding.recyclerViewProducts.adapter =
                    productAdapter.withLoadStateHeaderAndFooter(
                        header = LoadStateAdapter { productAdapter.retry() },
                        footer = LoadStateAdapter { productAdapter.retry() }
                    )
            }
        }


        productAdapter.addLoadStateListener { loadState ->
            binding.swipeRefreshLayout.isRefreshing = loadState.refresh is LoadState.Loading
            Log.e("collectProductsState: ", productAdapter.itemCount.toString())

            if (productAdapter.itemCount == 0) {
                binding.imgEmptyProducts.visibility = View.VISIBLE

            } else {
                binding.imgEmptyProducts.visibility = View.GONE
                binding.recyclerViewProducts.visibility = View.VISIBLE
            }

            handelError(loadState)
        }
    }

    private fun handelError(loadStates: CombinedLoadStates) {
        val errorState = loadStates.source.refresh as? LoadState.Error
            ?: loadStates.source.prepend as? LoadState.Error
        errorState?.let {
            when (val error = it.error) {
                is HttpException -> {
                    val gson = Gson()
                    val type = object : TypeToken<AuthResponse>() {}.type
                    val errorResponse: AuthResponse? =
                        gson.fromJson(error.response()?.errorBody()!!.charStream(), type)
                    requireContext().showToast(errorResponse?.message.toString())
                }

                else -> {}
            }
        }
    }

    override fun onButtonEditClick(data: ProductResponse.Data, binding: ProductItemBinding) {
        val action =
            ProductsFragmentDirections.actionProductsFragmentToAddAndEditFragment(data._id.toString())
        findNavController().navigate(action)
    }

    override fun onButtonDeleteClick(data: ProductResponse.Data, binding: ProductItemBinding) {
        showDeleteDialog(data)
    }


    private fun showDeleteDialog(data: ProductResponse.Data) {
        MaterialAlertDialogBuilder(requireContext()).apply {
            setTitle("سيتم حذف المنتج")
            setPositiveButton("اوافق") { _, _ ->
                viewModel.deleteProduct(data._id.toString())
            }
            setNegativeButton("رجوع") { dialog, _ ->
                dialog.dismiss()
            }
            show()
        }
    }


}