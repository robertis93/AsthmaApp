package com.example.asthmaapp.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.asthmaapp.R


class HelloFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        //Этот метод преобразует xml-файл с разметкой (дерево элементов) в объект класса View,
        // который можно явно преобразовать к корневому элементу xml-файла.
        return inflater.inflate(R.layout.fragment_hello, container, false)
    }
}