package com.example.dindinn.ui.ingredients

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.dindinn.MyApp
import com.example.dindinn.R
import com.example.dindinn.data.entities.ingredients.Ingredient
import com.example.dindinn.databinding.ItemIngredientBinding

class IngredientsAdapter : ListAdapter<Ingredient, IngredientsAdapter.ItemViewHolder>(diff){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = ItemIngredientBinding.inflate(layoutInflater, parent, false)
        return ItemViewHolder(itemBinding)
    }

    override fun onBindViewHolder(itemViewHolder: ItemViewHolder, position: Int) {
        itemViewHolder.bind(getItem(position))
    }

    class ItemViewHolder(private val mBinding: ItemIngredientBinding)
        : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(item: Ingredient) {
            mBinding.item = item
            Glide.with(MyApp.appContext).load(item.strDrinkThumb)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(mBinding.imgThumb)
            mBinding.executePendingBindings()
        }
    }

    companion object {
        val diff by lazy {
            object : DiffUtil.ItemCallback<Ingredient?>() {
                override fun areItemsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                    return oldItem.idDrink == newItem.idDrink
                }

                override fun areContentsTheSame(oldItem: Ingredient, newItem: Ingredient): Boolean {
                    return oldItem == newItem
                }
            }
        }
    }
}