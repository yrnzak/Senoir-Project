package com.org.jordanapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivityHome : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_home)

        // Get references to the buttons
        val shopButton: Button = findViewById(R.id.shop_button)
        val newArrivalsButton: Button = findViewById(R.id.new_arrivals_button)
        val offersButton: Button = findViewById(R.id.offers_button)

        // Set onClickListeners for the buttons
        shopButton.setOnClickListener {
            Toast.makeText(this, "Shop Now clicked", Toast.LENGTH_SHORT).show()
            // Add intent for navigating to Shop activity here
            startActivity(Intent(this, login_activity::class.java))
            finish()
        }

        newArrivalsButton.setOnClickListener {
            Toast.makeText(this, "New Arrivals clicked", Toast.LENGTH_SHORT).show()
            // Add intent for navigating to New Arrivals activity here
        }

        offersButton.setOnClickListener {
            Toast.makeText(this, "Offers clicked", Toast.LENGTH_SHORT).show()
            // Add intent for navigating to Offers activity here
        }
    }
}