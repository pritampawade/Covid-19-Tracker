package com.swarajyadev.covid_19coronavirusstatistics.Activities

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.internal.ContextUtils
import com.razerdp.widget.animatedpieview.AnimatedPieView
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import com.swarajyadev.covid_19coronavirusstatistics.API.API_Utils
import com.swarajyadev.covid_19coronavirusstatistics.Adapters.CityAdapter
import com.swarajyadev.covid_19coronavirusstatistics.Adapters.MoreInfoAdapter
import com.swarajyadev.covid_19coronavirusstatistics.Data.Country
import com.swarajyadev.covid_19coronavirusstatistics.Data.MoreInfo
import com.swarajyadev.covid_19coronavirusstatistics.Data.OverView
import com.swarajyadev.covid_19coronavirusstatistics.Data.Transporter
import com.swarajyadev.covid_19coronavirusstatistics.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.*

import kotlinx.android.synthetic.main.activity_city_list_.*
import kotlinx.android.synthetic.main.content_city_list_.*
import kotlinx.android.synthetic.main.countrylist.*
import kotlinx.android.synthetic.main.fragment__dash_board.*
import kotlinx.android.synthetic.main.fragment__dash_board.iv_mccflag
import kotlinx.android.synthetic.main.fragment__dash_board.mycountrypie
import kotlinx.android.synthetic.main.fragment__dash_board.sk_mc
import kotlinx.android.synthetic.main.fragment__dash_board.tv_mcconfirmed
import kotlinx.android.synthetic.main.fragment__dash_board.tv_mcdeaths
import kotlinx.android.synthetic.main.fragment__dash_board.tv_mcrecovered
import kotlinx.android.synthetic.main.fragment__dash_board.tv_mycountry
import kotlinx.android.synthetic.main.normal_toolbar.*
import org.json.JSONArray
import org.json.JSONObject
import org.w3c.dom.Text

class CityList_Activity : AppCompatActivity() {

