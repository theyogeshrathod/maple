package com.coolapps.yo.maple.util;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class Countries {

    public static List<String> getCountries() {
        Locale[] locales = Locale.getAvailableLocales();
        List<String> countries = new ArrayList<String>();
        for (Locale locale : locales) {
            String country = locale.getDisplayCountry();
            if (country.trim().length() > 0 && !countries.contains(country)) {
                countries.add(country);
            }
        }
        Collections.sort(countries);
        for (String country : countries) {
            Log.d("TAG", "getCountries: country " + country);
        }
        return countries;
    }
}