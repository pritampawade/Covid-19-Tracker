package com.swarajyadev.covid_19coronavirusstatistics

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.telephony.TelephonyManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.Display
import com.github.javiersantos.appupdater.enums.UpdateFrom
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mxn.soul.flowingdrawer_core.ElasticDrawer
import com.mxn.soul.flowingdrawer_core.ElasticDrawer.OnDrawerStateChangeListener
import com.razerdp.widget.animatedpieview.AnimatedPieView
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig
import com.razerdp.widget.animatedpieview.data.IPieInfo
import com.razerdp.widget.animatedpieview.data.PieOption
import com.razerdp.widget.animatedpieview.data.SimplePieInfo
import com.squareup.picasso.Picasso
import com.swarajyadev.covid_19coronavirusstatistics.API.API_Utils
import com.swarajyadev.covid_19coronavirusstatistics.Data.Country
import com.swarajyadev.covid_19coronavirusstatistics.Data.OverView
import com.swarajyadev.covid_19coronavirusstatistics.Fragments.Fragment_Compare
import com.swarajyadev.covid_19coronavirusstatistics.Fragments.Fragment_Disclaimer
import com.swarajyadev.covid_19coronavirusstatistics.Fragments.Fragment_InDash
import com.swarajyadev.covid_19coronavirusstatistics.Fragments.Fragment_affectedCities
import com.swarajyadev.covid_19coronavirusstatistics.HomescreenFrags.Fragment_Countries
import com.swarajyadev.covid_19coronavirusstatistics.HomescreenFrags.Fragment_DashBoard
import com.swarajyadev.covid_19coronavirusstatistics.HomescreenFrags.Fragment_Extras
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.layout_menu.*
import kotlinx.android.synthetic.main.toolbar.*
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity(), IPieInfo {

    var optionsMenu: Menu? = null;
    var mPieOption = PieOption()

    private val fragmentManager = supportFragmentManager
    private val frag_dash = Fragment_DashBoard()
    private val frag_countries = Fragment_Countries()
    private val frag_extras = Fragment_Extras()
    private val frag_disclaimer = Fragment_Disclaimer()
    private val frag_acities = Fragment_affectedCities()
    private val frag_compare = Fragment_Compare()

    var tm: TelephonyManager? = null;
    var countryCodeValue: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)/*
        setDefaultNightMode(
                AppCompatDelegate.MODE_NIGHT_YES);*/

        tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        countryCodeValue = tm!!.networkCountryIso
        //Picasso.get().load("https://www.countryflags.io/${countryCodeValue.toString().toUpperCase()}/shiny/64.png").into(civ_userflag)


        updater();
        menuOperator()
        dashboard()
        var appUpdater = AppUpdater(this)
            .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
            .setUpdateFrom(UpdateFrom.AMAZON)

        appUpdater.start();

        bottomBar.onItemSelected = {
            when (it) {
                0 ->
                    dashboard()
                1 ->
                    countries()
                2 ->
                    extras()
            }
        }

        bottomBar.onItemReselected = {
            /*
                        Toast.makeText(this, "Item $it re-selected",Toast.LENGTH_LONG).show()*/
        }

        iv_icon.setOnClickListener {
            mDrawer.openMenu()
        }


        navigationdrawer()


    }

    private fun menuOperator() {
        tv_home.setOnClickListener {
            dashboard()

            selector(tv_home)
            //perform actions here
        }
        tv_acities.setOnClickListener {

            var snack = Snackbar.make(it, "Coming Soon", Snackbar.LENGTH_LONG)
            snack.view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            snack.show()
            mDrawer.closeMenu()
        }
        tv_country_comp.setOnClickListener {

            var snack = Snackbar.make(it, "Coming Soon", Snackbar.LENGTH_LONG)
            snack.view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            snack.show()
            mDrawer.closeMenu()
        }
        tv_graph.setOnClickListener {
            var snack = Snackbar.make(it, "Coming Soon", Snackbar.LENGTH_LONG)
            snack.view.setBackgroundColor(resources.getColor(R.color.colorPrimary))
            snack.show()
            mDrawer.closeMenu()

        }
        tv_updates.setOnClickListener {
            var appUpdater = AppUpdater(this)
                .setDisplay(Display.DIALOG)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .setUpdateFrom(UpdateFrom.AMAZON)
                .showAppUpdated(true)

            appUpdater.start();
            mDrawer.closeMenu()

        }
        tv_feedback.setOnClickListener {
            var appPackageName =
                getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appPackageName)
                    )
                );
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)
                    )
                )
            }

        }
    }

    private fun selector(tv: TextView) {
        tv_home.setBackgroundColor(0x00000000)
        tv_acities.setBackgroundColor(0x00000000)
        tv_country_comp.setBackgroundColor(0x00000000)
        tv_graph.setBackgroundColor(0x00000000)
        tv_updates.setBackgroundColor(0x00000000)
        tv_feedback.setBackgroundColor(0x00000000)

        mDrawer.closeMenu()
        tv.setBackgroundColor(resources.getColor(R.color.colorAccent))

    }

    private fun navigationdrawer() {
        mDrawer.setTouchMode(ElasticDrawer.TOUCH_MODE_BEZEL)
        mDrawer.setOnDrawerStateChangeListener(object : OnDrawerStateChangeListener {
            override fun onDrawerStateChange(oldState: Int, newState: Int) {
                if (newState == ElasticDrawer.STATE_CLOSED) {
                    //closed
                }
            }

            override fun onDrawerSlide(openRatio: Float, offsetPixels: Int) {
                //Log.i("MainActivity", "openRatio=$openRatio ,offsetPixels=$offsetPixels")
            }
        })
    }


    override fun getColor(): Int {


        return Color.BLUE
    }

    override fun getDesc(): String {
        return "Hello"
    }

    override fun getValue(): Double {
        return 0.5
    }

    override fun getPieOption(): PieOption? {
        return mPieOption
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        optionsMenu = menu
        return super.onCreateOptionsMenu(menu)
    }

    public fun getCountryCode(): String {
        return countryCodeValue!!
    }

    /* public fun dashboard_india(){

         bottomBar.setActiveItem(0)
         val fragmentTransaction = fragmentManager.beginTransaction()
         fragmentTransaction.replace(R.id.my_fragment, Fragment_InDash())
         fragmentTransaction.commit()
     }*/

    public fun affected_cities() {

        bottomBar.setActiveItem(0)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.my_fragment, frag_acities)
        fragmentTransaction.commit()
    }

    public fun compare_countries() {

        bottomBar.setActiveItem(0)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.my_fragment, frag_compare)
        fragmentTransaction.commit()
    }

    public fun dashboard() {

        bottomBar.setActiveItem(0)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.my_fragment, frag_dash)
        fragmentTransaction.commit()
    }

    public fun countries() {
        bottomBar.setActiveItem(1)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.my_fragment, frag_countries)
        try {
            frag_countries.countrieslist!!.clear()
        } catch (e: Exception) {
        }
        fragmentTransaction.commit()
    }

    public fun extras() {
        bottomBar.setActiveItem(2)
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.my_fragment, frag_extras)
        fragmentTransaction.commit()
    }

    override fun onBackPressed() {
        if (bottomBar.getActiveItem() != 0) {
            dashboard()
        } else {
            finish()
        }
    }

    private fun updater() {
        // Write a message to the database
        var database = FirebaseDatabase.getInstance();
        var myRef = database.getReference("Updater");

        val postListener = object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                //Do nothing
            }

            override fun onDataChange(p0: DataSnapshot) {
                val vcode = p0.child("vcode").getValue().toString()
                val link = p0.child("link").getValue().toString()
              //  Toast.makeText(applicationContext,p0.toString(),Toast.LENGTH_LONG).show()
                try {

                    var pInfo = getPackageManager ().getPackageInfo(
                        getPackageName(),
                        0
                    );
                    var version = pInfo . versionCode;
                    if(vcode.equals("null")){
                        try {
                            Toast.makeText(applicationContext,"This application is no longer " +
                                    "supported by the developer",Toast.LENGTH_LONG).show()
                        } catch (e:java.lang.Exception){}
                    }else
                    if(vcode.toInt()>version){
                        AlertDialog.Builder(this@MainActivity).setTitle("Update Available").
                            setMessage("Lateset Version of this application is available")
                            .setPositiveButton("Update",DialogInterface.OnClickListener
                            { dialog, which ->

                                var browserIntent =
                                    Intent(Intent.ACTION_VIEW, Uri.parse(link));
                                startActivity(browserIntent);
                            }).setNegativeButton("later",null).show()
                    }

                } catch (e: java.lang.Exception) {
                    //do nothing
                    try {
                        Toast.makeText(applicationContext,"Update check failed",Toast.LENGTH_LONG).show()
                    }catch (e:java.lang.Exception){

                    }
                }
            }


        }
        myRef.addValueEventListener(postListener)
    }
}
