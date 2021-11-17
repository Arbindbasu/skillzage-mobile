package com.abt.skillzage;
import android.app.Application;
import org.json.JSONObject;

public class MyAppplication extends Application {

    JSONObject myprofileattribute = null;

    public JSONObject getMyprofileattribute() {
        return myprofileattribute;
    }

    public void setMyprofileattribute(JSONObject myprofileattribute) {
        this.myprofileattribute = myprofileattribute;
    }

    @Override
    public void onCreate() {
        super.onCreate();

    }
}
