package com.example.dindinn.ui.orderdetails

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.example.dindinn.R
import com.example.dindinn.base.BaseFragment
import com.example.dindinn.data.entities.order.Data
import com.example.dindinn.databinding.FragmentOrderDetailsBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class OrderDetailsFragment : BaseFragment() {

    companion object {
        const val ARG_ORDER_DETAILS = "orderDetails"
        fun newInstance() = OrderDetailsFragment()
    }

    private val viewModel: OrderDetailsViewModel by viewModels()
    private lateinit var mBinding: FragmentOrderDetailsBinding
    private lateinit var mCountDownTimer: CountDownTimer

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setBaseViewModel(viewModel)
        mBinding = FragmentOrderDetailsBinding.inflate(layoutInflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = viewModel
        initClicks()

        return mBinding.root
    }

    private fun initClicks() {
        mBinding.btnOkay.setOnClickListener {
            getBackAndRemoveOrder()
        }
        mBinding.btnAccept.setOnClickListener {
            viewModel.orderDetails.value!!.disposableProcess?.dispose()
            getBackAndRemoveOrder()
        }
        mBinding.btnCategories.setOnClickListener {
            navController.navigate(R.id.action_orderDetailsFragment_to_categoriesFragment)
        }
    }

    private fun getBackAndRemoveOrder() {
        // go back to previous page with data
        navController.previousBackStackEntry?.savedStateHandle?.set("order",
            viewModel.orderDetails.value)
        navController.popBackStack()
    }

    private fun setData() {
        mBinding.tvId.text = "#${viewModel.orderDetails.value!!.id}"
        // set time
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
        formatter.timeZone = TimeZone.getTimeZone("UTC")
        val createdAtValue = formatter.parse(viewModel.orderDetails.value!!.created_at)
        val cal = Calendar.getInstance()
        cal.time = createdAtValue
        val hours = cal[Calendar.HOUR]
        val minutes = cal[Calendar.MINUTE]
        mBinding.tvTime.text = getString(R.string.label_at) + " " + hours + " : " + minutes
        showHideComponents()
    }

    private fun showHideComponents() {
        if (viewModel.orderDetails.value!!.expired) {
            mBinding.tvAutoReject.isVisible = true
            mBinding.btnOkay.isVisible = true
            mBinding.btnAccept.isVisible = false
        } else {
            mBinding.tvAutoReject.isVisible = false
            mBinding.btnOkay.isVisible = false
            mBinding.btnAccept.isVisible = true
        }
        setProgressBar()
    }

    private fun setProgressBar() {
        mBinding.progressbar.progress = viewModel.orderDetails.value!!.lastTime.toInt()
        mBinding.progressbar.max = viewModel.orderDetails.value!!.expiredTime.toInt()

        val diff = viewModel.orderDetails.value!!.expiredTime - viewModel.orderDetails.value!!.lastTime
        Observable.interval(1, TimeUnit.MINUTES)
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { mBinding.progressbar.progress = viewModel.orderDetails.value!!.lastTime.toInt() }
            .takeUntil { aLong -> aLong == diff - 1 }
            .doOnComplete {
                viewModel.orderDetails.value!!.expired = true
                mBinding.tvAutoReject.isVisible = true
                mBinding.btnOkay.isVisible = true
                mBinding.btnAccept.isVisible = false
            }
            .subscribe()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.orderDetails.value = arguments?.getSerializable(ARG_ORDER_DETAILS) as Data
        setData()
    }
}