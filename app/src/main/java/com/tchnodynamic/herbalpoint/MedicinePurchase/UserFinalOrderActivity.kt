package com.tchnodynamic.herbalpoint.MedicinePurchase

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tchnodynamic.herbalpoint.Models.OrderModel
import com.tchnodynamic.herbalpoint.NetworkConnection
import androidx.lifecycle.Observer
import com.tchnodynamic.herbalpoint.Prevalent.Prevalent
import com.tchnodynamic.herbalpoint.R
import com.tchnodynamic.herbalpoint.databinding.ActivityUserFinalOrderBinding
import java.text.SimpleDateFormat
import java.util.*

class UserFinalOrderActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserFinalOrderBinding

    var totalamomunt: String = ""
    var productname: String = ""
    var productimg: String = ""
    var address: String = ""
    var name: String = ""
    var pincode: String = ""
    var phone: String = ""
    var Orderplaced: Boolean = false
    private var Savecurrentdate: String = ""
    private var Savecurrenttime: String = ""

    val nameregex = "[a-zA-Z][a-zA-Z ]*".toRegex()
    val phoneregex = "^(?:(?:\\+|0{0,2})91(\\s*[\\-]\\s*)?|[0]?)?[6789]\\d{9}$".toRegex()
    val pincoderegex = "^[1-9]{1}[0-9]{2}\\s{0,1}[0-9]{3}$".toRegex()
    val addrregex = "^([a-zA-Z\u0080-\u024F]+(?:. |-| |'))*[a-zA-Z\u0080-\u024F]*$".toRegex()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFinalOrderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {

                binding.interneton.visibility = View.VISIBLE
                binding.nointenelayoutt.visibility = View.GONE
            } else {
                binding.interneton.visibility = View.GONE
                binding.nointenelayoutt.visibility = View.VISIBLE

            }

        })


        // Toolbar Logic below

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        val textView = findViewById<TextView>(R.id.toolbartext)


        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ab = supportActionBar
        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        //Toolbar Logic Above

        Prevalent.Activityname = "ConfirmFinalOrderActivity"


        if (intent.getStringExtra("solve") == "solve") {

            verifydetails()
        }





        binding.confirmConfrim.setOnClickListener {
            if (intent.getStringExtra("solve") == "solve") {
                CheckInfo()
                Orderplaced = true

            }
        }


    }

    private fun verifydetails() {

        FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Buyers")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {

                        binding.confirmPhone.setText(snapshot.child("buyercontact").value.toString())
                        binding.confirmName.setText(snapshot.child("buyername").value.toString())
                        binding.confirmAddress.setText(snapshot.child("buyeraddress").value.toString())
                        binding.confirmPincode.setText(snapshot.child("buyerpincode").value.toString())


                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }


            })


    }

    private fun CheckInfo() {
        phone = binding.confirmPhone.text.toString()
        name = binding.confirmName.text.toString()
        address = binding.confirmAddress.text.toString()
        pincode = binding.confirmPincode.text.toString()

        when {
            TextUtils.isEmpty(phone) -> Toast.makeText(this, "Phone is Empty", Toast.LENGTH_SHORT)
                .show()
            TextUtils.isEmpty(name) -> Toast.makeText(this, "Name is Empty", Toast.LENGTH_SHORT)
                .show()
            TextUtils.isEmpty(address) -> Toast.makeText(
                this,
                "Address is Empty",
                Toast.LENGTH_SHORT
            ).show()
            TextUtils.isEmpty(pincode) -> Toast.makeText(
                this,
                "Pincode is Empty",
                Toast.LENGTH_SHORT
            ).show()


            (!name.matches(nameregex)) -> {
                Toast.makeText(this, "WARNING: Enter a valid name !", Toast.LENGTH_SHORT).show()
            }

            (!phone.matches(phoneregex)) -> {
                Toast.makeText(this, "WARNING: Enter a valid Number !", Toast.LENGTH_SHORT).show()
            }

            (!address.matches(nameregex)) -> {
                Toast.makeText(this, "WARNING: Enter Valid Address!", Toast.LENGTH_SHORT).show()
            }

            (!pincode.matches(pincoderegex)) -> {
                Toast.makeText(
                    this,
                    "WARNING: Enter Valid Pin Code of Business!",
                    Toast.LENGTH_SHORT
                ).show()
            }


            else -> {
                Confirmorder()

                Toast.makeText(applicationContext, "Orders Placed Successfully", Toast.LENGTH_SHORT)
                    .show()
                val intent = Intent(applicationContext, MyOrdersActivity::class.java)
//                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK+Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("activity", "ConfirmFinalOrderActivity")
                startActivity(intent)
                finish()

            }
        }

    }

    private fun Confirmorder() {


        val Orderref = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Placed Orders")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)

        val cartref = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Cart List").child("User View")
            .child(FirebaseAuth.getInstance().currentUser!!.uid).child("In Cart Item")

        cartref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (cartvalue in snapshot.children) {

                    val orderid = cartvalue.key
                    var calendar: Calendar = Calendar.getInstance()
                    var currentdate = SimpleDateFormat("MMM dd,yyyy")
                    Savecurrentdate = currentdate.format(calendar.time)

                    var currenttime = SimpleDateFormat("HH:mm:ss a")
                    Savecurrenttime = currenttime.format(calendar.time)

                    val orderModel = OrderModel()
                    orderModel.orderid = orderid.toString()
                    orderModel.address = address
                    orderModel.customername = name
                    orderModel.pincode = pincode
                    orderModel.productname = cartvalue.child("productname").value.toString()
                    orderModel.productimg = cartvalue.child("productimg").value.toString()
                    orderModel.date = Savecurrentdate
                    orderModel.phone = cartvalue.child("useridphone").value.toString()
                    orderModel.state = "Pending Approval"
                    orderModel.deliveryphone = phone
                    orderModel.userid = FirebaseAuth.getInstance().currentUser!!.uid
                    orderModel.quantity = cartvalue.child("quantity").value.toString()
                    orderModel.time = Savecurrenttime
                    orderModel.sellerid = cartvalue.child("sellerid").value.toString()
                    orderModel.totalamount = cartvalue.child("price").value.toString()
                    if (Orderplaced) {
                        Log.wtf("Orderplaced", Orderplaced.toString())
                        FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Placed Orders").child(
                            FirebaseAuth.getInstance().currentUser!!.uid
                        ).child(orderid.toString()).setValue(orderModel)
                        cartref.child(orderid!!).removeValue()
//                                Orderplaced=false
                    }

                }

                Orderplaced = false

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })


