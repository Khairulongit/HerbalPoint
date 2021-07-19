package com.tchnodynamic.herbalpoint.MedicinePurchase

import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.nex3z.notificationbadge.NotificationBadge
import com.tchnodynamic.herbalpoint.Adapters.DispensaryAdapters
import com.tchnodynamic.herbalpoint.Adapters.ProductAdapterNEW
import com.tchnodynamic.herbalpoint.Admin.AdminCategoryActivity
import com.tchnodynamic.herbalpoint.ChoiceActivity
import com.tchnodynamic.herbalpoint.Models.CartModel
import com.tchnodynamic.herbalpoint.Models.DispensaryModel
import com.tchnodynamic.herbalpoint.Models.ProductModel
import com.tchnodynamic.herbalpoint.Models.Users
import com.tchnodynamic.herbalpoint.NetworkConnection
import com.tchnodynamic.herbalpoint.Prevalent.Prevalent
import com.tchnodynamic.herbalpoint.R
import com.tchnodynamic.herbalpoint.databinding.ActivityAllDispensaryBinding
import com.tchnodynamic.herbalpoint.databinding.ActivityAllMedicineProductsBinding
import java.io.File

class AllMedicineProducts : AppCompatActivity() {
    private lateinit var binding: ActivityAllMedicineProductsBinding
    var toggle: ActionBarDrawerToggle? = null
    lateinit var hearderview: View
    private var Searchiinput: String = ""
    private var totalallcart: Int = 0
    var searchView: SearchView? = null


    //    private var productAdapter:ProductAdapter?=null
    private var productAdapter: ProductAdapterNEW? = null
    private var productList: MutableList<ProductModel>? = null
    var progressDialog: ProgressDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAllMedicineProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }


        val networkConnection = NetworkConnection(applicationContext)
        networkConnection.observe(this, Observer { isConnected ->
            if (isConnected) {

                binding.recycleView.visibility = View.VISIBLE
                binding.nointenet.visibility = View.GONE
            } else {
                binding.recycleView.visibility = View.GONE
                binding.nointenet.visibility = View.VISIBLE

            }

        })


        if (Prevalent.UserType != "Seller") {

            binding.addnew.visibility = View.INVISIBLE
        }

        if (Prevalent.UserType == "Seller") {
            hideItemseller()
            getproduct()

        } else {
            hideItem()
            getproductall()
        }


        binding.addnew.setOnClickListener {
            val intent = Intent(this, AdminCategoryActivity::class.java)
            startActivity(intent)

//            finish()
        }


        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout = true  // new product at top
        linearLayoutManager.stackFromEnd = true  // new product at top


        var recyclerView = findViewById<RecyclerView>(R.id.recycle_view)

        recyclerView!!.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true);


        productList = ArrayList()
        productAdapter = ProductAdapterNEW(applicationContext)
        productAdapter!!.setData(productList as ArrayList<ProductModel>)

        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView!!.adapter = productAdapter


    }

    private fun hideItem() {
    }

    private fun hideItemseller() {

    }



    private fun getcart(badge: NotificationBadge) {

        var CartListref = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Cart List").
        child("User View").child(FirebaseAuth.getInstance().currentUser!!.uid).child("In Cart Item")

        CartListref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (cartvalue in snapshot.children) {

                    val cart = cartvalue.getValue(CartModel::class.java)

                    if (cart != null) {

                        totalallcart += cart.quantity.toInt()

                    }
                }
                badge.setText(totalallcart.toString())
                Log.wtf("shahnaz", totalallcart.toString())

            }


            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


    private fun getproductall() {

        Prevalent.Activityname = "HomeActivityNew"
        val Sellerall = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Products")


        Sellerall.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                productList?.clear()


                for (userid in snapshot.children) {

                    val user = userid.getValue(Users::class.java)
                    user?.userid = userid.key.toString()


                    val Productall =
                        FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Products").child(
                            userid.key.toString()
                        )

                    Productall.addValueEventListener(object : ValueEventListener {

                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (productvalue in snapshot.children) {
//
                                val product = productvalue.getValue(ProductModel::class.java)
                                product?.productid = productvalue.key.toString()

                                if (product != null) {
                                    productList!!.add(product)
                                }
                                productAdapter!!.notifyDataSetChanged()
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        val item = menu.findItem(R.id.searchmenu)
        val updatecart = menu.findItem(R.id.updatecart)
        val view = menu.findItem(R.id.updatecart).actionView
        val badge = view.findViewById<View>(R.id.badge) as NotificationBadge
        getcart(badge)

//        badge.setText(totalcount.toString())

//        if(Prevalent.UserType=="Seller"){
//
//            item.isVisible=false
//            view.isVisible=false
//            updatecart.isVisible=false
//            badge.isVisible=false
//        }
//        updatecartcount()
        view.setOnClickListener {
            //                Toast.makeText(MainActivity.this, "Notthing", Toast.LENGTH_SHORT).show();
            startActivity(Intent(applicationContext, CartActivity::class.java))
            //                finish();
        }


        // SEARCHVIEW LOGIC IS BELOW
        searchView = item.actionView as SearchView
        searchView!!.queryHint = "Type Product Name"
        searchView!!.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                productAdapter?.filter?.filter(query)

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                productAdapter?.filter?.filter(newText)
                return true
            }
        })


//        SEARCHVIEW LOGIC IS ABOVE
        return super.onCreateOptionsMenu(menu)
    }



    private fun getproduct() {

        val onlyseller = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Products").child(
            FirebaseAuth.getInstance().currentUser!!.uid
        )



        productList?.clear()

        onlyseller.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {


                for (productvalue in snapshot.children) {
                    val product = productvalue.getValue(ProductModel::class.java)




                    if (product != null) {
                        productList!!.add(product)
                    }
                    productAdapter!!.notifyDataSetChanged()
                }
            }


            override fun onCancelled(error: DatabaseError) {
            }


        })


    }


    override fun onBackPressed() {

        val intent = Intent(this, ChoiceActivity::class.java)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK + Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        finish()
    }
}
