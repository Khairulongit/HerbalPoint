package com.tchnodynamic.herbalpoint

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tchnodynamic.herbalpoint.Adapters.AppointmentAdapter
import com.tchnodynamic.herbalpoint.MedicinePurchase.ProductDetailsActivity
import com.tchnodynamic.herbalpoint.Models.AppointmentModel
import com.tchnodynamic.herbalpoint.Prevalent.Prevalent
import com.tchnodynamic.herbalpoint.databinding.ActivityAllAppointmentBinding
import com.tchnodynamic.herbalpoint.databinding.ActivityMyOrdersBinding

class AllAppointmentActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAllAppointmentBinding

    private var myAppointsmentsAdapter: AppointmentAdapter? = null
    private var appolist: MutableList<AppointmentModel>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllAppointmentBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val textView = findViewById<TextView>(R.id.toolbartext)

        textView.text = "Your Current Appointment!!"
        textView.gravity = Gravity.CENTER_HORIZONTAL



        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
//        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {

                binding.appointmentsRecycleView.visibility = View.VISIBLE
                binding.nointenet.visibility = View.GONE
            } else {
                binding.appointmentsRecycleView.visibility = View.GONE
                binding.nointenet.visibility = View.VISIBLE

            }

        })


        //Toolbar Logic Above


        binding.liner1.visibility = View.GONE
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout = true  // new product at top
        linearLayoutManager.stackFromEnd = true  // new product at top


        var recyclerView = findViewById<RecyclerView>(R.id.appointmentsRecycleView)

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true);


        appolist = ArrayList()
        myAppointsmentsAdapter =
            this?.let { AppointmentAdapter(it, appolist as ArrayList<AppointmentModel>) }

        recyclerView!!.adapter = myAppointsmentsAdapter


        if (Prevalent.UserType=="Seller")
        {
            getmyorderssller()
        }else
          getmyordersbuyer()


    }

    private fun getmyorderssller() {

        val allappointmenntsseller = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Booked Appointment")

        allappointmenntsseller.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                appolist?.clear()

                for (orderValue in snapshot.children) {

//                    val order = orderValue.getValue(AppointmentModel::class.java)
//                    order?.orderid = orderValue.key.toString()

                    val Orderbyseller = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Booked Appointment").child(orderValue.key.toString())

                    Orderbyseller.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {

                            for (ordervaluefinal in snapshot.children) {

                                val product = ordervaluefinal.getValue(AppointmentModel::class.java)
                                product?.appointmentid = ordervaluefinal.child("appointmentid").value.toString()



                                if (product != null ) {
                                    appolist?.add(product)
                                }
                                myAppointsmentsAdapter!!.notifyDataSetChanged()

                            }

                        }

                        override fun onCancelled(error: DatabaseError) {
                        }


                    })
                }



            }



            override fun onCancelled(error: DatabaseError) {
            }
        })




    }

    private fun getmyordersbuyer() {

        val allappointmennts = FirebaseDatabase.getInstance().reference.child("Herbal Point")
            .child("Booked Appointment").child(
            FirebaseAuth.getInstance().currentUser!!.uid
        )


//        orderList?.clear()

        allappointmennts.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                appolist?.clear()

                for (orders in snapshot.children) {


                    val apointment = orders.getValue(AppointmentModel::class.java)


                    if (apointment != null) {
                        appolist!!.add(apointment)
                    }
                    myAppointsmentsAdapter!!.notifyDataSetChanged()
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }


        })

    }


    override fun onBackPressed() {

        val intent = Intent(this, ProductDetailsActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
