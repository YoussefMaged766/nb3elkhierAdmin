package com.devYoussef.nb3elkhieradmin.ui.home.promo

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentPromoCodesBinding
import com.devYoussef.nb3elkhieradmin.model.PromoCodeModel
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PromoCodesFragment : Fragment() {

    private lateinit var binding: FragmentPromoCodesBinding
    private val viewModel: PromoCodeViewModel by viewModels()
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

        binding.btnAddPromoCode.setOnClickListener {
            showDialogAddPromo()
        }
    }

    private fun showDialogAddPromo() {
        val builder = MaterialAlertDialogBuilder(requireContext())
        val view = layoutInflater.inflate(R.layout.promo_code_item, null)
        val switchAvailable = view.findViewById<MaterialSwitch>(R.id.switchAvailable)

        switchAvailable.visibility = View.GONE
        builder.setPositiveButton("اضافه") { dialog, which ->

            callAddPromoCodeApi(
                switch = switchAvailable,
                discount = view.findViewById(R.id.txtPrice),
                num = view.findViewById(R.id.txtNum),
                discountContainer = view.findViewById(R.id.txtPriceContainer),
                numContainer = view.findViewById(R.id.txtNumContainer)
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
                        requireContext().showToast(it.error)
                    }

                    it.success == "success" -> {
                        requireContext().showToast("تم حذف المنتج بنجاح")
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
                        requireContext().showToast(it.error)
                    }

                    it.success == "success" -> {
                        requireContext().showToast("تم تعديل المنتج بنجاح")
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
        numContainer: TextInputLayout
    ) {
        if (inputsValidation(switch, discount, num, discountContainer, numContainer)) {
            viewModel.addPromo(
                PromoCodeModel(
                    isActive = switch.isChecked,
                    price = discount.text.toString().trim().toDouble(),
                    timesNum = num.text.toString().trim().toInt()
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


}