package com.example.dindinn.ui.orderdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.dindinn.data.entities.order.Addon
import com.example.dindinn.databinding.ItemAddonBinding

class AddOnesAdapter : ListAdapter<Addon, AddOnesAdapter.ItemViewHolder>(diff){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemAddonBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(itemViewHolder: ItemViewHolder, position: Int) {
        itemViewHolder.bind(getItem(position))
    }

    class ItemViewHolder(private val mBinding: ItemAddonBinding)
        : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(item: Addon) {
            mBinding.item = item
            mBinding.executePendingBindings()
        }
    }

    companion object {
        val diff by lazy {
            object : DiffUtil.ItemCallback<Addon?>() {
                override fun areItemsTheSame(oldItem: Addon, newItem: Addon): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Addon, newItem: Addon): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}