package com.devYoussef.nb3elkhieradmin.ui.home.order.current

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentCurrentOrderBinding
import com.devYoussef.nb3elkhieradmin.model.OrderResponse
import com.devYoussef.nb3elkhieradmin.ui.adapter.OrderAdapter
import com.devYoussef.nb3elkhieradmin.ui.home.order.OrdersFragmentDirections
import com.devYoussef.nb3elkhieradmin.ui.home.order.OrdersViewModel
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CurrentOrderFragment : Fragment(), OrderAdapter.OnItemClickListener {
    private lateinit var binding: FragmentCurrentOrderBinding
    private val viewModel: OrdersViewModel by viewModels()
    private val adapter: OrderAdapter by lazy { OrderAdapter(this) }
    private val loadDialogBar: LoadDialogBar by lazy {
        LoadDialogBar(requireContext())
    }
    private val filteredList = MutableStateFlow<List<OrderResponse.AllOrder>>(emptyList())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCurrentOrderBinding.inflate(inflater, container, false)
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
        addMenu()
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
                            it.order?.allOrders?.filter { it.isCanceled == false && it.isDelivered == false }
                        if (list != null) {
                            filteredList.value = list
                        }
                        if (list.isNullOrEmpty()) {
                            binding.imgNoOrder.visibility = View.VISIBLE
                            binding.ordersRecyclerView.visibility = View.GONE
                        } else {
                            binding.imgNoOrder.visibility = View.GONE
                            binding.ordersRecyclerView.visibility = View.VISIBLE
                            adapter.submitList(list)
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

    private fun addMenu() {
        val imgFilter = requireActivity().findViewById<ImageView>(R.id.imgFilter)
        imgFilter.setOnClickListener {
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.menuInflater.inflate(R.menu.filter_menu, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_sort_by_date_asc -> {
                      val filteredListResult =  filteredList.value.sortedWith(compareBy { it.createdAt })
                        adapter.submitList(filteredListResult)

                        true
                    }

                    R.id.action_sort_by_date_desc -> {
                      val filteredListResult =  filteredList.value.sortedWith(compareByDescending { it.createdAt })
                        adapter.submitList(filteredListResult)
                        true
                    }

                    R.id.action_sort_by_price_asc -> {
                      val filteredListResult=  filteredList.value.sortedWith(compareBy { it.totalPrice })
                        adapter.submitList(filteredListResult)
                        Log.e( "addMenu: ",filteredListResult.toString() )
                        true
                    }

                    R.id.action_sort_by_price_desc -> {
                      val filteredListResult =  filteredList.value.sortedWith(compareByDescending { it.totalPrice })
                        adapter.submitList(filteredListResult)
                        Log.e( "addMenu: ",filteredListResult.toString() )
                        true
                    }

                    else -> false
                }
            }
            binding.ordersRecyclerView.adapter = adapter

            popupMenu.show()
        }

    }

    override fun onItemClick(item: OrderResponse.AllOrder) {
      val action = OrdersFragmentDirections.actionOrdersFragmentToDetailsOrderFragment(item._id.toString() , "current")
        findNavController().navigate(action)
    }


}