//                   .child("In Cart Item")
//                            .addValueEventListener(object : ValueEventListener {
//                                override fun onDataChange(snapshot: DataSnapshot)
//                                {
//
//
//                                    for ()
//
//
//                                    Log.wtf("pm949",snapshot.childrenCount.toString())
//                                    Log.wtf("pm1008",Prevalent.Activityname)
//
//                                    for (productvalue in snapshot.children) {
//
//
//                                            Orderplaced = if (it.isSuccessful && Prevalent.Activityname=="ConfirmFinalOrderActivity") {
//                                                FirebaseDatabase.getInstance().reference.child("Cart List").child("User View").child(FirebaseAuth.getInstance().currentUser.uid).child("In Cart Item").child(productvalue.key.toString()).removeValue()
//                                                true
//                                            } else {
//                                                false
//                                            }
//
//                                        }
//
//                                    }
//
////                                    else{
//
////                                        Toast.makeText(applicationContext, "Something went wrong while placing order", Toast.LENGTH_SHORT).show()
////                                        val intent = Intent(applicationContext, HomeActivityNew::class.java)
////                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK+Intent.FLAG_ACTIVITY_NEW_TASK)
////                                        startActivity(intent)
////                                        finish()
//
//
////                                    }
//                                }
//
//                                override fun onCancelled(error: DatabaseError) {
//                                    TODO("Not yet implemented")
//                                }
//
//
//                            })


    }

    override fun onBackPressed() {

        val intent = Intent(applicationContext, ProductDetailsActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}