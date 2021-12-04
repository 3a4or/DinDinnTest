package com.example.dindinn.ui.orders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import com.example.dindinn.R
import com.example.dindinn.base.BaseFragment
import com.example.dindinn.data.entities.Data
import com.example.dindinn.databinding.FragmentOrdersBinding
import com.example.dindinn.ui.orderdetails.OrderDetailsFragment

class OrdersFragment : BaseFragment() {

    companion object {
        fun newInstance() = OrdersFragment()
    }

    private val viewModel: OrdersViewModel by viewModels()
    private lateinit var mBinding: FragmentOrdersBinding
    private lateinit var ordersAdapter: OrdersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setBaseViewModel(viewModel)
        mBinding = FragmentOrdersBinding.inflate(layoutInflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = viewModel
        init()
        initObserving()

        return mBinding.root
    }

    private fun init() {
        ordersAdapter = OrdersAdapter {
            navController.navigate(
                R.id.action_ordersFragment_to_orderDetailsFragment, bundleOf(
                    OrderDetailsFragment.ARG_ORDER_DETAILS to it
                )
            )
        }
        mBinding.rvOrders.adapter = ordersAdapter
        if (viewModel.items.value!!.isNullOrEmpty()) { // first load
            viewModel.getOrders()
        } else {  // when get back and items already loaded
            ordersAdapter.submitList(viewModel.items.value!!)
        }
    }

    private fun initObserving() {
        viewModel.items.observe(viewLifecycleOwner, { ordersAdapter.submitList(it) })
    }

    override fun onResume() {
        super.onResume()
        navController.currentBackStackEntry?.savedStateHandle?.getLiveData<Data>("order")
            ?.observe(viewLifecycleOwner) {
                viewModel.items.value!!.remove(it)
                ordersAdapter.submitList(viewModel.items.value!!)
            }
    }
}