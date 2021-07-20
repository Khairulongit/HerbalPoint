package com.tchnodynamic.herbalpoint

import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tchnodynamic.herbalpoint.MedicinePurchase.MyOrdersActivity
import com.tchnodynamic.herbalpoint.Models.OrderModel
import com.tchnodynamic.herbalpoint.databinding.ActivityDispesarDetailsBinding
import java.text.SimpleDateFormat
import java.util.*

class DispesarDetailsActivit : AppCompatActivity(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityDispesarDetailsBinding
    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0

    var savedDay = 0
    var savedMonth = 0
    var savedYear = 0
    var savedHour = 0
    var savedMinute = 0


    var dname = ""
    var dcontact = ""
    var dfees = ""
    var daddr = ""
    var ddoctor = ""
    var dtimings = ""
    var dimg = ""

    var bookingtime = ""
    var patinetname = ""
    var patinetaddress = ""
    var patinetcontact = ""
    var patinetage = ""
    var patientdisease = ""

    val regexStrphn = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6789]\\d{9}$".toRegex()
    val regexfname = "[a-zA-Z][a-zA-Z ]*".toRegex()
    val regexnum = "\\d+".toRegex()
    val regexaddre = "\\d+\\s+([a-zA-Z]+|[a-zA-Z]+\\s[a-zA-Z]+)".toRegex()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDispesarDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.bookingTime.isEnabled=false


        dname = intent.getStringExtra("dname").toString()
        dcontact = intent.getStringExtra("dcontact").toString()
        dfees = intent.getStringExtra("dfees").toString()
        daddr = intent.getStringExtra("daddr").toString()
        ddoctor = intent.getStringExtra("ddoctor").toString()
        dtimings = intent.getStringExtra("dtimings").toString()


        binding.dispAddr.text = daddr
        binding.dispFees.text = dfees
//        binding.d.setText(daddr)
//        binding.dispAddr.setText(daddr)
//        binding.dispAddr.setText(daddr)
//        binding.dispAddr.setText(daddr)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        binding.bookingTime.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this, this, year, month, day).show()
        }


        binding.bookAppointment.setOnClickListener {
            verifydetails()
        }


//        binding.bookingDate.setOnClickListener {
//            pickdate()
//
//
//
//        }


    }

    private fun verifydetails() {

        bookingtime = binding.bookingTime.text.toString()
        patinetname = binding.dispPatientName.text.toString()
        patinetaddress = binding.dispPatientAddress.text.toString()
        patinetcontact = binding.dispPatientContact.text.toString()
        patinetage = binding.dispPatientAge.text.toString()
        patientdisease = binding.dispPatientTypeofdisease.text.toString()

        when {
            TextUtils.isEmpty(bookingtime) -> Toast.makeText(
                this,
                "Time is Empty",
                Toast.LENGTH_SHORT
            )
                .show()
//            TextUtils.isEmpty(patinetname) -> Toast.makeText(
//                this,
//                "Patient's Name is Empty",
//                Toast.LENGTH_SHORT
//            )
//                .show()
//            TextUtils.isEmpty(patinetaddress) -> Toast.makeText(
//                this,
//                "Patient's Address is Empty",
//                Toast.LENGTH_SHORT
//            ).show()
//            TextUtils.isEmpty(patinetcontact) -> Toast.makeText(
//                this,
//                "Your Contact is Empty",
//                Toast.LENGTH_SHORT
//            ).show()
//            TextUtils.isEmpty(patinetage) -> Toast.makeText(
//                this,
//                "Patient's Age is Empty",
//                Toast.LENGTH_SHORT
//            ).show()
//            TextUtils.isEmpty(patientdisease) -> Toast.makeText(
//                this,
//                "Type of Disease is Empty",
//                Toast.LENGTH_SHORT
//            ).show()
//
//
//            (!patinetname.matches(regexfname)) -> {
//                Toast.makeText(this, "WARNING: Enter a valid name !", Toast.LENGTH_SHORT).show()
//            }
//
//            (!patinetcontact.matches(regexStrphn)) -> {
//                Toast.makeText(this, "WARNING: Enter a valid Number !", Toast.LENGTH_SHORT).show()
//            }
//
////            (!patinetaddress.matches(regexaddre)) -> {
////                Toast.makeText(this, "WARNING: Enter Valid Address!", Toast.LENGTH_SHORT).show()
////            }
//
//            (!patinetage.matches(regexnum)) -> {
//                Toast.makeText(
//                    this,
//                    "WARNING: Enter a Valid Age !",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }
//            (!patientdisease.matches(regexaddre)) -> {
//                Toast.makeText(
//                    this,
//                    "WARNING: Enter a Valid Type of Disease !",
//                    Toast.LENGTH_SHORT
//                ).show()
//            }


            else -> {
                Confirmorder()

//                Toast.makeText(applicationContext, "Orders Placed Successfully", Toast.LENGTH_SHORT)
//                    .show()
//                val intent = Intent(applicationContext, MyOrdersActivity::class.java)
////                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK+Intent.FLAG_ACTIVITY_NEW_TASK)
//                intent.putExtra("activity", "ConfirmFinalOrderActivity")
//                startActivity(intent)
//                finish()

            }
        }

    }

    private fun Confirmorder() {


        val progressdialoglogin = ProgressDialog(this)

        progressdialoglogin.setTitle("Booking Appointment!")
        progressdialoglogin.setMessage("Please wait,while we are booking your Appointment..")
        progressdialoglogin.setCanceledOnTouchOutside(false)
        progressdialoglogin.show()


        val appobookedref = FirebaseDatabase.getInstance().reference.child("Herbal Point")
            .child("Booked Appointment")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        val appointmentid = appobookedref.push().key // will create random number

        val newpapponiment = HashMap<String, Any>()




        newpapponiment["dispname"] = dname
        newpapponiment["dispfees"] = dfees
        newpapponiment["dispdoctor"] = ddoctor
        newpapponiment["bookingtime"] = bookingtime
        newpapponiment["patientname"] = patinetname
        newpapponiment["patinetaddress"] = patinetaddress
        newpapponiment["patinetcontact"] = patinetcontact
        newpapponiment["patinetage"] = patinetage
        newpapponiment["patientdisease"] = patientdisease
        newpapponiment["appointmentid"] = appointmentid.toString()
        newpapponiment["productstatus"] = "pending"

        appobookedref.child(appointmentid!!).updateChildren(newpapponiment).addOnCompleteListener {

            if (it.isSuccessful) {
                progressdialoglogin.dismiss()
                val intent = Intent(applicationContext, AllAppointmentActivity::class.java)
                startActivity(intent)
                Toast.makeText(
                    applicationContext,
                    "Appointment Booked Successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } else {

                it.exception?.let {
                    throw it
                    progressdialoglogin.dismiss()
                }
            }
        }


    }

    private fun getDateTimeCalendar() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)

    }


    private fun pickdate() {

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        getDateTimeCalendar()

        TimePickerDialog(this, this, hour, minute, false).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        savedHour = hourOfDay
        savedMinute = minute


        binding.bookingTime.setText("$savedDay-$savedMonth-$savedYear  at $hour : $minute")
    }
}