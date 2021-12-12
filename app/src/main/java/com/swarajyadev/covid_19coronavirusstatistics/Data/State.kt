package com.swarajyadev.covid_19coronavirusstatistics.Data

import java.util.*

class State(districts: ArrayList<District>, name: String, count:Int) {
    var districts = ArrayList<District>()
    var name: String
    var count: Int

    init {
        this.districts = districts
        this.name = name
        this.count = count
    }
}