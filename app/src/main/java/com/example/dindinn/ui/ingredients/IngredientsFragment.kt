package com.example.dindinn.ui.ingredients

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.dindinn.MyApp
import com.example.dindinn.base.BaseFragment
import com.example.dindinn.databinding.FragmentIngredientsBinding

class IngredientsFragment : BaseFragment() {

    companion object {
        private const val INGREDIENT_ID = "ingredientId"
        fun newInstance(
            ingredientId: String
        ): IngredientsFragment {
            val fragment = IngredientsFragment()
            fragment.arguments = bundleOf(
                INGREDIENT_ID to ingredientId
            )
            return fragment
        }
    }

    private val viewModel: IngredientsViewModel by viewModels()
    private lateinit var mBinding: FragmentIngredientsBinding
    private lateinit var ingredientId: String
    private lateinit var ingredientsAdapter: IngredientsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setBaseViewModel(viewModel)
        mBinding = FragmentIngredientsBinding.inflate(layoutInflater, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        mBinding.viewModel = viewModel
        init()
        initObserving()

        return mBinding.root
    }

    private fun init() {
        val gridLayoutManager = GridLayoutManager(MyApp.appContext, 2, GridLayoutManager.VERTICAL, false)
        mBinding.rvIngredients.layoutManager = gridLayoutManager
        ingredientsAdapter = IngredientsAdapter()
        mBinding.rvIngredients.adapter = ingredientsAdapter
    }

    private fun initObserving() {
        viewModel.ingredientsList.observe(viewLifecycleOwner, { ingredientsAdapter.submitList(it) })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ingredientId = arguments?.getString(INGREDIENT_ID) ?: ""
        viewModel.getIngredients(ingredientId)
    }
}