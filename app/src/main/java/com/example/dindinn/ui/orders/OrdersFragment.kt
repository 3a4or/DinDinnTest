package com.example.dindinn.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.dindinn.base.BaseFragment
import com.example.dindinn.databinding.FragmentOrdersBinding

class OrdersFragment : BaseFragment() {

    companion object {
        fun newInstance() = OrdersFragment()
    }

    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mBinding: FragmentOrdersBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setBaseViewModel(viewModel)
        mBinding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = viewModel

        return mBinding.root
    }

}