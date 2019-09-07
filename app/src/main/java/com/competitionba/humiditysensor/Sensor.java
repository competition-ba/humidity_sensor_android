package com.competitionba.humiditysensor;

import java.util.Date;
import java.util.UUID;

public class Sensor {
    private String sensorGUID;
    private double humidity;
    private String nickname;
    private Date lastUpdateTime;

    public Sensor(String GUID,double humidity,String nickname,Date lastUpdateTime){
        super();
        this.sensorGUID = GUID;
        this.humidity = humidity;
        this.nickname = nickname;
        this.lastUpdateTime = lastUpdateTime;
    }
    public String getSensorGUID() {
        return sensorGUID;
    }

    public double getHumidity() {
        return humidity;
    }

    public String getNickname() {
        return nickname;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }


    public void setSensorGUID(String sensorGUID) {
        this.sensorGUID = sensorGUID;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
