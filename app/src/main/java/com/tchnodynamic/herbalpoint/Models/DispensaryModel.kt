package com.tchnodynamic.herbalpoint.Models

class DispensaryModel {

    var dispname: String =""
    var dispadd: String =""
    var dispfees: String =""
    var disptimngs: String=""
    var dispcontact: String=""
    var dispdoctr: String=""

    constructor()


    constructor(
        dispname: String,
        dispadd: String,
        dispfees: String,
        disptimngs: String,
        dispcontact: String,
        dispdoctr: String
    ) {
        this.dispname = dispname
        this.dispadd = dispadd
        this.dispfees = dispfees
        this.disptimngs = disptimngs
        this.dispcontact = dispcontact
        this.dispdoctr = dispdoctr
    }
}