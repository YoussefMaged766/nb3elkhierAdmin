package com.devYoussef.nb3elkhieradmin.ui.home.order.details

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants.getAddressFromLatLng
import com.devYoussef.nb3elkhieradmin.constant.Constants.handleBackButton
import com.devYoussef.nb3elkhieradmin.constant.Constants.handleToolbarNavigation
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.databinding.FragmentDetailsOrderBinding
import com.devYoussef.nb3elkhieradmin.databinding.ProductItemBinding
import com.devYoussef.nb3elkhieradmin.model.LoginModel
import com.devYoussef.nb3elkhieradmin.model.OrderDetailsResponse
import com.devYoussef.nb3elkhieradmin.model.OrderResponse
import com.devYoussef.nb3elkhieradmin.model.ProductResponse
import com.devYoussef.nb3elkhieradmin.model.dummyProduct
import com.devYoussef.nb3elkhieradmin.ui.adapter.OrderDetailsAdapter
import com.devYoussef.nb3elkhieradmin.ui.home.product.ProductsFragmentDirections
import com.devYoussef.nb3elkhieradmin.ui.home.product.ProductsViewModel
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import com.devYoussef.nb3elkhieradmin.utils.Status
import com.devYoussef.nb3elkhieradmin.utils.getFilePathFromUri
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@AndroidEntryPoint
class DetailsOrderFragment : Fragment(), OrderDetailsAdapter.OnItemClickListener {
    private lateinit var binding: FragmentDetailsOrderBinding
    private val args by navArgs<DetailsOrderFragmentArgs>()
    private val viewModel: OrderDetailsViewModel by viewModels()
    private val productsViewModel: ProductsViewModel by viewModels()
    private val adapter: OrderDetailsAdapter by lazy { OrderDetailsAdapter(this, args.type) }
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
        binding = FragmentDetailsOrderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)

        val navigationLogic:()->Unit = {
            if (findNavController().previousBackStackEntry != null) {
                findNavController().navigate(R.id.ordersFragment)
            } else {
                findNavController().popBackStack()
            }
        }
