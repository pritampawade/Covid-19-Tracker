package com.swarajyadev.covid_19coronavirusstatistics.HomescreenFrags

import android.app.AlertDialog
import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.squareup.picasso.Picasso
import com.swarajyadev.covid_19coronavirusstatistics.Activities.YouTubePlayerActivity
import com.swarajyadev.covid_19coronavirusstatistics.R
import kotlinx.android.synthetic.main.fragment__extras.*


/**
 * A simple [Fragment] subclass.
 */
class Fragment_Extras : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment__extras, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        tv_who.setOnClickListener {
            browser("https://www.who.int/health-topics/coronavirus")
        }

        tv_who2.setOnClickListener {
            browser("https://www.who.int/emergencies/diseases/novel-coronavirus-2019/situation-reports")
        }
        tv_legal1.setOnClickListener {
            browser("https://github.com/ExpDev07/coronavirus-tracker-api")
        }
        tv_disclaimer.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Disclaimer")
                .setMessage("Disclaimer\n" +
                        "Information available on Pandemic COVID-19 is correct as per our knowledge. But actual information may vary, please contact your government or responsible personnels for latest and most accurate information regarding COVID-19 (a.k.a. Corona Virus).\n" +
                        "\n" +
                        "This application is developed for INFORMATION purpose ONLY, and we are not responsible for any loss or harm caused due the information available on this application. We DO NOT own any part of information available on Pandemic COVID-19 application.\n" +
                        "\n" +
                        "Our main intention behind sharing this application is to educate people and to make people aware of their responsibilities as a good citizen.\n" +
                        "\n")
                .setPositiveButton("Close",null).show()
        }

        Picasso.get().load("https://img.youtube.com/vi/bPITHEiFWLc/2.jpg").into(iv_v1)
        Picasso.get().load("https://img.youtube.com/vi/6Ooz1GZsQ70/2.jpg").into(iv_v2)
        Picasso.get().load("https://img.youtube.com/vi/qF42gZVm1Bo/2.jpg").into(iv_v3)

        cv_v1.setOnClickListener {
            play("https://www.youtube.com/watch?v=bPITHEiFWLc")
        }
        cv_v2.setOnClickListener {
            play("https://www.youtube.com/watch?v=6Ooz1GZsQ70")
        }
        cv_v3.setOnClickListener {
            play("https://www.youtube.com/watch?v=qF42gZVm1Bo")
        }
    }

    private  fun play(url:String){
        var i = Intent(context,YouTubePlayerActivity::class.java)
        i.putExtra("video",url)
        startActivity(i)
    }
    fun browser(url:String){
        try {
            val i = Intent("android.intent.action.MAIN")
            i.component =
                ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main")
            i.addCategory("android.intent.category.LAUNCHER")
            i.data = Uri.parse(url)
            startActivity(i)
        } catch (e: ActivityNotFoundException) { // Chrome is not installed
            val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(i)
        }
    }

}
