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
import com.google.firebase.auth.FirebaseAuth
import com.tchnodynamic.herbalpoint.LoginActivity
import com.tchnodynamic.herbalpoint.MedicinePurchase.AllMedicineProducts
import com.tchnodynamic.herbalpoint.NetworkConnection
import com.tchnodynamic.herbalpoint.R
import com.tchnodynamic.herbalpoint.databinding.ActivityAdminCategoryBinding
import io.paperdb.Paper

class AdminCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminCategoryBinding

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAdminCategoryBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {
                binding.intermetoff.visibility = View.GONE
                binding.interneton.visibility = View.VISIBLE

            } else {
                binding.intermetoff.visibility = View.VISIBLE
                binding.interneton.visibility = View.GONE
            }

        })

        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var textView = findViewById<TextView>(R.id.toolbartext)

        textView.text = "Add New Product"
        textView.foregroundGravity = Gravity.CENTER_HORIZONTAL
        textView.textSize = 20F

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
//        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        //Toolbar Logic Above


        binding.medicine.setOnClickListener {
            val intent = Intent(applicationContext, AdminAddNewProductActivity::class.java)
            intent.putExtra("category", "tshirt")
            startActivity(intent)
        }




        binding.adminLogout.setOnClickListener {
            Paper.book().destroy()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK + Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()
        }

        binding.chkOrdersBtn.setOnClickListener {
            val intent = Intent(this, AdminActivity::class.java)
            startActivity(intent)
        }

        binding.modifyProductxBtn.setOnClickListener {
            val intent = Intent(this, AllMedicineProducts::class.java)
            startActivity(intent)
        }

    }

    override fun onBackPressed() {
        val intent = Intent(this, AllMedicineProducts::class.java)
        startActivity(intent)
    }
}
