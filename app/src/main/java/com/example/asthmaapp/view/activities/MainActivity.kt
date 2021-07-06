package com.example.asthmaapp.view.activities

import android.app.ActionBar
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
        val view = binding.getRoot()
        setContentView(view)

        val bottomNavigationView = binding.btnNavigationView
        //ответственный чтобы заменять фрагменты в navhostfragment

        // Метод setupWithNavController вешает листенер на BottomNavigationView и
        // выполняет навигацию при нажатии на его элементы.
        bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.listFragment -> binding.btnNavigationView.visibility = View.VISIBLE
                R.id.alarmFragment -> binding.btnNavigationView.visibility = View.VISIBLE
                R.id.medicalFragment -> binding.btnNavigationView.visibility = View.VISIBLE
                R.id.helloFragment -> binding.btnNavigationView.visibility = View.GONE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val actionBar: ActionBar? = actionBar
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(false) // Disable the button
            actionBar.setDisplayHomeAsUpEnabled(false) // Remove the left caret
            actionBar.setDisplayShowHomeEnabled(false) // Remove the icon
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainer)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}