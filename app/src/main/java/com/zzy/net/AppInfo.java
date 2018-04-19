package com.zzy.net;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;

public class AppInfo {

    private int uid;
    private String appName;
    private String packName;
    private Drawable icon;
    private String version;
    private String letter;
    private String allLetter;
    public ArrayList<NetInfo> lsTcp;
    public ArrayList<NetInfo> lsUdp;
    public ArrayList<NetInfo> lsRaw;


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getAllLetter() {
        return allLetter;
    }

    public void setAllLetter(String allLetter) {
        this.allLetter = allLetter;
    }
}
