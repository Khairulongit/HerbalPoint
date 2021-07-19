package com.tchnodynamic.herbalpoint.Admin

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.tchnodynamic.herbalpoint.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.tchnodynamic.herbalpoint.Models.ProductModel
import com.tchnodynamic.herbalpoint.NetworkConnection
import com.tchnodynamic.herbalpoint.databinding.ActivityModifyProductAdminBinding
import java.text.SimpleDateFormat
import java.util.*

class ModifyProductAdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityModifyProductAdminBinding
    var productId:String=""
    private var productref: DatabaseReference? = null
    private var Savecurrentdate: String = ""
    private var Savecurrenttime: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityModifyProductAdminBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected->
            if (isConnected){
                binding.internetoff.visibility= View.GONE
                binding.interneton.visibility= View.VISIBLE

            }else{
                binding.internetoff.visibility= View.VISIBLE
                binding.interneton.visibility= View.GONE
            }

        })

        // Toolbar Logic below

//        val toolbar = findViewById<Toolbar>(com.google.firebase.database.R.id.toolbar)
//        setSupportActionBar(toolbar)
//
//        val textView = findViewById<TextView>(com.google.firebase.database.R.id.toolbartext)

//        supportActionBar!!.setDisplayShowTitleEnabled(false)
//        val ab = supportActionBar
//        ab!!.setDisplayHomeAsUpEnabled(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        //Toolbar Logic Above

        productId= intent.getStringExtra("pid").toString()


        productref= FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Products").child(FirebaseAuth.getInstance().currentUser!!.uid).child(productId)

        displaytheproduct()

        binding.modifyProduct.setOnClickListener{
            applychanges()
        }

        binding.deleteProduct.setOnClickListener{
            deleteproduct()
        }


    }

    private fun deleteproduct() {

        productref?.removeValue()?.addOnCompleteListener {
            if (it.isSuccessful){

                val intent = Intent(this, AdminCategoryActivity::class.java)
                startActivity(intent)
                finish()
                Toast.makeText(applicationContext, "This product is deleted", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun applychanges() {

        val pname = binding.modifyProductDetailsName.text.toString()
        val pPrice = binding.modifyProductDetailsPrice.text.toString()
        val pdescriptiion = binding.modifyProductDetailsDesc.text.toString()

        when {
            TextUtils.isEmpty(pname) -> Toast.makeText(this, "Product Name is Empty", Toast.LENGTH_SHORT)
            TextUtils.isEmpty(pPrice) -> Toast.makeText(this, "Product Price is Empty", Toast.LENGTH_SHORT)
            TextUtils.isEmpty(pPrice) -> Toast.makeText(this, "Product Description is Empty", Toast.LENGTH_SHORT)

            else -> {

                var calendar : Calendar = Calendar.getInstance()

                var currentdate= SimpleDateFormat("MMM dd,yyyy")
                Savecurrentdate = currentdate.format(calendar.time)

                var currenttime= SimpleDateFormat("HH:mm:ss a")
                Savecurrenttime = currenttime.format(calendar.time)

                val updatepostmap = HashMap<String, Any>()
//                updatepostmap["postid"] = postId!!
                updatepostmap["date"] = Savecurrentdate
                updatepostmap["time"] = Savecurrenttime
                updatepostmap["description"] = pdescriptiion
//                updatepostmap["productimg"] = myUri
//                updatepostmap["category"] = categoryName
                updatepostmap["price"] = pPrice
                updatepostmap["productname"] = pname

                productref?.updateChildren(updatepostmap)?.addOnCompleteListener {task->

                    if(task.isSuccessful){
                        Toast.makeText(applicationContext, "Changes Applied Successfully KI", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, AdminCategoryActivity::class.java)
                        startActivity(intent)
                        finish()

                    }else {

                        task.exception?.let {
                            throw it

                        }
                    }



//                val intent = Intent(this, MainActivity::class.java)
//                startActivity(intent)
//                finish()
//                progressDialog.dismiss()


                }}


        }
    }



    private fun displaytheproduct() {

        val Productref= FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Products").child(
            FirebaseAuth.getInstance().currentUser!!.uid).child(productId)
        Productref?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                if (snapshot.exists()){
                    val prodduct = snapshot.getValue<ProductModel>(ProductModel::class.java)
                    Picasso.get().load(prodduct!!.productimg).fit().placeholder(R.drawable.ayurvedicbig).into(binding.modifyProductImageDeytails)
                    binding.modifyProductDetailsName.setText(prodduct!!.productname)
                    binding.modifyProductDetailsDesc.setText(prodduct.description)
                    binding.modifyProductDetailsPrice.setText(prodduct.price)

//                    Picasso.get().load(prodduct.productimg).into(binding.modifyProductImageDeytails)

                }
            }

            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
}