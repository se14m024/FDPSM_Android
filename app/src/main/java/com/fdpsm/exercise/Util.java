package com.fdpsm.exercise;

/**
 * Created by SplinterCell-TP on 30.09.2015.
 */
public class Util {

    public static String createRequestURL(String search, String apiURL) {
        String temp = apiURL;
        // replace whitespaces with '+'
        search = search.replace(" ", "+");
        temp = temp.replace("*movie*", search);
        return temp;
    }
}
