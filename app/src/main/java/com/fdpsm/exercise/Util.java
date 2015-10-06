package com.fdpsm.exercise;

public class Util {

    public static String createRequestURL(String search, String apiURL) {
        String temp = apiURL;
        // replace whitespaces with '+'
        search = search.replace(" ", "+");
        temp = temp.replace("*movie*", search);
        return temp;
    }
}
