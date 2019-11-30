package com.silambar.fragmentcommunicationwitheventbus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initPersons()
    }

    private fun initPersons() {
        supportFragmentManager.beginTransaction()
            .add(R.id.container_a, PersonA())
            .addToBackStack(null)
            .commit()

        supportFragmentManager.beginTransaction()
            .add(R.id.container_b, PersonB())
            .addToBackStack(null)
            .commit()
    }
}
