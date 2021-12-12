package com.swarajyadev.covid_19coronavirusstatistics.Data

class Country(

    var lat: String,
    var lon: String,
    var country_name: String,

    var country_code: String,
    var id: Int,
    var last_update: String,

    var overView: OverView,
    var province: String,
    var more: Int
)