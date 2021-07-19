package com.tchnodynamic.herbalpoint

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.tchnodynamic.herbalpoint.Adapters.DispensaryAdapters
import com.tchnodynamic.herbalpoint.Models.DispensaryModel
import com.tchnodynamic.herbalpoint.databinding.ActivityAllDispensaryBinding
import java.io.File

class AllDispensaryActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAllDispensaryBinding
    private var dispensarylist:MutableList<DispensaryModel>?=null
    private var allDispensaryAdapters: DispensaryAdapters?=null
    private var storeprodref: StorageReference? = null




    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAllDispensaryBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.statusBarColor = resources.getColor(R.color.status, this.theme)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(applicationContext, R.color.status)
        }




        storeprodref= FirebaseStorage.getInstance().reference.child("Herbal Point").child("Picture/tdvisit.jpg")
        val localfile: File = File.createTempFile("tdvist","jpg")


        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.reverseLayout=true  // new product at top
        linearLayoutManager.stackFromEnd=true  // new product at top


        var recyclerView=findViewById<RecyclerView>(R.id.dispRecycleView)

        recyclerView!!.layoutManager=linearLayoutManager
        recyclerView.setHasFixedSize(true);


        dispensarylist = ArrayList()
        allDispensaryAdapters=this?.let { DispensaryAdapters(it, dispensarylist as ArrayList<DispensaryModel>) }

        recyclerView!!.adapter =allDispensaryAdapters

        getmyallDispensary()




    }

    private fun getmyallDispensary() {

        val AllDispensary = FirebaseDatabase.getInstance().reference.child("Herbal Point").child("Dispensaries")


//        orderList?.clear()

        AllDispensary.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                dispensarylist?.clear()

                for (dispensaries in snapshot.children) {

                    Log.wtf("dispensaries","${dispensaries.value.toString()}")

                    val dispensary = dispensaries.getValue(DispensaryModel::class.java)


                    if (dispensary != null) {
                        dispensarylist!!.add(dispensary)
                    }
                    allDispensaryAdapters!!.notifyDataSetChanged()
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