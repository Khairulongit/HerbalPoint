package com.tchnodynamic.herbalpoint.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tchnodynamic.herbalpoint.DispesarDetailsActivit
import com.tchnodynamic.herbalpoint.Models.DispensaryModel
import com.tchnodynamic.herbalpoint.R

class DispensaryAdapters (private val mContext: Context,
                          private val mProduct:List<DispensaryModel>): RecyclerView.Adapter<DispensaryAdapters.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(mContext).inflate(R.layout.layout_dispensary, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



        val mainModel: DispensaryModel = mProduct[position]
        holder.dispname.text=mainModel.dispname
        holder.dispcontact.text=mainModel.dispcontact
        holder.dispfees.text=mainModel.dispfees
        holder.dispaddre.text=mainModel.dispadd
        holder.dispdoctor.text=mainModel.dispdoctr
        holder.disptiming.text=mainModel.disptimngs
        holder.dispimg.setImageResource(R.drawable.herbalone)


        holder.itemView.setOnClickListener {


            val intent = Intent(mContext, DispesarDetailsActivit::class.java)
            intent.putExtra("dname", mainModel.dispname)
            intent.putExtra("dcontact", mainModel.dispcontact)
            intent.putExtra("dfees",mainModel.dispfees)
            intent.putExtra("daddr",mainModel.dispadd)
            intent.putExtra("ddoctor",mainModel.dispdoctr)
            intent.putExtra("dtimings",mainModel.disptimngs)
            intent.putExtra("dimg",mainModel.disptimngs)
            mContext.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mProduct.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var  dispimg: ImageView = itemView.findViewById(R.id.dispimg)
        var  dispname: TextView = itemView.findViewById(R.id.dispname)
        var  dispcontact: TextView = itemView.findViewById(R.id.dispcontact)
        var  disptiming: TextView = itemView.findViewById(R.id.disptimings)
        var  dispfees: TextView = itemView.findViewById(R.id.dispfees)
        var  dispaddre: TextView = itemView.findViewById(R.id.dispaddress)
        var  dispdoctor: TextView = itemView.findViewById(R.id.dispdoctor)

    }


}