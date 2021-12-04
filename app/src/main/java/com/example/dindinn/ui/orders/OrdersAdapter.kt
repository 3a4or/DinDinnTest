package com.example.dindinn.ui.orders

import android.media.MediaPlayer
import android.media.RingtoneManager
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.util.Consumer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dindinn.MyApp
import com.example.dindinn.R
import com.example.dindinn.data.entities.Data
import com.example.dindinn.databinding.ItemOrderBinding
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class OrdersAdapter(private val consumer: (Data) -> Unit)
    : ListAdapter<Data, OrdersAdapter.ItemViewHolder>(diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemOrderBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(itemBinding, consumer)
    }

    override fun onBindViewHolder(itemViewHolder: ItemViewHolder, position: Int) {
        itemViewHolder.bind(getItem(position))
    }

    class ItemViewHolder(
        private val mBinding: ItemOrderBinding,
        private val consumer: Consumer<Data>
    ) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(item: Data) {
            mBinding.item = item
            if (!item.hasCounter) {
                convertDates(item)
                item.hasCounter = true
            }
            mBinding.itemCard.setOnClickListener { consumer.accept(item) }
            mBinding.executePendingBindings()
        }

        private fun convertDates(item: Data) {
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)
            formatter.timeZone = TimeZone.getTimeZone("UTC")

            val createdAtValue = formatter.parse(item.created_at)
            val alertedAtAtValue = formatter.parse(item.alerted_at)
            val expiredAtAtValue = formatter.parse(item.expired_at)

            val alertTime = ((alertedAtAtValue.time - createdAtValue.time) / 1000) / 60
            val expiredTime = ((expiredAtAtValue.time - createdAtValue.time) / 1000) / 60
            startCounter(alertTime, expiredTime, item)
            Log.e("TTT", "Times of ${item.id} : $alertTime : expired: $expiredTime")
        }

        private fun startCounter(alertTime: Long, expiredTime: Long, item: Data) {
            Log.e("ZZZ", "Staaaaaaaaraaaaaart ------- ${item.id}")
            Observable.interval(1, TimeUnit.MINUTES)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    item.lastTime = it + 1 // we add ( + 1) because counter starts from 0
                    if (it + 1 == alertTime) {
                        Log.e("ZZZ", "alert item ${item.id} at $it")
                        startAlert(item)
                    }
                }
                .takeUntil { aLong -> aLong == expiredTime - 1 }
                .doOnComplete {
                    item.expired = true
                    Log.e("ZZZ", "finished item: ${item.id}")
                }
                .subscribe()
        }

        private fun startAlert(item: Data) {
            Toast.makeText(MyApp.appContext,
                "${item.title} ${MyApp.appContext.resources.getString(R.string.alert_order)}",
                Toast.LENGTH_LONG).show()
            val notification: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val mp: MediaPlayer = MediaPlayer.create(MyApp.appContext, notification)
            mp.start()
        }
    }

    companion object {
        val diff by lazy {
            object : DiffUtil.ItemCallback<Data?>() {
                override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}