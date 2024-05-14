package com.devYoussef.nb3elkhieradmin.ui.home.regions

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentRegionsBinding
import com.devYoussef.nb3elkhieradmin.model.LoginModel
import com.devYoussef.nb3elkhieradmin.model.PromoCodeModel
import com.devYoussef.nb3elkhieradmin.model.RegionsResponse
import com.devYoussef.nb3elkhieradmin.ui.adapter.RegionsAdapter
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegionsFragment : Fragment(), RegionsAdapter.OnItemClickListener {
    private lateinit var binding: FragmentRegionsBinding
    private val viewModel: RegionViewModel by viewModels()
    private val adapter: RegionsAdapter by lazy { RegionsAdapter(this) }
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
        binding = FragmentRegionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllRegions()
        collectGetAllState()
        collectUpdateState()
        collectGetOneRegionState()
        collectDeleteState()
        collectAddRegionState()

        binding.fabAddRegion.setOnClickListener {
            showEditDialog(type = "add")
        }

        binding.regionsRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0 && binding.fabAddRegion.isShown) {
                    binding.fabAddRegion.hide()
                } else if (dy < 0 && !binding.fabAddRegion.isShown) {
                    binding.fabAddRegion.show()
                }
            }
        })
    }


    private fun collectGetAllState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateAllRegions.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        if (it.region?.data.isNullOrEmpty()) {
                            binding.imgNoRegions.visibility = View.VISIBLE
                        } else {
                            binding.imgNoRegions.visibility = View.GONE
                            binding.regionsRecyclerView.visibility = View.VISIBLE
                            adapter.submitList(it.region?.data)
                            binding.regionsRecyclerView.adapter = adapter
                        }

                    }
                }
            }
        }
    }

    private fun collectUpdateState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateUpdateRegions.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        requireContext().showToast("تم تعديل المنطقه بنجاح")
                        viewModel.getAllRegions()
                    }
                }
            }
        }
    }

    private fun collectGetOneRegionState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateGetOneRegions.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                    }
                }
            }
        }
    }

    private fun collectDeleteState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateDeleteRegions.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        requireContext().showToast("تم حذف المنطقه بنجاح")
                        viewModel.getAllRegions()
                    }
                }
            }
        }
    }

    private fun collectAddRegionState() {
        lifecycleScope.launch {
            viewModel.stateAddRegions.collect { state ->
                if (state.isLoading) {
                    loadDialogBar.show()
                }

                if (state.error != null) {
                    loadDialogBar.hide()
                    requireContext().showToast(state.error)
                }

                if (state.status == "success") {
                    loadDialogBar.hide()
                    requireContext().showToast("تم اضافه المنطقه بنجاح")
                    viewModel.getAllRegions()
                }
            }
        }
    }

    override fun onEditItemClick(data: RegionsResponse.Data) {
        showEditDialog(data._id.toString(), data, "edit")
    }

    override fun onDeleteItemClick(data: RegionsResponse.Data) {

        showDeleteDialog(data)
    }

    private fun showDeleteDialog(data: RegionsResponse.Data) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("حذف المنطقه")
            .setMessage("هل انت متاكد من حذف المنطقه ${data.region}")
            .setNegativeButton("الغاء") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("حذف") { dialog, which ->
                viewModel.deleteRegion(data._id.toString())
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditDialog(id: String?=null, data: RegionsResponse.Data ?=null, type: String) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = layoutInflater.inflate(R.layout.add_region_item, null)
        val txtRegion = view.findViewById<TextInputEditText>(R.id.txtRegion)
        val txtLimit = view.findViewById<TextInputEditText>(R.id.txtPrice)
        val country = view.findViewById<Spinner>(R.id.spinner)


        if (type == "edit") {
            viewModel.getOneRegion(id!!)
            txtRegion.setText(data?.region)
            txtLimit.setText(data?.limit.toString())
            setCountrySpinner(data?.country.toString(), country)
            builder.setPositiveButton("تعديل") { dialog, which ->
                viewModel.updateRegion(
                    LoginModel(
                        region = txtRegion.text.toString(),
                        limit = txtLimit.text.toString().toDouble(),
                        country = country.selectedItem.toString()
                    ), id
                )
            }
        } else {
            builder.setPositiveButton("اضافه") { dialog, which ->
                viewModel.addRegion(
                    LoginModel(
                        region = txtRegion.text.toString(),
                        limit = txtLimit.text.toString().toDouble(),
                        country = country.selectedItem.toString()
                    )
                )
            }
        }

        builder.setNegativeButton("الغاء ") { dialog, which ->
            dialog.dismiss()
        }

        builder.setView(view)
        builder.create()
        builder.show()

    }

    private fun setCountrySpinner(value: String, spinner: Spinner) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.countries,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        val spinnerArray = resources.getStringArray(R.array.countries)
        val position = spinnerArray.indexOf(value)
        if (position >= 0) {
            spinner.setSelection(position)
        }
    }


}