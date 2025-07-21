package com.example.helloworld;

import android.app.Application;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {
    public static Map<String, String> userCredentials = new HashMap<>();
    @Override
    public void onCreate() {
        super.onCreate();
        //HashMap <String, String> es como un JSON o también es como un diccionario
        userCredentials.put("admin", "adm1n");
        userCredentials.put("cris.valench", "12345");

    }
}
