package com.example.dindinn.base

import android.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.dindinn.R
import com.google.android.material.snackbar.Snackbar

abstract class BaseFragment : Fragment() {

    val navController: NavController by lazy {
        return@lazy (requireActivity() as BaseActivity).navController
    }
    private lateinit var mBaseViewModel: BaseViewModel

    protected fun setBaseViewModel(baseViewModel: BaseViewModel) {
        mBaseViewModel = baseViewModel
    }

    protected open fun showSuccess(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.purple_500))
            view.background =
                ResourcesCompat.getDrawable(resources, R.drawable.round_corner_bg, null)
            setActionTextColor(Color.WHITE)
            setAction(R.string.dismiss) { dismiss() }
        }.show()
    }

    protected open fun showError(message: String) {
        Snackbar.make(
            requireActivity().findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).apply {
            setBackgroundTint(ContextCompat.getColor(requireContext(), R.color.color_filter4))
            view.background =
                ResourcesCompat.getDrawable(resources, R.drawable.round_corner_bg, null)
            setActionTextColor(Color.WHITE)
            setAction(R.string.dismiss) { dismiss() }
        }.show()
    }
}