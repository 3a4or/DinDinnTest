package com.example.dindinn.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.dindinn.data.entities.Ingredient
import java.util.*

class NoFilterAdapter<T>(context: Context, resource: Int) :
    ArrayAdapter<T>(context, resource) {

    private var mFilter: Filter? = null

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        val view = super.getView(position, convertView, parent)
        (view as TextView).text = getItem(position).toString()
        return view
    }

    override fun getFilter(): Filter {
        if (mFilter == null) {
            mFilter = NoFilter(this)
        }
        return mFilter!!
    }

    private class NoFilter<T>(val noFilterAdapter: NoFilterAdapter<T>) : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()
            val list: MutableList<T> = ArrayList()
            for (i in 0 until noFilterAdapter.count) {
                list.add(noFilterAdapter.getItem(i)!!)
            }
            results.values = list
            results.count = list.size
            return results
        }

        override fun publishResults(
            constraint: CharSequence?,
            results: FilterResults
        ) {
            if (results.count > 0) {
                noFilterAdapter.notifyDataSetChanged()
            } else {
                noFilterAdapter.notifyDataSetInvalidated()
            }
        }

        override fun convertResultToString(resultValue: Any): CharSequence {
            return if (resultValue is Ingredient) resultValue.toString()
            else super.convertResultToString(resultValue)
        }
    }

}