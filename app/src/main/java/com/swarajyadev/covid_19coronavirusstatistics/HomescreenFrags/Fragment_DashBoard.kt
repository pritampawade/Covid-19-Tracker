package com.swarajyadev.covid_19coronavirusstatistics.HomescreenFrags

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.razerdp.widget.animatedpieview.AnimatedPieView
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import com.swarajyadev.covid_19coronavirusstatistics.API.API_Utils
import com.swarajyadev.covid_19coronavirusstatistics.Adapters.MoreInfoAdapter
import com.swarajyadev.covid_19coronavirusstatistics.Data.Country
import com.swarajyadev.covid_19coronavirusstatistics.Data.MoreInfo
import com.swarajyadev.covid_19coronavirusstatistics.Data.OverView
import com.swarajyadev.covid_19coronavirusstatistics.MainActivity
import com.swarajyadev.covid_19coronavirusstatistics.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment__dash_board.*
import org.json.JSONArray
import org.json.JSONObject


/**
 * A simple [Fragment] subclass.
 */
class Fragment_DashBoard : Fragment() {
    val overviewurl: String = "${API_Utils.LATEST}"
    var data:OverView?=null
    var queue: RequestQueue? = null

    var countrycode:String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__dash_board, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tv_allcountries.setOnClickListener {
            activity!!.bottomBar.setActiveItem(1)
            (activity as MainActivity?)!!.countries()


        }


        countrycode = (activity as MainActivity?)!!.countryCodeValue
        queue = Volley.newRequestQueue(context)
        overview()

        var url =  "${API_Utils.COUNTRY}${countrycode!!.toUpperCase()}"
        countrylist(url,mycountrypie,tv_mcconfirmed,tv_mcdeaths,tv_mcrecovered)
    }


    private fun overview() {
        val stringReq = StringRequest(
            Request.Method.GET, overviewurl,
            Response.Listener<String> { response ->

                if(response!=null) {
                    var latest: JSONObject = JSONObject(response)
                    latest = latest.getJSONObject("latest")
                    data = OverView(
                        latest.getString("confirmed"),
                        latest.getString("deaths"),
                        latest.getString("recovered")
                    )

                    try {
                        cl_overall.setOnClickListener {
                            cityAlert(
                                data!!.confirmed.toInt(),
                                data!!.deaths.toInt(),
                                data!!.recovered.toInt(),
                                "Worldwide"
                            )

                        }
                    } catch (e: Exception) {
                    }

                    try {
                        pieChart(data!!.confirmed.toInt(),data!!.deaths.toInt(),data!!.recovered.toInt(),overallpie,tv_oconfirmed,tv_odeaths,tv_orecovered)
                        sk_overall.visibility = View.GONE
                    } catch (e: Exception) {
                        //do nothing
                    }


                }
            },
            Response.ErrorListener {
                println("Something went wrong")
            })
        queue!!.add(stringReq)
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

                        try {
                            cl_mycountry.setOnClickListener {

                                cityAlert(
                                    data!!.confirmed.toInt(),
                                    data!!.deaths.toInt(),
                                    data!!.recovered.toInt(),
                                    countryitem.country_name
                                )

                            }
                        } catch (e: java.lang.Exception) {
                            try {
                                Toast.makeText(context, "Try again", Toast.LENGTH_LONG).show()
                            } catch (e: Exception) {
                            } finally {
                            }
                        }

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

                        try {
                            pieChart(countryitem.overView.confirmed.toInt(),countryitem.overView.deaths.toInt(),countryitem.overView.recovered.toInt(),
                                animatedPieView,tv_confirmed,tv_deaths,tv_recovered)
                        } catch (e: Exception) {
                            //do nothing
                        }

                        //country flag here


                        try {
                            var flagid = getResources().getIdentifier(countrycode,
                                "drawable", context!!.getPackageName());
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

    private fun pieChart(conf:Int,death:Int, rec:Int, animatedPieView:AnimatedPieView,tv_confirmed:TextView, tv_deaths:TextView,tv_recovered:TextView){


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

    private fun cityAlert(confirmed:Int,deaths:Int,recovered:Int,name:String){
        var a:AlertDialog?=null
        var v = LayoutInflater.from(context).inflate(R.layout.alert_city,null)
        var tv_titlename = v.findViewById<TextView>(R.id.tv_name_title)
        var overallpie = v.findViewById<AnimatedPieView>(R.id.overallpie)
        var flag = v.findViewById<CircleImageView>(R.id.iv_flag)

        var flagid = getResources().getIdentifier(countrycode,
            "drawable", context!!.getPackageName());
        flag.setImageResource(flagid)
        if(name.equals("Worldwide"))
            flag.setImageResource(R.drawable.ic_world)
        tv_titlename.setText(name)

        var tv_confirmed =  v.findViewById<TextView>(R.id.tv_oconfirmed)
        var tv_deaths =     v.findViewById<TextView>(R.id.tv_odeaths)
        var tv_recovered =  v.findViewById<TextView>(R.id.tv_orecovered)
        var lv_details =  v.findViewById<ListView>(R.id.lv_moreinfo)
        var iv_close = v.findViewById<ImageView>(R.id.iv_close)

        iv_close.setOnClickListener {
            a!!.dismiss()
        }
        var list = ArrayList<MoreInfo>()
        var num = "%4f".format((deaths.toDouble()/confirmed.toDouble())*100.0)
        list.add(MoreInfo("Death Ratio","$num%"))
        var miadapter = MoreInfoAdapter(context!!,R.layout.double_layout,list)
        lv_details.adapter = miadapter

        var alert = AlertDialog.Builder(context!!).setView(v)

        pieChart(confirmed.toInt(),deaths.toInt(),recovered.toInt(),overallpie,tv_confirmed, tv_deaths, tv_recovered)

        a = alert.show()



    }
}
