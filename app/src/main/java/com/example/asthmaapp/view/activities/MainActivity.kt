package com.example.asthmaapp.view.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.asthmaapp.NavGraphDirections
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.ActivityMainBinding
import com.example.asthmaapp.view.fragments.HelloFragment


class MainActivity : AppCompatActivity() {

    private val TAG = "myLogs"
    val MY_SETTINGS = "1"

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(getLayoutInflater())

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        val navController = navHostFragment.navController



        //активити/ фрагмент появлялся один раз при первом запуске приложения
        val sp = getSharedPreferences(
            MY_SETTINGS,
            Context.MODE_PRIVATE
        )
        // проверяем, первый ли раз открывается программа
        // проверяем, первый ли раз открывается программа
//        val hasVisited = sp.getBoolean("hasVisited", false)
//
//        if (!hasVisited) {
//           // navController.navigate(R.id.helloFragment)
//            // выводим нужную активность
//           // navController.navigate(R.id.helloFragment)
//            val intent = Intent(this,HelloActivity::class.java)
//            startActivity(intent)
//
//            val e: SharedPreferences.Editor = sp.edit()
//            e.putBoolean("hasVisited", true)
//            e.commit() // не забудьте подтвердить изменения
//        }
        val view = binding.getRoot()
        setContentView(view)

        Log.i(TAG, "onCreate MainActivity")


        val bottomNavigationView = binding.btnNavigationList
        //ответственный чтобы заменять фрагменты в navhostfragment

            // val navController = findNavController(R.id.fragmentContainer)
//Метод setupWithNavController вешает листенер на BottomNavigationView и
// выполняет навигацию при нажатии на его элементы.
        bottomNavigationView.setupWithNavController(navController)


//actionbar менялся
       setupActionBarWithNavController(findNavController(R.id.fragmentContainer))


            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.listFragment -> binding.btnNavigationList.visibility = View.VISIBLE
                    R.id.alarmFragment -> binding.btnNavigationList.visibility = View.VISIBLE
                    R.id.medicalFragment -> binding.btnNavigationList.visibility = View.VISIBLE
                    R.id.helloFragment-> binding.btnNavigationList.visibility = View.GONE


            }
        }


//        //обрабатываем intent, если MainActivity открывается при нажатии на уведомление, то
//        // переходит на AddFragment, если нет - то открывается MainActivity, соответственно проверяется через ntent,
//        //если в нем что то есть или нет
//
//        Log.i("myLogs", " val action = NavGraphDirections.actionGlobalAlarmShowFragment(dateTime)")
//        val dateTime = intent.getStringExtra("dateTime")
//        if (dateTime!=null) {
//            val action = NavGraphDirections.actionGlobalAlarmShowFragment(dateTime)
//            navController.navigate(action)
//        }
    }



//This method is called whenever the user chooses to navigate Up
// within your application's activity hierarchy from the action bar.
    ///Этот метод вызывается всякий раз, когда пользователь решает перейти Вверх
    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.fragmentContainer)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}