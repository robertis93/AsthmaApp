package com.example.asthmaapp.view.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import com.example.asthmaapp.R
import com.example.asthmaapp.databinding.ActivityHelloBinding

class HelloActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHelloBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide();

        binding = ActivityHelloBinding.inflate(getLayoutInflater())
        setContentView(binding.getRoot())

//        binding.helloBtn.setOnClickListener {
//
//            val intent = Intent(this, MainActivity::class.java)
//            startActivity(intent)
//        }
    }
}