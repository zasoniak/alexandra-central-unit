package com.kms.alexandracentralunit;


import android.content.UriMatcher;
import android.net.Uri;


/**
 * Created by Mateusz Zasoński on 2014-10-30.
 */
public final class ConfigurationContract {

    static final String PROVIDER_NAME = "com.kms.alexandracentralunit.configurationprovider";
    static final String URL = "content://"+PROVIDER_NAME+"/all";
    static final Uri URI = Uri.parse(URL);

    /**
     * list of ConfigurationDatabase tables
     */
    static final String SYSTEMS_TABLE = "systems";
    static final String USERS_TABLE = "users";
    static final String SYSTEM_USERS_TABLE = "system_users";
    static final String ROOMS_TABLE = "rooms";
    static final String GADGETS_TABLE = "gadgets";
    static final String SCENES_TABLE = "scenes";
    static final String TRIGGERS_TABLE = "scenes_triggers";
    static final String ACTIONS_TABLE = "action_scenes";
    static final String SUBSCENES_TABLE = "scenes_subscenes";

    /**
     * instance of UriMatcher with possible actions enumeration
     */

    static final int SYSTEMS = 0;
    static final int USERS = 1;
    static final int SYSTEM_USERS = 2;
    static final int ROOMS = 3;
    static final int GADGETS = 4;
    static final int SCENES = 5;
    static final int TRIGGERS = 6;
    static final int ACTIONS = 7;
    static final int SUBSCENES = 8;

    static final UriMatcher uriMatcher;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "systems", SYSTEMS);
        uriMatcher.addURI(PROVIDER_NAME, "users", USERS);
        uriMatcher.addURI(PROVIDER_NAME, "systems/users", SYSTEM_USERS);
        uriMatcher.addURI(PROVIDER_NAME, "rooms", ROOMS);
        uriMatcher.addURI(PROVIDER_NAME, "users", GADGETS);
        uriMatcher.addURI(PROVIDER_NAME, "users", SCENES);
        uriMatcher.addURI(PROVIDER_NAME, "users", TRIGGERS);
        uriMatcher.addURI(PROVIDER_NAME, "users", ACTIONS);
        uriMatcher.addURI(PROVIDER_NAME, "users", SUBSCENES);

    }
}
