package com.tchnodynamic.herbalpoint.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tchnodynamic.herbalpoint.Admin.BookingConfirmationAdmin
import com.tchnodynamic.herbalpoint.DispesarDetailsActivit
import com.tchnodynamic.herbalpoint.Models.AppointmentModel
import com.tchnodynamic.herbalpoint.Models.DispensaryModel
import com.tchnodynamic.herbalpoint.R

class AppointmentAdapter (private val mContext: Context,
                         private val mBookings:List<AppointmentModel>): RecyclerView.Adapter<AppointmentAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(mContext).inflate(R.layout.appointment_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {


        val mainModel: AppointmentModel = mBookings[position]
        holder.dispname.text = mainModel.dispname
        holder.disppatcontact.text = mainModel.patinetcontact
        holder.dispatname.text = mainModel.patientname
        holder.dispbooktime.text = mainModel.bookingtime
        holder.dispfees.text = mainModel.dispfees
        holder.disptypeofdisease.text=mainModel.patientdisease
        holder.dispaddr.text = mainModel.patinetaddressring
        holder.dispimg.setImageResource(R.drawable.herbalone)


        holder.itemView.setOnClickListener {


            val intent = Intent(mContext, BookingConfirmationAdmin::class.java)
            intent.putExtra("dispuserid", mainModel.dispuserid)
            intent.putExtra("appointmentid", mainModel.appointmentid)
            intent.putExtra("dispname", mainModel.dispname)
            intent.putExtra("patinetcontact", mainModel.patinetcontact)
            intent.putExtra("patientname", mainModel.patientname)
            intent.putExtra("bookingtime", mainModel.bookingtime)
            intent.putExtra("dispfees", mainModel.dispfees)
            intent.putExtra("patientdisease", mainModel.patientdisease)
            intent.putExtra("patinetaddressring", mainModel.patinetaddressring)
            mContext.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return mBookings.size
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var dispimg: ImageView = itemView.findViewById(R.id.appdispimg)
        var dispname: TextView = itemView.findViewById(R.id.appdispname)
        var disppatcontact: TextView = itemView.findViewById(R.id.apppatcontact)
        var dispbooktime: TextView = itemView.findViewById(R.id.appbooktime)
        var dispfees: TextView = itemView.findViewById(R.id.consultancy_fees)
        var disptypeofdisease: TextView = itemView.findViewById(R.id.typeofdisease)
        var dispaddr: TextView = itemView.findViewById(R.id.pataddress)
        var dispatname: TextView = itemView.findViewById(R.id.appopatname)

    }
}


