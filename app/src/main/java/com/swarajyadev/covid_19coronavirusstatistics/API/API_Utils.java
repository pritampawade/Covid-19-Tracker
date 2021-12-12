package com.swarajyadev.covid_19coronavirusstatistics.API;

public class API_Utils {
    public static String GLOBAL_BASE_URL = "https://coronavirus-tracker-api.herokuapp.com/v2/";
    public static String CODE = "locations?country_code=";
    public static String COUNTRY = GLOBAL_BASE_URL+CODE;
    public static String LATEST = GLOBAL_BASE_URL+"latest";
    public static String INDIA_API = "https://api.covid19india.org/";
    public static String INDIA_STATE_DISTRICT = INDIA_API+"state_district_wise.json";
    public static String LOCATIONS = GLOBAL_BASE_URL+"locations";
    public static String youtube = "AIzaSyD1j3S3mAc-Kx6R3FySxnTb4YnDZ2jHVec";
}
