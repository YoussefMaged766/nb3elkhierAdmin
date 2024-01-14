package com.devYoussef.nb3elkhieradmin.ui.home.order.past

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.BottomSheetFilterBinding
import com.devYoussef.nb3elkhieradmin.databinding.FragmentPastOrderBinding
import com.devYoussef.nb3elkhieradmin.model.FilterType
import com.devYoussef.nb3elkhieradmin.model.OrderResponse
import com.devYoussef.nb3elkhieradmin.ui.adapter.OrderAdapter
import com.devYoussef.nb3elkhieradmin.ui.home.order.OrdersFragmentDirections
import com.devYoussef.nb3elkhieradmin.ui.home.order.OrdersViewModel
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PastOrderFragment : Fragment(), OrderAdapter.OnItemClickListener {
    private lateinit var binding: FragmentPastOrderBinding
    private val viewModel: PastOrderViewModel by viewModels()
    private val adapter: OrderAdapter by lazy { OrderAdapter(this) }
    private val loadDialogBar: LoadDialogBar by lazy {
        LoadDialogBar(requireContext())
    }
    private val filteredList = MutableStateFlow<List<OrderResponse.AllOrder>>(emptyList())
    private var sortedList = emptyList<OrderResponse.AllOrder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPastOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllOrder()
        orderCollectStates()
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.getAllOrder()
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.fabFilter.setOnClickListener {
            showButtonSheet()
        }
    }

    private fun orderCollectStates() {
        lifecycleScope.launch {
            viewModel.stateOrder.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        val list =
                            it.order?.allOrders?.filter { it.isCanceled == false && it.isDelivered == true }
                        if (list != null) {
                            filteredList.value = list
                        }
                        if (list.isNullOrEmpty()) {
                            binding.imgNoOrder.visibility = View.VISIBLE
                            binding.ordersRecyclerView.visibility = View.GONE
                        } else {
                            binding.imgNoOrder.visibility = View.GONE
                            binding.ordersRecyclerView.visibility = View.VISIBLE
                            adapter.submitList(list.sortedByDescending { it.createdAt })
                            binding.ordersRecyclerView.adapter = adapter
                        }

                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }
                }
            }
        }
    }



    private fun updateAdapter(filteredListResult: List<OrderResponse.AllOrder>) {
        // Detach adapter from RecyclerView
        binding.ordersRecyclerView.adapter = null

        // Update the adapter with the sorted list
        adapter.submitList(filteredListResult)

        // Attach adapter back to RecyclerView
        binding.ordersRecyclerView.adapter = adapter
    }

    private fun showButtonSheet() {
        val dialog = BottomSheetDialog(requireContext())
        val binding = BottomSheetFilterBinding.inflate(layoutInflater)

        binding.btnSave.setOnClickListener {
            val selectedRadioButtonId = binding.rgFilter.checkedRadioButtonId
            val filterType = when (selectedRadioButtonId) {
                R.id.rbNew -> FilterType.DATE_DESC
                R.id.rbOld -> FilterType.DATE_ASC
                R.id.rbExpensive -> FilterType.PRICE_DESC
                R.id.rbCheap -> FilterType.PRICE_ASC
                else -> null
            }
            Log.e("Selected RadioButton ID: ", selectedRadioButtonId.toString())

            val sortedList = sortList(
                filteredList.value,
                filterType ?: return@setOnClickListener
            )
            Log.e("showButtonSheet: ", sortedList.size.toString())
            updateAdapter(sortedList)
            dialog.dismiss()
        }
        dialog.setContentView(binding.root)
        dialog.show()

    }

    private fun sortList(
        list: List<OrderResponse.AllOrder>,
        sortingType: FilterType
    ): List<OrderResponse.AllOrder> {
        return when (sortingType) {
            FilterType.DATE_ASC -> list.sortedBy { it.createdAt }
            FilterType.DATE_DESC -> list.sortedByDescending { it.createdAt }
            FilterType.PRICE_ASC -> list.sortedBy { it.totalPrice }
            FilterType.PRICE_DESC -> list.sortedByDescending { it.totalPrice }
        }
    }

    override fun onItemClick(item: OrderResponse.AllOrder) {
        val action = OrdersFragmentDirections.actionOrdersFragmentToDetailsOrderFragment(
            item._id.toString(),
            "past"
        )
        findNavController().navigate(action)
    }


}