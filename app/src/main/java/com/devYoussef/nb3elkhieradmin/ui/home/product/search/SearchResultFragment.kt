package com.devYoussef.nb3elkhieradmin.ui.home.product.search

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.devYoussef.nab3elkheir.ui.adapter.paging.LoadStateAdapter
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentSearchResultBinding
import com.devYoussef.nb3elkhieradmin.databinding.ProductItemBinding
import com.devYoussef.nb3elkhieradmin.model.AuthResponse
import com.devYoussef.nb3elkhieradmin.model.ProductResponse
import com.devYoussef.nb3elkhieradmin.ui.adapter.paging.ProductsPagingAdapter
import com.devYoussef.nb3elkhieradmin.ui.home.product.ProductsFragmentDirections
import com.devYoussef.nb3elkhieradmin.ui.home.product.ProductsViewModel
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import retrofit2.HttpException

@AndroidEntryPoint
class SearchResultFragment : Fragment(), ProductsPagingAdapter.OnButton1ClickListener {
    private lateinit var binding: FragmentSearchResultBinding
    private val loadDialogBar: LoadDialogBar by lazy {
        LoadDialogBar(requireContext())
    }
    private val viewModel by viewModels<ProductsViewModel>()
    private val searchAdapter by lazy { ProductsPagingAdapter(this) }
    private val args :SearchResultFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectSearchState()
        val title  = requireActivity().findViewById<TextView>(R.id.txtTitleToolBar)
        title.text = args.query
        viewModel.getPagingSearchedProducts(args.query)
        binding.swipeRefreshLayout.setOnRefreshListener {
            searchAdapter.refresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        collectDeleteState()
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
                        searchAdapter.refresh()
                    }
                }
                Log.e("collectDeleteState1: ", it.status.toString())
            }
        }
    }

    private fun collectSearchState() {
        lifecycleScope.launch {
            viewModel.data.collect {
                searchAdapter.submitData(it)
                binding.recyclerViewSearchResult.adapter =
                    searchAdapter.withLoadStateHeaderAndFooter(
                        header = LoadStateAdapter { searchAdapter.retry() },
                        footer = LoadStateAdapter { searchAdapter.retry() }
                    )
            }

        }
        searchAdapter.addLoadStateListener { loadState ->
            binding.swipeRefreshLayout.isRefreshing = loadState.refresh is LoadState.Loading

            if (searchAdapter.itemCount == 0) {
                binding.imgEmptySearchProducts.visibility = View.VISIBLE
            } else {
                binding.imgEmptySearchProducts.visibility = View.GONE
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
            SearchResultFragmentDirections.actionSearchResultFragmentToAddAndEditFragment(data._id.toString())
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