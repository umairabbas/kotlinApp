package com.sample.test

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.sample.test.R
import com.sample.test.view.address.AddressFragment
import com.sample.test.view.trip.TripFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
//        if (savedInstanceState == null) {
//            supportFragmentManager.beginTransaction()
//                    .replace(R.id.container, TripFragment())//AddressFragment.newInstance())
//                    .commitNow()
//        }
        setupNavigation()
    }

    //Navigato
    private fun setupNavigation() {
        val navController = findNavController(R.id.navController)
        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.navController).navigateUp()

}
