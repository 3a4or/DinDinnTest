package com.example.dindinn.ui.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import com.example.dindinn.R
import com.example.dindinn.base.BaseFragment
import com.example.dindinn.databinding.FragmentCategoriesBinding
import com.example.dindinn.utils.NoFilterAdapter

class CategoriesFragment : BaseFragment() {

    companion object {
        fun newInstance() = CategoriesFragment()
    }

    private val viewModel: CategoriesViewModel by viewModels()
    private lateinit var mBinding: FragmentCategoriesBinding
    private lateinit var autoCompleteAdapter: NoFilterAdapter<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setBaseViewModel(viewModel)
        mBinding = FragmentCategoriesBinding.inflate(layoutInflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = viewModel
        initSearch()

        return mBinding.root
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
}