package com.swarajyadev.covid_19coronavirusstatistics.Adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.swarajyadev.covid_19coronavirusstatistics.Activities.CityList_Activity
import com.swarajyadev.covid_19coronavirusstatistics.Data.Country
import com.swarajyadev.covid_19coronavirusstatistics.Data.OverView
import com.swarajyadev.covid_19coronavirusstatistics.R
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment__dash_board.*
import org.json.JSONArray
import org.json.JSONObject


class AffectedCitiesAdapter:RecyclerView.Adapter<AffectedCitiesAdapter.ViewHolder> {

    var list:ArrayList<Country>?=null
    var context: Context?=null

    var queue: RequestQueue?=null
    constructor(list: ArrayList<Country>?, context: Context?) : super() {
        this.list = list
        this.context = context
        queue = Volley.newRequestQueue(context)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AffectedCitiesAdapter.ViewHolder {
        var layout = LayoutInflater.from(context).inflate(R.layout.countrylist,parent,false)
        return ViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    override fun onBindViewHolder(holder: AffectedCitiesAdapter.ViewHolder, position: Int) {
        var item = list!!.get(position)
        var flagid = context!!.getResources().getIdentifier(item.country_code.toLowerCase(),
            "drawable", context!!.getPackageName());
        holder.iv_cflag!!.setImageResource(flagid)
        holder.tv_deaths!!.setText("Deaths: "+item.overView.deaths)
        if(item.overView.recovered.toInt()>0){
            holder.tv_recovered!!.visibility = View.VISIBLE
            holder.tv_recovered!!.setText("Recovered: "+item.overView.recovered)
        }
        holder.tv_confirmed!!.setText("Confirmed: "+item.overView.confirmed)

        if(item.province.length<=2){
            holder.tv_cname!!.setText(item.country_name)
            holder.tv_cname!!.setTextColor(context!!.getColor(R.color.colorAccent))
        }else{
            holder.tv_cname!!.setText(item.province+"(${item.country_code})")
            holder.tv_cname!!.setTextColor(context!!.getColor(R.color.colorPrimary))
        }
        holder.tv_rank!!.setText("${position+1}")
        holder.cv_country!!.setOnClickListener {
            var i = Intent(context,CityList_Activity::class.java)
            i.putExtra("country",item.country_code)
            context!!.startActivity(i)

        }

        /*if(item.more==1)
            holder.iv_more!!.visibility = View.VISIBLE*/

    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv_cflag : CircleImageView?=null
        var tv_cname: TextView?=null
        var cv_country: CardView?=null

        var tv_confirmed:TextView?=null
        var tv_recovered:TextView?=null
        var tv_deaths:TextView?=null

        var tv_rank:TextView?=null
        var iv_more:ImageView?=null


        init {
            iv_cflag = itemView.findViewById(R.id.iv_flag)
            tv_cname = itemView.findViewById(R.id.tv_cname)
            cv_country = itemView.findViewById(R.id.cv_country)
            tv_confirmed = itemView.findViewById(R.id.tv_confirmed)
            tv_recovered = itemView.findViewById(R.id.tv_recovered)
            tv_deaths = itemView.findViewById(R.id.tv_deaths)
            tv_rank = itemView.findViewById(R.id.tv_rank)
            iv_more = itemView.findViewById(R.id.iv_more)
        }
    }


}