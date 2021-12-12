package com.swarajyadev.covid_19coronavirusstatistics.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.swarajyadev.covid_19coronavirusstatistics.Data.MoreInfo
import com.swarajyadev.covid_19coronavirusstatistics.R

class MoreInfoAdapter(
    context: Context,
    resource: Int,
    var list: List<MoreInfo>
) : ArrayAdapter<MoreInfo?>(context, resource, list) {
    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.double_layout, parent, false)
        var tv_key = convertView.findViewById<TextView>(R.id.tv_key)
        var tv_val = convertView.findViewById<TextView>(R.id.tv_value)

        tv_key.setText(list.get(position).key)
        tv_val.setText(list.get(position).value)
        return convertView
    }

}

