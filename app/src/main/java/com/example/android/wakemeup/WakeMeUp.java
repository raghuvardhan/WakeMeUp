package com.example.android.wakemeup;

import android.app.Application;

import com.facebook.stetho.Stetho;

public class WakeMeUp extends Application {
        public void onCreate() {
            super.onCreate();
            Stetho.initializeWithDefaults(this);
        }
}
