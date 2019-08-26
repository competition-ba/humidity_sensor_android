package com.competitionba.humiditysensor;

import java.util.Date;

public class Sensor {
    private String wifiname;
    private String wifipsw;
    private Date mDate;
    private String sensorGUID;

    public String getWifiname() {
        return wifiname;
    }

    public void setWifiname(String wifiname) {
        this.wifiname = wifiname;
    }

    public String getWifipsw() {
        return wifipsw;
    }

    public void setWifipsw(String wifipsw) {
        this.wifipsw = wifipsw;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getSensorGUID() {
        return sensorGUID;
    }

    public void setSensorGUID(String sensorGUID) {
        this.sensorGUID = sensorGUID;
    }
}