//        handleBackButton(navigationLogic)
//        handleToolbarNavigation(toolbar, nav igationLogic)

        Log.e("onViewCreated: ", args.id.toString())
        viewModel.getOrderDetails(args.id)
        showButtons()
        collectAcceptOrderStates()
        collectDeclineOrderStates()
        orderCollectStates()
        collectBanUsersStates()
        collectDeleteProductStates()
        setDividerToRecyclerProducts()
        setHasOptionsMenu(true)
        binding.btnAccept.setOnClickListener {
            showAcceptDialog(args.id)
        }
        binding.btnDecline.setOnClickListener {
            showDeclineDialog(args.id)
        }
        binding.txtPhone.setOnLongClickListener { view ->
            val clipboard =
                requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("phone", binding.txtPhone.text)
            clipboard.setPrimaryClip(clip)
            requireContext().showToast("تم نسخ الرقم")
            true
        }



    }

    private fun showButtons() {
        when (args.type) {
            "current" -> {
                binding.btnAccept.visibility = View.VISIBLE
                binding.btnDecline.visibility = View.VISIBLE
                binding.txt10.visibility = View.VISIBLE
                binding.txtBan.visibility = View.VISIBLE

            }

            "past" -> {
                binding.btnAccept.visibility = View.GONE
                binding.btnDecline.visibility = View.GONE
                binding.txt10.visibility = View.GONE
                binding.txtBan.visibility = View.GONE
            }

            "cancel" -> {
                binding.btnAccept.visibility = View.GONE
                binding.btnDecline.visibility = View.GONE
                binding.txt10.visibility = View.GONE
                binding.txtBan.visibility = View.GONE
            }

        }
    }

    @SuppressLint("SetTextI18n")
    private fun orderCollectStates() {
        lifecycleScope.launch {
            viewModel.stateOrder.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                        Log.i("TAG", "orderCollectStates: loading ")
                    }

                    it.status == "success" -> {
                        Log.i("TAG", "orderCollectStates: success ")
                        loadDialogBar.hide()
                        if (!it.orderDetails?.userOrder.isNullOrEmpty()) {
                            adapter.submitList(it.orderDetails?.userOrder?.get(0)?.products)
                            binding.rvOrderDetails.adapter = adapter
                            binding.txtOrderNumber.text =
                                it.orderDetails?.userOrder?.get(0)?.orderNum
                            binding.txtShopAddress.text =
                                it.orderDetails?.userOrder?.get(0)?.userId?.shopName

                            binding.txtAddress.text =addMapHyperLink(
                                it.orderDetails?.userOrder?.get(0)?.location?.latitude!!,
                                it.orderDetails.userOrder[0].location?.longitude!!
                            )
                            binding.txtAddressDetails.text = getAddressFromLatLng(
                                it.orderDetails.userOrder[0].location?.latitude!!,
                                it.orderDetails.userOrder[0].location?.longitude!!
                            )
                            binding.txtPhone.text = it.orderDetails?.userOrder?.get(0)?.phone
                            binding.txtNotes.text = it.orderDetails?.userOrder?.get(0)?.note
                            binding.txtDate.text =
                                convertDateToCustomFormat(it.orderDetails?.userOrder?.get(0)?.createdAt!!)
                            binding.txtTotalTotalPrice.text =
                                "${it.orderDetails.userOrder[0].totalPrice} ${
                                    it.orderDetails.userOrder[0].products?.get(0)?.productId?.priceCurrency
                                }"
                            binding.txtTotalQuantity.text =
                                it.orderDetails.userOrder[0].quantity.toString()

                            val price = it.orderDetails.userOrder[0].promoCode?.price ?: 0.0
                            binding.txtPromoPrice.text =
                                "$price ${
                                    it.orderDetails.userOrder[0].promoCode?.unitValue ?: ""
                                }"

                            binding.txtBan.setOnClickListener { view ->
                                showDialogBan(it.orderDetails.userOrder[0].userId?._id!!)
                            }
                            if (it.orderDetails.userOrder[0].userId?.isBlocked == true) {
                                binding.txtBan.visibility = View.GONE
                                binding.txt10.visibility = View.GONE
                            }
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

    private fun setDividerToRecyclerProducts(){
        val dividerItemDecoration = DividerItemDecoration(
            binding.rvOrderDetails.context,
            DividerItemDecoration.VERTICAL
        )
        binding.rvOrderDetails.addItemDecoration(dividerItemDecoration)
    }

    private fun addMapHyperLink(lat:Double , lon:Double): CharSequence {
        val mapUrl = "https://www.google.com/maps/search/?api=1&query=${lat},${lon}"
        val clickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
                startActivity(intent)
            }
        }
        val ss = SpannableString("اضغط هنا ") // Use the address text as the link text
        Log.e( "addMapHyperLink: ","$ss $lat $lon" )
        ss.setSpan(clickableSpan, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.txtAddress.movementMethod = LinkMovementMethod.getInstance()
        return ss
    }

    private fun collectAcceptOrderStates() {
        lifecycleScope.launch {
            viewModel.stateAcceptOrder.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        requireContext().showToast("تم قبول الطلب")
                        findNavController().popBackStack()
                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }
                }
            }
        }
    }

    private fun collectDeclineOrderStates() {
        lifecycleScope.launch {
            viewModel.stateDeclineOrder.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        requireContext().showToast("تم رفض الطلب")
                        findNavController().popBackStack()
                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }
                }
            }
        }
    }

    private fun collectBanUsersStates() {
        lifecycleScope.launch {
            viewModel.stateBlock.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }

                    it.status == "success" -> {
                        loadDialogBar.hide()
                        requireContext().showToast("تم حظر المستخدم")
                        findNavController().popBackStack()
                    }

                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }
                }
            }
        }
    }

    private fun collectDeleteProductStates() {
        lifecycleScope.launch {
            viewModel.stateDeleteOrder.collect {
                when {
                    it.isLoading -> {
                    }

                    it.status == "success" -> {
                        requireContext().showToast(it.success.toString())

                    }

                    it.error != null -> {
                        requireContext().showToast(it.error)
                    }
                }
            }
        }
    }

    private fun convertDateToCustomFormat(inputDate: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        inputFormat.timeZone = TimeZone.getTimeZone("UTC") // Ensure UTC time zone

        val outputFormat = SimpleDateFormat("hh:mm a|dd-MM-yyyy", Locale.ENGLISH)
        outputFormat.timeZone = TimeZone.getDefault() // Use the default time zone

        val parsedDate: Date? = try {
            inputFormat.parse(inputDate)
        } catch (e: Exception) {
            null
        }

        return if (parsedDate != null) {
            outputFormat.format(parsedDate)
        } else {
            ""
        }
    }

    private fun showDialogBan(userId: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("حظر المستخدم")
            .setMessage("هل تريد حظر هذا المستخدم؟")
            .setNegativeButton("الغاء") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("حظر") { dialog, which ->
                viewModel.blockUser(userId)
            }
            .show()
    }

    private fun showAcceptDialog(id: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("قبول الطلب")
            .setMessage("هل تريد قبول هذا الطلب؟")
            .setNegativeButton("الغاء") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("قبول") { dialog, which ->
                viewModel.acceptOrder(id)
            }
            .show()
    }

    private fun showDeclineDialog(id: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("رفض الطلب")
            .setMessage("هل تريد رفض هذا الطلب؟")
            .setNegativeButton("الغاء") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("رفض") { dialog, which ->
                viewModel.declineOrder(id)
            }
            .show()
    }

    private fun showDeleteDialog(id: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("حذف المنتج")
            .setMessage("هل تريد حذف هذا المنتج من الطلب؟")
            .setNegativeButton("الغاء") { dialog, which ->
                dialog.dismiss()
            }
            .setPositiveButton("حذف") { dialog, which ->
                viewModel.deleteProductFromOrder(LoginModel(productId = id, orderId = args.id))
                viewModel.getOrderDetails(args.id)
            }
            .show()
    }

    private fun showCustomizeDialog(id : String){
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("هل المنتج متاح؟")
            .setNegativeButton("متاح"){ dialog, which ->
                showDeleteDialog(id)
                dialog.dismiss()
            }
            .setPositiveButton("غير متاح"){ dialog, which ->
//                onButtonEditClick(id)
                showDeleteDialog(id)
                productsViewModel.getOneProduct(id)
                lifecycleScope.launch{
                    productsViewModel.stateProduct.collect{
                        when{
                            it.isLoading ->{

                            }
                            it.status == "success"-> {
                                Log.i("TAG", "showCustomizeDialog: ${it.product?.data?.get(0)}")
                                val product = it.product?.data?.get(0)
                                callApiUpdateProduct(product!!)
                            }
                        }
                    }

                }

            }
            .show()
    }
    private fun callApiUpdateProduct(product: ProductResponse.Data) {
        val name = product.name
        val shortDescription = product.shortDescription
        val category = product.category
        val country = product.country
        val isAvailable = false
        val isOffered = product.isOffered
        val quantity = product.quantity.toString()
        val price = product.price.toString()
        val priceCurrency = product.priceCurrency.toString()
        val originalPrice = product.originalPrice.toString()
        val id = product._id
        Log.e("callApiUpdateProduct: ", category.toString())

        val offerPrice = product.offer?.get(0)?.priceOffered.toString()
        val offerItemNum = product.offer?.get(0)?.itemNum.toString()

            productsViewModel.updateProduct(
                ctx = requireContext(),
                name = name?: "",
                shortDescription = shortDescription ?: "",
                category = category ?: "",
                country = country?: "",
                isAvailable = isAvailable,
                isOffered = isOffered!!,
                quantity = quantity,
                price = price,
                priceCurrency = priceCurrency,
                originalPrice = originalPrice,
                offerPrice = offerPrice,
                offerItemNum = offerItemNum,
                id = id!!
            )
    }

    override fun onItemClick(position: Int, item: OrderDetailsResponse.UserOrder.Product) {
        showCustomizeDialog(item.productId?._id!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                if (findNavController().previousBackStackEntry != null) {
                    findNavController().navigate(R.id.ordersFragment)
                } else {
                    findNavController().popBackStack()
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}