    var a:AlertDialog?=null
    var queue:RequestQueue? = null
    var countrycode:String?=null
    var adapter:CityAdapter?=null
    var list:ArrayList<Country>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_list_)
        iv_back.setOnClickListener { finish() }

        queue = Volley.newRequestQueue(this)
        countrycode = intent.getStringExtra("country").toLowerCase()

        list = ArrayList()
        adapter = CityAdapter(this,R.layout.layout_cities, list!!)
        rv_cities.adapter = adapter

        rv_cities.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            var item:Country = parent.adapter.getItem(position) as Country
            if(item.province.length==0)
                countryAlert(item)
            else{

                cityAlert(item)
            }
        })

        countrylist(API_Utils.COUNTRY+"${countrycode!!.toUpperCase()}",mycountrypie,tv_mcconfirmed,tv_mcdeaths,tv_mcrecovered)


    }


    private  fun countryAlert(item:Country){
        var tmpitem = item
        tmpitem.province = item.country_name
        cityAlert(tmpitem)
    }
    private fun cityAlert(item:Country){
        Transporter.country = item
        var v = LayoutInflater.from(this).inflate(R.layout.alert_city,null)
        var tv_titlename = v.findViewById<TextView>(R.id.tv_name_title)
        var overallpie = v.findViewById<AnimatedPieView>(R.id.overallpie)
        var flag = v.findViewById<CircleImageView>(R.id.iv_flag)

        var flagid = getResources().getIdentifier(countrycode,
            "drawable", getPackageName());
        flag.setImageResource(flagid)
        tv_titlename.setText(item.province+"(${item.country_code})")

        var tv_confirmed =  v.findViewById<TextView>(R.id.tv_oconfirmed)
        var tv_deaths =     v.findViewById<TextView>(R.id.tv_odeaths)
        var tv_recovered =  v.findViewById<TextView>(R.id.tv_orecovered)
        var lv_details =  v.findViewById<ListView>(R.id.lv_moreinfo)
        var list = ArrayList<MoreInfo>()
        var num = "%4f".format((item.overView.deaths.toDouble()/item.overView.confirmed.toDouble())*100.0)
        list.add(MoreInfo("Death Ratio","$num%"))
        list.add(MoreInfo("Locate","locate ${item.province}"))
        var miadapter = MoreInfoAdapter(this,R.layout.double_layout,list)
        lv_details.adapter = miadapter
        var iv_close = v.findViewById<ImageView>(R.id.iv_close)

        lv_details.setOnItemClickListener(AdapterView.OnItemClickListener { parent, view, position, id ->
            if(position==1)
            startActivity(Intent(this,Locator_Activity::class.java))
        })

        iv_close.setOnClickListener {
            a!!.dismiss()

        }
        var alert = AlertDialog.Builder(this).setView(v)

        pieChart(item.overView.confirmed.toInt(),item.overView.deaths.toInt(),item.overView.recovered.toInt(),overallpie,tv_confirmed, tv_deaths, tv_recovered)


        if(a==null) {
            a = alert.show()
            a!!.setOnDismissListener {
                a=null
            }
        }





    }
    private fun countrylist(url:String, animatedPieView: AnimatedPieView, tv_confirmed: TextView, tv_deaths: TextView, tv_recovered: TextView) {
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

                    try {
                        pieChart(overalldata.confirmed.toInt(),overalldata.deaths.toInt(),overalldata.recovered.toInt(),
                            animatedPieView,tv_confirmed,tv_deaths,tv_recovered)
                    } catch (e: Exception) {
                        //do nothing
                    }

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
                        tv_mycountry.setText(country_name.toUpperCase())

                        list!!.add(countryitem)
                        adapter!!.notifyDataSetChanged()
                        //   Toast.makeText(this,lat+" "+lon+" "+id.toString(),Toast.LENGTH_LONG).show()
                        var cntrydata = "Lat: ${countryitem.lat}\n" +
                                "Lon: ${countryitem.lon}\n" +
                                "Name: ${countryitem.country_name}\n" +
                                "Code: ${countryitem.country_code}\n" +
                                "id: ${countryitem.id}\n" +
                                "lastupdate: ${countryitem.last_update}\n" +
                                "confirmed: ${countryitem.overView.confirmed}\n" +
                                "deaths: ${countryitem.overView.deaths}\n" +
                                "recovered: ${countryitem.overView.recovered}\n" +
                                "province: ${countryitem.province}\n\n"

                        //country flag here

                        try {
                            var flagid = getResources().getIdentifier(countrycode,
                                "drawable", getPackageName());
                            iv_mccflag.setImageResource(flagid)
                            sk_mc.visibility= View.GONE
                        } catch (e: Exception) {
                        }
                    }
                }
            },
            Response.ErrorListener { println("Something went wrong")})
        queue!!.add(stringReq)
    }

    private fun pieChart(conf:Int, death:Int, rec:Int, animatedPieView: AnimatedPieView, tv_confirmed: TextView, tv_deaths: TextView, tv_recovered: TextView){


        var config = AnimatedPieViewConfig()
        config.startAngle(-90f)
            .addData(SimplePieInfo(conf.toDouble() , resources.getColor(R.color.warn), "Confirmed $conf"))
            .addData(SimplePieInfo(death.toDouble(), resources.getColor(R.color.alert), "Death $death"))

        if(rec!=0){
            config.addData(SimplePieInfo(rec.toDouble()  , resources.getColor(R.color.ok), "Recovered $rec"))
            tv_recovered.visibility= View.VISIBLE
            iv_recovered.visibility = View.VISIBLE
        }

        try {
            config.textGravity(AnimatedPieViewConfig.ECTOPIC)
                .drawText(true)
                .duration(800)
                .pieRadius(75f)
                .setTextSize(25f)
                .strokeWidth = 35;// draw pie animation duration

            animatedPieView.applyConfig(config);
            animatedPieView.start();
            tv_confirmed.setText("$conf Confirmed")
            tv_deaths.setText("$death Deaths")
            tv_recovered.setText("$rec Recovered")
        }catch (e:Exception){
            //do nothing
        }
    }

}
