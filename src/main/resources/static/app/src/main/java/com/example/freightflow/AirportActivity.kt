package com.example.freightflow


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AirportActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_airport)

        // Get reference to the "Start to Airport" button
        val startToAirportButton: Button = findViewById(R.id.startToAirportButton)

        // Set an OnClickListener to navigate to TerminalSelectionActivity
        startToAirportButton.setOnClickListener {
            // Navigate to Terminal Selection activity
            val intent = Intent(this, TerminalSelectionActivity::class.java)
            startActivity(intent)
        }
    }
}