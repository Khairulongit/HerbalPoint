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
import com.tchnodynamic.herbalpoint.Models.OrderModel
import com.tchnodynamic.herbalpoint.NetworkConnection
import com.tchnodynamic.herbalpoint.R
import com.tchnodynamic.herbalpoint.databinding.ActivityConfirmFinalOrdersBinding

class ConfirmFinalOrdersActivity : AppCompatActivity() {
    private lateinit var binding: ActivityConfirmFinalOrdersBinding

    var orderid:String=""
    var buyerid:String=""
    var ostatus:String=""

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConfirmFinalOrdersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected->
            if (isConnected){
                binding.interneton.visibility= View.VISIBLE
                binding.nointenetlayout.visibility= View.GONE

            }else{
                binding.interneton.visibility= View.GONE
                binding.nointenetlayout.visibility= View.VISIBLE
            }

        })


        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        var textView = findViewById<TextView>(R.id.toolbartext)

        textView.text="Order Info"
        textView.foregroundGravity= Gravity.CENTER_HORIZONTAL
        textView.textSize= 20F

        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        //Toolbar Logic Above

        binding.adminFinalProductDate.isEnabled=false
        binding.adminFinalProductTime.isEnabled=false
        binding.adminFinalProductDetailsName.isEnabled=false
        binding.adminFinalProductQuantity.isEnabled=false
        binding.adminFinalProductPrice.isEnabled=false
        binding.adminFinalProductAddress.isEnabled=false



        orderid= intent.getStringExtra("oid").toString()
        buyerid= intent.getStringExtra("bid").toString()
        ostatus= intent.getStringExtra("ostatus").toString()


        getproductdetails(orderid)



        when(ostatus){

            "Pending Approval"-> {
                binding.adminDeliveryConfirm.visibility= View.INVISIBLE}
            "Approved"-> {
                binding.adminFinalNotconfirm.visibility= View.INVISIBLE
                binding.adminFinalConfirm.visibility= View.INVISIBLE}
            "Cancelled"-> {
                binding.adminDeliveryConfirm.visibility= View.INVISIBLE
                binding.adminFinalNotconfirm.visibility= View.INVISIBLE
                binding.adminFinalConfirm.visibility= View.INVISIBLE
            }

            else ->{
                binding.adminFinalNotconfirm.visibility= View.INVISIBLE
                binding.adminFinalConfirm.visibility= View.INVISIBLE
                binding.adminDeliveryConfirm.visibility= View.INVISIBLE

            }
        }


        binding.adminDeliveryConfirm.setOnClickListener {

            val Productref = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Placed Orders").child(buyerid)
            Productref.child(orderid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){

                        val product: OrderModel? = snapshot.getValue(OrderModel::class.java)
                        Productref.child(product!!.orderid).child("state").setValue("Delivered")
                        val intent = Intent(applicationContext, AdminActivity::class.java)
                        startActivity(intent)
                        finish()

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


        }

        binding.adminFinalConfirm.setOnClickListener {

            val Productref = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Placed Orders").child(buyerid)
            Productref.child(orderid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){

                        val product: OrderModel? = snapshot.getValue(OrderModel::class.java)
                        Productref.child(product!!.orderid).child("state").setValue("Approved")
                        val intent = Intent(applicationContext, AdminActivity::class.java)
                        startActivity(intent)
                        finish()

                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })


        }



        binding.adminFinalNotconfirm.setOnClickListener {
            val Productref = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Placed Orders").child(buyerid)
            Productref.child(orderid).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()){

                        val product: OrderModel? = snapshot.getValue(OrderModel::class.java)
                        Productref.child(product!!.orderid).child("state").setValue("Cancelled")
                        val intent = Intent(applicationContext, AdminActivity::class.java)
                        startActivity(intent)
                        finish()


                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        }

    }


    private fun getproductdetails(orderid: String) {

        val Productref = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Placed Orders").child(buyerid)
        Productref.child(orderid).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){

                    val product: OrderModel? = snapshot.getValue(OrderModel::class.java)



                    Picasso.get().load(product?.productimg).placeholder(R.drawable.profile).into(binding.adminFinalProductImageDeytails)
                    binding.adminFinalProductDate.setText(product?.date)
                    binding.adminFinalProductTime.setText(product?.time)
                    binding.adminFinalProductDetailsName.setText(product?.productname)
                    binding.adminFinalProductQuantity.setText(product?.quantity)
                    binding.adminFinalProductPrice.setText(product?.totalamount)
                    binding.adminFinalProductAddress.setText(product?.address)

                }


            }

            override fun onCancelled(error: DatabaseError) {
            }

        })


    }



}