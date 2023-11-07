package com.devYoussef.nb3elkhieradmin.ui.home.order.current

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.devYoussef.nb3elkhieradmin.R
import com.devYoussef.nb3elkhieradmin.databinding.FragmentCurrentOrderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CurrentOrderFragment : Fragment() {
    private lateinit var binding: FragmentCurrentOrderBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentCurrentOrderBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }


}