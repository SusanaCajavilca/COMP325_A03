package com.example.assignment03.utils;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;

// This code was provided by professor in class. No need to modify it (yet)
// (1) I coded this part first because it was just reviewing and copying
// there is no change here for Assignment 03

public class ApiClient {

    // Assignment 02 - COMP3025 - 02 - Friday 17:00 hrs
    // Susana Julia Cajavilca Turco - #200553998

    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");


    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
