package com.rightname.asthmaapp.view.activities

import android.app.ActionBar
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.rightname.asthmaapp.NavGraphDirections
import com.rightname.asthmaapp.R
import com.rightname.asthmaapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val view = binding.root
        setContentView(view)

        val bottomNavigationView = binding.btnNavigationView

        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.listFragment -> binding.btnNavigationView.visibility = View.VISIBLE
                R.id.alarmFragment -> binding.btnNavigationView.visibility = View.VISIBLE
                R.id.medicalFragment -> binding.btnNavigationView.visibility = View.VISIBLE
                R.id.viewPagerFragment -> binding.btnNavigationView.visibility = View.GONE
            }
        }

        if (onBoardingFinished()) {
            val action = NavGraphDirections.actionGlobalListFragment2()
            navController.navigate(action)

        } else {
            val action = NavGraphDirections.actionGlobalViewPagerFragment4()
            navController.navigate(action)
        }
    }

    private fun onBoardingFinished(): Boolean {
        val sharedPref = applicationContext.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("Finished", false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val actionBar: ActionBar? = actionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false)
            actionBar.setDisplayHomeAsUpEnabled(false)
            actionBar.setDisplayShowHomeEnabled(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainer)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}