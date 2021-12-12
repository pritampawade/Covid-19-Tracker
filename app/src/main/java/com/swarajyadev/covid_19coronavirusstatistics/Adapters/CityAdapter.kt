package com.swarajyadev.covid_19coronavirusstatistics.Adapters

import android.app.Dialog
import android.content.Context
import android.view.*
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.internal.ContextUtils.getActivity
import com.swarajyadev.covid_19coronavirusstatistics.Data.Country
import com.swarajyadev.covid_19coronavirusstatistics.R
import kotlinx.android.synthetic.main.layout_cities.view.*
import java.util.*


class CityAdapter(
    context: Context,
    resource: Int,
    objects: List<Country?>
) : ArrayAdapter<Country?>(context, resource, objects) {
    var list: ArrayList<Country?>
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.layout_cities, parent, false)
        var item = list.get(position)
        var city_name = convertView!!.findViewById<TextView>(R.id.tv_city_name)

        var deaths = convertView!!.findViewById<TextView>(R.id.tv_deaths)
        var recovered = convertView!!.findViewById<TextView>(R.id.tv_recovered)
        var confirmed = convertView!!.findViewById<TextView>(R.id.tv_confirmed)



        if(item!!.overView.recovered.toInt()>0){
            recovered.visibility = View.VISIBLE
            recovered.setText("Recovered: "+item!!.overView.recovered)
        }
        deaths.setText("Deaths: "+item.overView.deaths)
        confirmed.setText("Confirmed: "+item.overView.confirmed)
        if(item!!.province.length>0) {
            city_name.setText("${item!!.province}(${item!!.country_code})")
        }
        else {
            city_name.setText("${item!!.country_name}(${item!!.country_code})")
        }
        return convertView!!
    }

    init {
        list = objects as ArrayList<Country?>
    }
}