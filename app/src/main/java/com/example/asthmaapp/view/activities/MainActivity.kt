package com.example.asthmaapp.view.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val TAG = "myLogs"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        Log.i(TAG, "oncreate MainActivity")


        val bottomNavigationView = binding.btnNavigationList
        //ответственный чтобы заменять фрагменты в navhostfragment
        val navController = findNavController(R.id.fragmentContainer)
//Метод setupWithNavController вешает листенер на BottomNavigationView и
// выполняет навигацию при нажатии на его элементы.
        bottomNavigationView.setupWithNavController(navController)


//actionbar менялся
        setupActionBarWithNavController(findNavController(R.id.fragmentContainer))

    }

//This method is called whenever the user chooses to navigate Up
// within your application's activity hierarchy from the action bar.
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainer)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}