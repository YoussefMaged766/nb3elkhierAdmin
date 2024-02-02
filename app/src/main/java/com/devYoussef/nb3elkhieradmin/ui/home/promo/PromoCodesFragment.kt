package com.devYoussef.nb3elkhieradmin.ui.home.promo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentPromoCodesBinding
import com.devYoussef.nb3elkhieradmin.databinding.PromoCodeItemBinding
import com.devYoussef.nb3elkhieradmin.model.PromoCodeModel
import com.devYoussef.nb3elkhieradmin.model.PromoCodeResponse
import com.devYoussef.nb3elkhieradmin.ui.adapter.PromoAdapter
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromoCodesFragment : Fragment(), PromoAdapter.OnItemClickListener {

    private lateinit var binding: FragmentPromoCodesBinding
    private val viewModel: PromoCodeViewModel by viewModels()
    private val adapter by lazy { PromoAdapter(this) }
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
        binding = FragmentPromoCodesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectAddPromoState()
        collectDeleteState()
        collectUpdateState()
        collectGetAllState()
        collectGetOnePromoState()

        viewModel.getAllPromo()
        binding.swipeRefreshPromoCodes.setOnRefreshListener {
            viewModel.getAllPromo()
            binding.swipeRefreshPromoCodes.isRefreshing = false
        }

        binding.btnAddPromoCode.setOnClickListener {
            showDialogAddPromo()
        }
    }

    private fun showDialogAddPromo() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = layoutInflater.inflate(R.layout.promo_code_item, null)
        val switchAvailable = view.findViewById<MaterialSwitch>(R.id.switchAvailable)
        val txtCode = view.findViewById<TextInputLayout>(R.id.txtCodeContainer)
        val spinnerTypeError = view.findViewById<TextView>(R.id.txtSpinnerTypeError)
        spinnerTypeError.visibility = View.VISIBLE
        txtCode.visibility = View.GONE
        switchAvailable.visibility = View.GONE

        isTypeSelected(view.findViewById(R.id.spinnerType) , spinnerTypeError  , view.findViewById(R.id.txtPrice))
        builder.setPositiveButton("اضافه") { dialog, which ->
            callAddPromoCodeApi(
                switch = switchAvailable,
                discount = view.findViewById(R.id.txtPrice),
                num = view.findViewById(R.id.txtNum),
                discountContainer = view.findViewById(R.id.txtPriceContainer),
                numContainer = view.findViewById(R.id.txtNumContainer),
                unitValue = view.findViewById(R.id.spinnerType)
            )
        }
        builder.setNegativeButton("الغاء") { dialog, which ->
            dialog.dismiss()
        }
        builder.setView(view)
        builder.create()
        builder.show()
    }

    private fun collectAddPromoState() {
        lifecycleScope.launch {
            viewModel.stateAddPromo.collect { state ->
                if (state.isLoading) {
                    loadDialogBar.show()
                }

                if (state.error != null) {
                    loadDialogBar.hide()
                    requireContext().showToast(state.error)
                }

                if (state.status == "success") {
                    loadDialogBar.hide()
                    requireContext().showToast("تم اضافه الكود بنجاح")
                    viewModel.getAllPromo()
                }
            }
        }
    }

    private fun collectDeleteState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateDeletePromo.collect {
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
                        requireContext().showToast("تم حذف المنتج بنجاح")
                        viewModel.getAllPromo()
                    }
                }
            }
        }
    }

    private fun collectGetAllState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateAllPromo.collect {
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
                        if (it.promo?.data.isNullOrEmpty()) {
                            binding.imgNoPromoCode.visibility = View.VISIBLE
                        } else {
                            binding.imgNoPromoCode.visibility = View.GONE
                            binding.rvPromoCodes.visibility = View.VISIBLE
                            adapter.submitList(it.promo?.data)
                            binding.rvPromoCodes.adapter = adapter
                        }

                    }
                }
            }
        }
    }

    private fun collectUpdateState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateUpdatePromo.collect {
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
                        requireContext().showToast("تم تعديل المنتج بنجاح")
                        viewModel.getAllPromo()
                    }
                }
            }
        }
    }

    private fun collectGetOnePromoState() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.stateGetOnePromo.collect {
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

    private fun callAddPromoCodeApi(
        switch: MaterialSwitch,
        discount: EditText,
        num: EditText,
        discountContainer: TextInputLayout,
        numContainer: TextInputLayout,
        unitValue :Spinner
    ) {
        if (inputsValidation(switch, discount, num, discountContainer, numContainer)) {
            viewModel.addPromo(
                PromoCodeModel(
                    isActive = switch.isChecked,
                    price = discount.text.toString().trim().toDouble(),
                    timesNum = num.text.toString().trim().toInt(),
                    unitValue = unitValue.selectedItem.toString()
                )
            )
        }

    }

    private fun inputsValidation(
        switch: MaterialSwitch,
        discount: EditText,
        num: EditText,
        discountContainer: TextInputLayout,
        numContainer: TextInputLayout
    ): Boolean {
        if (discount.text.toString().trim().isNotEmpty() && num.text.toString().trim()
                .isNotEmpty() && switch.isChecked
        ) {
            return true
        }

        if (discount.text.isEmpty()) discountContainer.error = "ادخل الخصم"

        if (num.text.isEmpty()) numContainer.error =
            "ادخل عدد مرات استخدام الكود"

        if (!switch.isChecked) switch.error = "اختر الحاله"



        discount.doOnTextChanged { _, _, _, _ ->
            discountContainer.error = null
        }
        num.doOnTextChanged { _, _, _, _ ->
            numContainer.error = null
        }

        return false
    }

    private fun isTypeSelected( unitValue :Spinner , itemSelect : TextView , price:EditText) {
        unitValue.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (unitValue.selectedItem.toString() == "النوع") {
                    itemSelect.visibility = View.VISIBLE
                    itemSelect.text = "اختر النوع"
                }
                if (unitValue.selectedItem.toString() == "%"){
                    if (price.text.toString().isNotEmpty() && price.text.toString().toInt()>=100){
                        itemSelect.visibility = View.VISIBLE
                        itemSelect.text = "الخصم لا يمكن ان يكون اقل من 100%"
                    } else itemSelect.visibility = View.GONE
                }
                else itemSelect.visibility = View.GONE
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                itemSelect.visibility = View.VISIBLE
            }
        }
    }

    private fun showDialogEditPromo(id: String, data: PromoCodeResponse.Data) {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = layoutInflater.inflate(R.layout.promo_code_item, null)
        val switchAvailable = view.findViewById<MaterialSwitch>(R.id.switchAvailable)
        val txtCodeContainer = view.findViewById<TextInputLayout>(R.id.txtCodeContainer)
        val txtPrice = view.findViewById<EditText>(R.id.txtPrice)
        val txtNum = view.findViewById<EditText>(R.id.txtNum)
        txtCodeContainer.visibility = View.GONE

        isTypeSelected(view.findViewById(R.id.spinnerType) , view.findViewById(R.id.txtSpinnerTypeError) , txtPrice)


        viewModel.getOnePromo(id)
        switchAvailable.isChecked = data.isActive!!
        txtPrice.setText(data.price.toString())
        txtNum.setText(data.timesNum.toString())
        setTypeSpinner(data.unitValue.toString(), PromoCodeItemBinding.bind(view))

        builder.setPositiveButton("تعديل") { dialog, which ->
            viewModel.updatePromo(
                PromoCodeModel(
                    isActive = switchAvailable.isChecked,
                    price = txtPrice.text.toString().trim().toDouble(),
                    timesNum = txtNum.text.toString().trim().toInt(),
                    unitValue = view.findViewById<Spinner>(R.id.spinnerType).selectedItem.toString()
                ), id
            )
        }
        builder.setNegativeButton("حذف ") { dialog, which ->
            viewModel.deletePromo(id)
        }
        builder.setNeutralButton("الغاء") { dialog, which ->
            dialog.dismiss()
        }
        builder.setView(view)
        builder.create()
        builder.show()
    }

    private fun setTypeSpinner(value: String , binding: PromoCodeItemBinding) {
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.type,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerType.adapter = adapter
        val spinnerArray = resources.getStringArray(R.array.type)
        val position = spinnerArray.indexOf(value)
        if (position >= 0) {
            binding.spinnerType.setSelection(position)
        }
    }

    override fun onItemClick(data: PromoCodeResponse.Data) {
        showDialogEditPromo(data._id!!, data)

    }


}