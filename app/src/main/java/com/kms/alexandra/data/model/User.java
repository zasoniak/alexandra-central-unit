package com.kms.alexandra.data.model;


import android.media.Image;

import java.util.Date;


/**
 * Created by Mateusz Zasoński on 2014-10-16.
 */
public class User {

    private String id;
    private String name;
    private String surname;
    private String password;
    private String login;
    private String email;
    private String phone;
    private String eyeColor;
    private int privileges;
    private Image photo;
    private Date birthday;

    public String getId() {
        return id;
    }
}
