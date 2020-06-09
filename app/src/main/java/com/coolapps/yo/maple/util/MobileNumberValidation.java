package com.coolapps.yo.maple.util;

import java.util.regex.Pattern;

public class MobileNumberValidation {

    public static boolean isMobileValid(String number) {
        return Pattern.compile("^[6-9]{1}[0-9]{9}$").matcher(number).matches();
    }
}