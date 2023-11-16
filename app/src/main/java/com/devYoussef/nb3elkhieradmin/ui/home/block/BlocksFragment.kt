package com.devYoussef.nb3elkhieradmin.ui.home.block

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentBlocksBinding
import com.devYoussef.nb3elkhieradmin.model.BlockUsersResponse
import com.devYoussef.nb3elkhieradmin.ui.adapter.UserBanAdapter
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BlocksFragment : Fragment(), UserBanAdapter.OnItemClickListener {
    private lateinit var binding: FragmentBlocksBinding
    private val viewModel: BlockViewModel by viewModels()
    private val adapter: UserBanAdapter by lazy { UserBanAdapter(this) }
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
        binding = FragmentBlocksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllBlocks()
        collectAllBlocks()
        collectUnBlocks()
    }

    private fun collectAllBlocks() {
        lifecycleScope.launch {
            viewModel.stateGetAllBlock.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        if (it.block?.user.isNullOrEmpty()) {
                            binding.imgNoBlock.visibility = View.VISIBLE
                        } else {
                            binding.imgNoBlock.visibility = View.GONE
                            adapter.submitList(it.block?.user)
                            binding.recyclerBlocks.adapter = adapter
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

    private fun collectUnBlocks() {
        lifecycleScope.launch {
            viewModel.stateUnBlock.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.success.toString())
                        viewModel.getAllBlocks()
                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }
                }
            }
        }
    }

    override fun onItemClick(data: BlockUsersResponse.User) {
        showDialog(data._id.toString())
    }

    private fun showDialog(id: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("فك الحظر")
            .setMessage("هل تريد فك الحظر عن هذا المستخدم؟")
            .setNegativeButton("الغاء") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("فك الحظر") { dialog, which ->
                viewModel.unBlockUser(id)
            }
            .show()
    }


}