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
import com.tchnodynamic.herbalpoint.Prevalent.Prevalent
import com.tchnodynamic.herbalpoint.R
import com.tchnodynamic.herbalpoint.databinding.ActivityBookingConfirmationAdminBinding

class BookingConfirmationAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityBookingConfirmationAdminBinding

    var dispname: String = ""
    var patinetcontact: String = ""
    var patientname: String = ""
    var bookingtime: String = ""
    var dispfees: String = ""
    var patinetaddressring: String = ""
    var patientdisease: String = ""
    var appointmentid: String = ""
    var dispuserid: String = ""

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


        if (Prevalent.UserType != "Seller")
            binding.liner123.visibility = View.GONE


        binding.appoinmentConfirm.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Sellers").child(dispuserid).child(appointmentid).child("productstatus").setValue("Confirm")
        }

        binding.appoinmentNotConfirm.setOnClickListener {
            FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Sellers").child(dispuserid).child(appointmentid).child("productstatus").setValue("Cancel")
        }





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


        dispname = intent.getStringExtra("dispname").toString()
        dispuserid = intent.getStringExtra("dispuserid").toString()
        appointmentid = intent.getStringExtra("appointmentid").toString()
        patinetcontact = intent.getStringExtra("patinetcontact").toString()
        patientname = intent.getStringExtra("patientname").toString()
        bookingtime = intent.getStringExtra("bookingtime").toString()
        dispfees = intent.getStringExtra("dispfees").toString()
        patientdisease = intent.getStringExtra("patientdisease").toString()
        patinetaddressring = intent.getStringExtra("patinetaddressring").toString()
        patinetaddressring = intent.getStringExtra("patinetaddressring").toString()
        patinetaddressring = intent.getStringExtra("patinetaddressring").toString()

        binding.bookingTime.text = bookingtime
        binding.dispName.text = patientname
        binding.dispAddr.text = patinetaddressring
        binding.dispFees.text = dispfees
        binding.dispPatientName.text = patientname
        binding.dispPatientAddress.text = patinetaddressring
        binding.dispPatientContact.text = patinetcontact
        binding.dispPatientTypeofdisease.text = patientdisease


    }
}


