package com.swarajyadev.covid_19coronavirusstatistics.HomescreenFrags

import android.R.attr.data
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.swarajyadev.covid_19coronavirusstatistics.API.API_Utils
import com.swarajyadev.covid_19coronavirusstatistics.Adapters.CountriesAdapter
import com.swarajyadev.covid_19coronavirusstatistics.Data.Country
import com.swarajyadev.covid_19coronavirusstatistics.Data.OverView
import com.swarajyadev.covid_19coronavirusstatistics.R
import kotlinx.android.synthetic.main.fragment__countries.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.Comparator
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 */
class Fragment_Countries : Fragment() {

    var cc:String = "N/A"
    var tmp:Country?=null

    var cncnt:Int = 0
    var dcount:Int = 0
    var rcount:Int = 0
    var ccount:Int = 0

    val url: String = API_Utils.LOCATIONS
    var queue:RequestQueue?=null
    var countrieslist:ArrayList<Country>?= ArrayList()
    var adapter:CountriesAdapter?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__countries, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        queue = Volley.newRequestQueue(context)

        adapter = CountriesAdapter(countrieslist,context)
        rv_clist.layoutManager = LinearLayoutManager(context)
        countrieslist!!.clear()
        rv_clist.adapter = adapter


        countrylist(url)

    }

    private fun countrylist(url:String) {
        val stringReq = StringRequest(
            Request.Method.GET, url,
            Response.Listener<String> { response ->

                if(response!=null) {

                    var jsonobj = JSONObject(response);
                    var jsonarray: JSONArray = jsonobj.getJSONArray("locations")
                    var latest = jsonobj.getJSONObject("latest")


                    var overalldata = OverView(
                        latest.getString("confirmed"),
                        latest.getString("deaths"),
                        latest.getString("recovered")
                    )



                    for(i in 0 until jsonarray.length()){
                        var country = jsonarray.getJSONObject(i)


                        var lat:String?=null
                        var lon:String?=null

                        var coordinates = country.getJSONObject("coordinates")
                        lat = coordinates.getString("latitude")
                        lon = coordinates.getString("longitude")


                        var country_name:String?=null
                        var country_code:String?=null

                        var id:Int?=null
                        var last_updated:String?=null
                        latest = country.getJSONObject("latest")
                        var countrydata = OverView(
                            latest.getString("confirmed"),
                            latest.getString("deaths"),
                            latest.getString("recovered")
                        )
                        var province:String?=null;

                        country_name = country.getString("country")
                        country_code = country.getString("country_code")

                        id = country.getInt("id")
                        last_updated = country.getString("last_updated")

                        province = country.getString("province")


                        var countryitem = Country(lat,lon,country_name!!,country_code!!,id!!,last_updated!!,countrydata,province!!,0)

                        if(cc.equals("N/A")){
                            cc=countryitem.country_code
                        }
                        if(cc.equals(countryitem.country_code)){
                            dcount+=countryitem.overView.deaths.toInt()
                            rcount+=countryitem.overView.recovered.toInt()
                            ccount+=countryitem.overView.confirmed.toInt()

                            countryitem.more = 1
                            tmp = countryitem
                            tmp!!.overView.deaths = dcount.toString()
                            tmp!!.overView.confirmed = ccount.toString()
                            tmp!!.overView.recovered = rcount.toString()

                        }else{
                            countrieslist!!.add(tmp!!)
                            dcount =0
                            rcount =0
                            ccount =0
                            cc = countryitem.country_code
                            tmp = countryitem
                            countrieslist!!.sortWith(Comparator { lhs, rhs ->
                                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                if (lhs.overView.confirmed.toInt() > rhs.overView.confirmed.toInt()) -1 else if (lhs.overView.confirmed.toInt() < rhs.overView.confirmed.toInt()) 1 else 0
                            })
                            adapter!!.notifyDataSetChanged()

                        }

                        try {
                            ll_loading.visibility =View.GONE
                        } catch (e: Exception) {
                        }
                        /*if(!clist.contains(countryitem.country_name)){
                            countrieslist!!.add(countryitem)

                            clist.add(countryitem.country_name)

                            countrieslist!!.sortWith(Comparator { lhs, rhs ->
                                // -1 - less than, 1 - greater than, 0 - equal, all inversed for descending
                                if (lhs.overView.confirmed.toInt() > rhs.overView.confirmed.toInt()) -1 else if (lhs.overView.confirmed.toInt() < rhs.overView.confirmed.toInt()) 1 else 0
                            })
                            adapter!!.notifyDataSetChanged()
                        }*/


                    }
                    countrieslist!!.add(tmp!!)
                    adapter!!.notifyDataSetChanged()




                }
            },
            Response.ErrorListener { println("Something went wrong")})
        queue!!.add(stringReq)
    }
}
