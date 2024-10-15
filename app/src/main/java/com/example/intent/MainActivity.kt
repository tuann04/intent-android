package com.example.intent

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.Manifest
import android.app.Activity
import android.app.SearchManager
import android.widget.Toast
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {
    private val REQUEST_CALL_PHONE_PERMISSION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val phoneNumberInput = findViewById<EditText>(R.id.phoneNumberInput)

        val dialButton = findViewById<Button>(R.id.dialButton)
        dialButton.setOnClickListener {
            val phoneNumber = phoneNumberInput.text.toString()
            if (phoneNumber.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$phoneNumber")
                }
                startActivity(intent)
            } else {
                phoneNumberInput.error = "Please enter a valid phone number"
            }
        }

        val callButton = findViewById<Button>(R.id.callButton)
        callButton.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.CALL_PHONE),
                    REQUEST_CALL_PHONE_PERMISSION
                )
            } else{
                makePhoneCall()
            }
        }


        val searchStringInput = findViewById<EditText>(R.id.searchStringInput)
        val searchButton = findViewById<Button>(R.id.searchButton)
        searchButton.setOnClickListener {
            val query = searchStringInput.text.toString()
            val intent = Intent(Intent.ACTION_WEB_SEARCH).apply {
                putExtra(SearchManager.QUERY, query)
            }
            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            }
        }

    }

    private fun makePhoneCall() {
        val phoneNumberInput = findViewById<EditText>(R.id.phoneNumberInput)
        val phoneNumber = phoneNumberInput.text.toString()
        if (phoneNumber.isNotEmpty()) {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:$phoneNumber")
            }
            startActivity(intent)
        } else {
            phoneNumberInput.error = "Please enter a valid phone number"
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CALL_PHONE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, place the call
                makePhoneCall()
            } else {
                // Permission denied, show a toast message
                Toast.makeText(this, "Permission to make calls was denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}