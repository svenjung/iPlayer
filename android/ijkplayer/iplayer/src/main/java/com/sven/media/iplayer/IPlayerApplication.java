package com.sven.media.iplayer;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Sven.J on 18-3-1.
 */

public class IPlayerApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // init timber
        Timber.plant(new Timber.DebugTree());
    }

}
