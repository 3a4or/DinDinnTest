package com.example.dindinn.ui.orderdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.dindinn.R
import com.example.dindinn.base.BaseFragment
import com.example.dindinn.data.entities.Data
import com.example.dindinn.databinding.FragmentOrderDetailsBinding

class OrderDetailsFragment : BaseFragment() {

    companion object {
        const val ARG_ORDER_DETAILS = "orderDetails"
        fun newInstance(
            orderDetails: Data
        ) : OrderDetailsFragment{
            val fragment = OrderDetailsFragment()
            fragment.arguments = bundleOf(ARG_ORDER_DETAILS to orderDetails)
            return fragment
        }
    }

    private val viewModel: OrderDetailsViewModel by viewModels()
    private lateinit var mBinding: FragmentOrderDetailsBinding
    private lateinit var orderDetails: Data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setBaseViewModel(viewModel)
        mBinding = FragmentOrderDetailsBinding.inflate(layoutInflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = viewModel

        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        orderDetails = arguments?.getSerializable(ARG_ORDER_DETAILS) as Data
        showSuccess(orderDetails.title)
    }
}