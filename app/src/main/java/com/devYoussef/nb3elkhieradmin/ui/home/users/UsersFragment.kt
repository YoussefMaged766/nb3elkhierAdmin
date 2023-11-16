package com.devYoussef.nb3elkhieradmin.ui.home.users

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentUsersBinding
import com.devYoussef.nb3elkhieradmin.ui.adapter.UserBanAdapter
import com.devYoussef.nb3elkhieradmin.ui.adapter.UsersAdapter
import com.devYoussef.nb3elkhieradmin.ui.home.block.BlockViewModel
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment() {
    private lateinit var binding: FragmentUsersBinding
    private val viewModel: UsersViewModel by viewModels()
    private val adapter: UsersAdapter by lazy { UsersAdapter() }
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
       binding = FragmentUsersBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllUsers()
        collectStates()
        binding.swipeRefreshUsers.setOnRefreshListener {
            viewModel.getAllUsers()
            binding.swipeRefreshUsers.isRefreshing = false
        }
    }
    private fun collectStates(){
        lifecycleScope.launch {
            viewModel.stateGetAllUsers.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }
                    it.error != null -> {
                        loadDialogBar.hide()
                       requireContext().showToast( it.error)
                    }
                    it.status =="success" -> {
                        loadDialogBar.hide()
                        if (it.users?.user.isNullOrEmpty()){
                            binding.imgNoBlock.visibility = View.VISIBLE
                        } else{
                            binding.imgNoBlock.visibility = View.GONE
                            adapter.submitList(it.users?.user)
                            binding.recyclerBlocks.adapter = adapter
                        }

                    }
                }
            }
        }
    }


}