package com.devYoussef.nb3elkhieradmin.ui.auth.login

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.constant.Constants
import com.devYoussef.nb3elkhieradmin.constant.Constants.dataStore
import com.devYoussef.nb3elkhieradmin.constant.Constants.getTokenSuspended
import com.devYoussef.nb3elkhieradmin.constant.Constants.showToast
import com.devYoussef.nb3elkhieradmin.data.local.DataStoreRepository
import com.devYoussef.nb3elkhieradmin.databinding.FragmentLoginBinding
import com.devYoussef.nb3elkhieradmin.model.LoginModel
import com.devYoussef.nb3elkhieradmin.utils.LoadDialogBar
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment : Fragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: LoginViewModel by viewModels()
    private val loadDialogBar: LoadDialogBar by lazy {
        LoadDialogBar(requireContext())
    }
    private lateinit var dataStoreRepository: DataStoreRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnLogin.setOnClickListener {
            if (inputsValidation(
                    LoginModel(
                        userName = binding.txtName.text.toString(),
                        password = binding.txtPassword.text.toString()
                    )
                )
            ) {
                lifecycleScope.launch {
                    callApi()
                }

            }

        }
        collectStates()

    }

    private suspend fun callApi() {
        viewModel.loginIn(
            LoginModel(
                userName = binding.txtName.text.toString(),
                password = binding.txtPassword.text.toString(),
                fcmToken = FirebaseMessaging.getInstance().getTokenSuspended() ?: ""
            )
        )
    }

    private fun collectStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect {
                when {
                    it.isLoading -> {
                        loadDialogBar.show()
                    }


                    it.error != null -> {
                        loadDialogBar.hide()
                        requireContext().showToast(it.error)
                    }


                    it.success == "success" -> {
                        requireContext().showToast("تم تسجيل الدخول بنجاح")
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        loadDialogBar.hide()
                        dataStoreRepository = DataStoreRepository(requireContext().dataStore)
                        dataStoreRepository.saveToken(Constants.TOKEN, it.login?.token.toString())
                    }


                }
            }
        }
    }

    private fun inputsValidation(user: LoginModel): Boolean {
        if (user.password!!.isNotEmpty() && user.userName!!.isNotEmpty()) {
            return true
        }

        if (user.password.isEmpty()) binding.txtPasswordContainer.error =
            "ادخل كلمة المرور"

        if (user.userName?.isEmpty() == false) binding.txtNameContainer.error =
            "ادخل اسم المستخدم"


        binding.txtPassword.doOnTextChanged { _, _, _, _ ->
            binding.txtPasswordContainer.error = null
        }
        binding.txtName.doOnTextChanged { _, _, _, _ ->
            binding.txtNameContainer.error = null
        }

        return false
    }

}