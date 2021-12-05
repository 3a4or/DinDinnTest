package com.example.dindinn.ui.orderdetails

import com.example.dindinn.base.BaseViewModel
import com.example.dindinn.data.entities.order.Data
import com.example.dindinn.utils.SingleLiveEvent

class OrderDetailsViewModel : BaseViewModel() {

    val orderDetails = SingleLiveEvent<Data>()
}