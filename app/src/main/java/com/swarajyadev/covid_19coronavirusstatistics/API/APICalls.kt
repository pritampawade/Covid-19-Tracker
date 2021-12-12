package com.swarajyadev.covid_19coronavirusstatistics.API

import android.content.Context
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.swarajyadev.covid_19coronavirusstatistics.Activities.CityList_Activity
import com.swarajyadev.covid_19coronavirusstatistics.Data.OverView
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject

class APICalls {
    fun overview(url: String?,context: Context): OverView? {

        var data:OverView?=null

        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                var r = response
                System.out.println(r)
                if(response!=null) {
                    var latest: JSONObject = JSONObject(response)
                    latest = latest.getJSONObject("latest")
                    data = OverView(
                        latest.getString("confirmed"),
                        latest.getString("deaths"),
                        latest.getString("recovered")
                    )

                }
            },
            return data
        )
        return data
    }
}