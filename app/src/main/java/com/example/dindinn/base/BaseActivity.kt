package com.example.dindinn.base

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.dindinn.R
import com.example.dindinn.data.network.NetworkEvent
import com.example.dindinn.data.network.NetworkState
import com.example.dindinn.databinding.ActivityBaseBinding
import com.google.android.material.snackbar.Snackbar

class BaseActivity : AppCompatActivity() {

    private lateinit var activityBaseBinding: ActivityBaseBinding
    private val viewModel: BaseViewModel by viewModels()
    lateinit var navController : NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityBaseBinding = ActivityBaseBinding.inflate(layoutInflater)
        setContentView(activityBaseBinding.root)
        setSupportActionBar(activityBaseBinding.toolbar)
        activityBaseBinding.lifecycleOwner = this
        init()
    }

    private fun init() {
        navController = findNavController(R.id.main_nav_fragment)
        setSupportActionBar(activityBaseBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        NavigationUI.setupActionBarWithNavController(this, navController)
        activityBaseBinding.viewModel = viewModel

        navController.addOnDestinationChangedListener { _, destination, arguments ->
            var label = destination.label?:""
            if (label.startsWith("{")) {
                label = arguments?.getString(label.removePrefix("{").removeSuffix("}").toString())?:""
            }
            activityBaseBinding.toolbarTitle.text = label
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return when (navController.currentDestination?.id) {
            R.id.orderDetailsFragment, R.id.categoriesFragment -> {
                onBackPressed()
                true
            }
            else -> super.onSupportNavigateUp()
        }
    }

    fun showHideProgress(show: Boolean) {
        viewModel.dataLoading.value = show
    }

    private fun showError(message: String) {
        Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        ).apply {
            setBackgroundTint(ContextCompat.getColor(this@BaseActivity, R.color.color_filter4))
            view.background =
                ResourcesCompat.getDrawable(resources, R.drawable.round_corner_bg, null)
            setActionTextColor(Color.WHITE)
            setAction(R.string.dismiss) { dismiss() }
        }.show()
    }

    override fun onResume() {
        super.onResume()
        NetworkEvent.instance?.register(this) { networkState ->
            when (networkState) {
                NetworkState.NO_INTERNET -> showError("No Internet Connection")
                NetworkState.NO_RESPONSE, NetworkState.SERVER_ERROR -> showError("No Response")
                NetworkState.API_NOT_FOUND -> showError("No Api")
                NetworkState.UNAUTHORISED -> showError("Unauthorized")
                NetworkState.NOT_ALLOWED_METHOD -> showError("Not Allowed")
                NetworkState.MAINTENANCE -> showError("Maintenance Mood")
                NetworkState.BAD_REQUEST -> showError("Bad Request")
            }
        }
    }

    override fun onStop() {
        super.onStop()
        NetworkEvent.instance?.unregister(this)
    }
}