package com.example.asthmaapp.view.activities

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.asthmaapp.NavGraphDirections
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private val TAG = "myLogs"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater())
        val view = binding.getRoot()
        setContentView(view)

        Log.i(TAG, "oncreate MainActivity")


        val bottomNavigationView = binding.btnNavigationList
        //ответственный чтобы заменять фрагменты в navhostfragment
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController
            // val navController = findNavController(R.id.fragmentContainer)
//Метод setupWithNavController вешает листенер на BottomNavigationView и
// выполняет навигацию при нажатии на его элементы.
        bottomNavigationView.setupWithNavController(navController)


//actionbar менялся
       setupActionBarWithNavController(findNavController(R.id.fragmentContainer))

        //обрабатываем intent, если MainActivity открывается при нажатии на уведомление, то
        // переходит на AddFragment, если нет - то открывается MainActivity, соответственно проверяется через ntent,
        //если в нем что то есть или нет
        val dateTime = intent.getStringExtra("dateTime")
        if (dateTime!=null) {
            val action = NavGraphDirections.goToAddFragmentAction(dateTime)
            navController.navigate(action)
        }
    }

//This method is called whenever the user chooses to navigate Up
// within your application's activity hierarchy from the action bar.
    ///Этот метод вызывается всякий раз, когда пользователь решает перейти Вверх
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainer)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}