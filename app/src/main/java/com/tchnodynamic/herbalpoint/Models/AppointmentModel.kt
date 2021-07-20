package com.tchnodynamic.herbalpoint.Models

class AppointmentModel {

    var dispuserid : String =""
    var dispname : String =""
    var dispfees: String =""
    var dispdoctor: String =""
    var bookingtime: String =""
    var patientname: String =""
    var patinetaddressring :String =""
    var patinetcontact : String=""
    var patinetage :String=""
    var patientdisease:String=""
    var appointmentid:String=""

    constructor()


    constructor(

        dispuserid: String,
        dispname: String,
        dispfees: String,
        dispdoctor: String,
        bookingtime: String,
        patientname: String,
        patinetaddressring: String,
        patinetcontact: String,
        patinetage: String,
        patientdisease: String,
        appointmentid: String
    ) {
        this.dispuserid=dispuserid
        this.dispname = dispname
        this.dispfees = dispfees
        this.dispdoctor = dispdoctor
        this.bookingtime = bookingtime
        this.patientname = patientname
        this.patinetaddressring = patinetaddressring
        this.patinetcontact = patinetcontact
        this.patinetage = patinetage
        this.patientdisease = patientdisease
        this.appointmentid = appointmentid
    }
}