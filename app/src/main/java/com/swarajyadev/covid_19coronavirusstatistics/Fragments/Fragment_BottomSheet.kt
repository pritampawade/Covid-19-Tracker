package com.swarajyadev.covid_19coronavirusstatistics.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.swarajyadev.covid_19coronavirusstatistics.Adapters.MoreInfoAdapter
import com.swarajyadev.covid_19coronavirusstatistics.Data.Country
import com.swarajyadev.covid_19coronavirusstatistics.Data.MoreInfo
import com.swarajyadev.covid_19coronavirusstatistics.Data.Transporter

import com.swarajyadev.covid_19coronavirusstatistics.R
import kotlinx.android.synthetic.main.fragment__bottom_sheet.*

/**
 * A simple [Fragment] subclass.
 */
class Fragment_BottomSheet : Fragment() {

    var adapter:MoreInfoAdapter?=null
    var list:ArrayList<MoreInfo> = ArrayList<MoreInfo>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var item = Transporter.country

        list.add(MoreInfo("Name: ",item!!.province))
        list.add(MoreInfo("Country: ",item!!.country_name))

        list.add(MoreInfo("Country Code: ",item!!.country_code))
        list.add(MoreInfo("Confirmed Patients: ",item!!.overView.confirmed))

        list.add(MoreInfo("Total Deaths: ",item.overView.deaths))
        list.add(MoreInfo("Death Ratio: ", "${(item!!.overView.deaths.toDouble()/item!!.overView.confirmed.toDouble())*100}"))
        adapter = MoreInfoAdapter(context!!,R.layout.double_layout,list)
        lv_moreinfo.adapter = adapter
    }
}
