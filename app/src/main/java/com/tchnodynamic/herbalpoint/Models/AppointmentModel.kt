package com.tchnodynamic.herbalpoint.Models

class AppointmentModel {

    var dispname : String =""
    var dispfees: String =""
    var dispdoctor: String =""
    var bookingtime: String =""
    var patientname: String =""
    var patinetaddressring :String =""
    var patinetcontact : String=""
    var patinetage :String=""
    var patientdisease:String=""

    constructor()


    constructor(
        dispname: String,
        dispfees: String,
        dispdoctor: String,
        bookingtime: String,
        patientname: String,
        patinetaddressring: String,
        patinetcontact: String,
        patinetage: String,
        patientdisease: String
    ) {
        this.dispname = dispname
        this.dispfees = dispfees
        this.dispdoctor = dispdoctor
        this.bookingtime = bookingtime
        this.patientname = patientname
        this.patinetaddressring = patinetaddressring
        this.patinetcontact = patinetcontact
        this.patinetage = patinetage
        this.patientdisease = patientdisease
    }
}