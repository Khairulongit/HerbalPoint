package com.tchnodynamic.herbalpoint

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tchnodynamic.herbalpoint.databinding.ActivityDoctorsAppoitetBookigBinding

class DoctorsAppoitetBookig : AppCompatActivity() {

    private lateinit var binding: ActivityDoctorsAppoitetBookigBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDoctorsAppoitetBookigBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }
}