package com.tchnodynamic.herbalpoint.Admin

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.tchnodynamic.herbalpoint.NetworkConnection
import com.tchnodynamic.herbalpoint.R
import com.tchnodynamic.herbalpoint.databinding.ActivityBookingConfirmationAdminBinding

class BookingConfirmationAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityBookingConfirmationAdminBinding

    var dname: String = ""
    var dcontact: String = ""
    var dfees: String = ""
    var daddr: String = ""
    var ddoctor: String = ""
    var dtimings: String = ""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookingConfirmationAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                binding.interneton.visibility = View.VISIBLE
                binding.nointenet.visibility = View.GONE

            } else {
                binding.interneton.visibility = View.GONE
                binding.nointenet.visibility = View.VISIBLE
            }

        })


        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var textView = findViewById<TextView>(R.id.toolbartext)

        textView.text = "Order Info"
        textView.foregroundGravity = Gravity.CENTER_HORIZONTAL
        textView.textSize = 20F

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        dname = intent.getStringExtra("dname").toString()
        dcontact = intent.getStringExtra("dcontact").toString()
        dfees = intent.getStringExtra("dfees").toString()
        daddr = intent.getStringExtra("daddr").toString()
        ddoctor = intent.getStringExtra("ddoctor").toString()
        dtimings = intent.getStringExtra("dtimings").toString()

        binding.bookingTime.text = dtimings
        binding.dispName.text = dname
        binding.dispFees.text = dfees
        binding.dispPatientAddress.text = daddr



    }
}


