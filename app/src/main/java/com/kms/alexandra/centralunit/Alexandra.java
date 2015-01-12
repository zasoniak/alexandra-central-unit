package com.kms.alexandra.centralunit;


import android.app.Application;
import android.content.Context;

import com.kms.alexandra.data.HomeManager;
import com.kms.alexandra.data.database.HomeRepository;
import com.kms.alexandra.data.model.Home;



/**
 * Created by Mateusz Zasoński on 2014-11-19.
 * AdminActivity - main activity for future administrator work
 */
public class Alexandra extends Application {

    private static Context context;
    private HomeManager homeManager;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        context = getApplicationContext();
        super.onCreate();
    }

    public Home getHome() {
        if(homeManager != null)
        {
            return this.homeManager.getHome();

        }
        else
        {
            return null;
        }
    }

    public HomeRepository getHomeRepository() {
        if(homeManager != null)
        {
            return this.homeManager.getHomeRepository();

    }
        else
            return null;
    }

    public HomeManager getHomeManager() {
        return this.homeManager;
    }

    public void setHomeManager(HomeManager manager) {
        this.homeManager = manager;
    }

}
