package com.swarajyadev.covid_19coronavirusstatistics.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.swarajyadev.covid_19coronavirusstatistics.API.API_Utils
import com.swarajyadev.covid_19coronavirusstatistics.Data.District
import com.swarajyadev.covid_19coronavirusstatistics.Data.State
import com.swarajyadev.covid_19coronavirusstatistics.R
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class Fragment_InDash : Fragment() {
    var statesdata:ArrayList<State> = ArrayList()
    var queue:RequestQueue?=null
    var states:MutableList<String>?= ArrayList()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? { // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__in_dash, container, false)
    }

    override fun onViewCreated(view: View,savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        queue = Volley.newRequestQueue(context)
        statewise()

    }
    fun statewise(){
        val url = API_Utils.INDIA_STATE_DISTRICT

        val stringReq = StringRequest(
            Request.Method.GET,url,Response.Listener {response ->

                var statelist = JSONObject(response)
                val iterator: Iterator<*> = statelist.keys()

                while (iterator.hasNext()) {
                    var count:Int = 0
                    val key = iterator.next() as String
                    states!!.add(key)

                    var district = statelist.getJSONObject(key).getJSONObject("districtData")
                    var distlist:ArrayList<District> = ArrayList()

                    val distiterator: Iterator<*> = district.keys()

                    while (distiterator.hasNext()){
                        var dkey = distiterator.next() as String
                        var dist = district.getJSONObject(dkey)
                        distlist.add(District(dkey,dist.getString("confirmed")))
                        count = count+ dist.getInt("confirmed")
                        //Toast.makeText(context,key+"=>"+dkey+"=>"+dist.getString("confirmed"),Toast.LENGTH_SHORT).show()

                    }
                    statesdata.add(State(distlist,key,count))
                  //  Toast.makeText(context,key+"=>$count",Toast.LENGTH_LONG).show()
                }
                layoutUpdater()


            },Response.ErrorListener {
                println(it.toString())
                Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show()
            }
        )

        queue!!.add(stringReq)
    }
    fun layoutUpdater(){
        var cnt:Int = 0
        for(i in 0..statesdata.size-1){
            cnt+=statesdata.get(i).count
        }
        Toast.makeText(context,"$cnt",Toast.LENGTH_LONG).show()
    }
}