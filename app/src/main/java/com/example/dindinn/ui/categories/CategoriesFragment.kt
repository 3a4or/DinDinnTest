package com.example.dindinn.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.dindinn.base.BaseFragment
import com.example.dindinn.databinding.FragmentCategoriesBinding
import com.example.dindinn.ui.ingredients.IngredientsFragment
import com.example.dindinn.utils.NoFilterAdapter
import com.example.dindinn.utils.PagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.AbstractMap

@AndroidEntryPoint
class CategoriesFragment : BaseFragment() {

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    private val viewModel: CategoriesViewModel by viewModels()
    private lateinit var mBinding: FragmentCategoriesBinding
    private lateinit var autoCompleteAdapter: NoFilterAdapter<String>
    private lateinit var mPagerAdapter: PagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setBaseViewModel(viewModel)
        mBinding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = viewModel
        initSearch()
        initObserving()

        return mBinding.root
    }

    private fun initObserving() {
        viewModel.categoriesList.observe(viewLifecycleOwner, {
            initPager()
        })
    }

    private fun initPager() {
        mPagerAdapter = PagerAdapter(this)
        mBinding.pager.adapter = mPagerAdapter
        val tabs: MutableList<Map.Entry<String, Fragment>> = mutableListOf()
        for (item in viewModel.categoriesList.value!!) {
            tabs.add(
                AbstractMap.SimpleEntry(
                    item.strCategory,
                    IngredientsFragment.newInstance(item.strCategory)
                )
            )
        }
        mPagerAdapter.setItems(tabs.map { it.value })
        TabLayoutMediator(mBinding.tabLayout, mBinding.pager) { tab: TabLayout.Tab, position: Int ->
            tab.text = tabs[position].key
        }.attach()
    }

    private fun initSearch() {
        autoCompleteAdapter = NoFilterAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item)
        viewModel.autoCompleteList.observe(viewLifecycleOwner, {
            autoCompleteAdapter.clear()
            autoCompleteAdapter.addAll(it)
        })
        mBinding.search.setAdapter(autoCompleteAdapter)
        mBinding.search.doAfterTextChanged {
            //ignore text change due to user choice from autoComplete menu
            if (mBinding.search.isPerformingCompletion) return@doAfterTextChanged
            if (it.isNullOrBlank()) return@doAfterTextChanged
            if (it.length < 3){
                autoCompleteAdapter.clear()
                autoCompleteAdapter.addAll(viewModel.autoCompleteList.value!!)
                return@doAfterTextChanged
            }
            viewModel.autoComplete(it.toString())
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCategories()
    }
}