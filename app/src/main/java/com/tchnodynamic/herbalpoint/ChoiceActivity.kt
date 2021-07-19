package com.tchnodynamic.herbalpoint

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tchnodynamic.herbalpoint.MedicinePurchase.AllMedicineProducts
import com.tchnodynamic.herbalpoint.databinding.ActivityAllDispensaryBinding
import com.tchnodynamic.herbalpoint.databinding.ActivityChoiceBinding

class ChoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.doctorappo.setOnClickListener {
            val intent = Intent(applicationContext, AllDispensaryActivity::class.java)
            startActivity(intent)
            finish()
        }


        binding.purchasemedi.setOnClickListener {
            val intent = Intent(applicationContext,AllMedicineProducts::class.java)
            startActivity(intent)
            finish()
        }
    }
}