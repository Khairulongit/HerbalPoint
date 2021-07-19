package com.tchnodynamic.herbalpoint

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.tchnodynamic.herbalpoint.databinding.ActivityDispesarDetailsBinding
import java.sql.Date
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class DispesarDetailsActivit : AppCompatActivity(),DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    private lateinit var binding: ActivityDispesarDetailsBinding
    var day= 0
    var month= 0
    var year= 0
    var hour=0
    var minute=0

    var savedDay= 0
    var savedMonth= 0
    var savedYear= 0
    var savedHour=0
    var savedMinute=0


   var dname  =""
   var dcontact =""
   var dfees =""
   var daddr  =""
   var ddoctor  =""
   var dtimings  =""
   var dimg=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDispesarDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.bookingTime.isEnabled=false


        dname  = intent.getStringExtra("dname").toString()
        dcontact= intent.getStringExtra("dcontact").toString()
        dfees =intent.getStringExtra("dfees").toString()
        daddr  = intent.getStringExtra("daddr").toString()
        ddoctor= intent.getStringExtra("ddoctor").toString()
        dtimings= intent.getStringExtra("dtimings").toString()


        binding.dispAddr.text = daddr
        binding.dispFees.text = dfees
//        binding.d.setText(daddr)
//        binding.dispAddr.setText(daddr)
//        binding.dispAddr.setText(daddr)
//        binding.dispAddr.setText(daddr)


        binding.bookingTime.setOnClickListener {
            getDateTimeCalendar()
            DatePickerDialog(this,this,year,month,day).show()
        }



//        binding.bookingDate.setOnClickListener {
//            pickdate()
//
//
//
//        }


    }

    private fun getDateTimeCalendar(){
        val cal:Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)

    }


    private fun pickdate(){

    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        savedDay=dayOfMonth
        savedMonth=month
        savedYear=year

        getDateTimeCalendar()

        TimePickerDialog(this,this,hour,minute,false).show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        savedHour=hourOfDay
        savedMinute=minute


        binding.bookingTime.setText("$savedDay-$savedMonth-$savedYear  at $hour : $minute")
    }
}