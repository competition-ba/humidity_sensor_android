package com.competitionba.humiditysensor;

import java.util.Date;
import java.util.UUID;

public class Sensor {
    private UUID mId;
    private String wifiname;
    private String wifipsw;
    private String sensorGUID;

    public Sensor(){
        mId = UUID.randomUUID();
    }
    public UUID getId() {
        return mId;
    }
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

    public String getSensorGUID() {
        return sensorGUID;
    }

    public void setSensorGUID(String sensorGUID) {
        this.sensorGUID = sensorGUID;
    }
}
