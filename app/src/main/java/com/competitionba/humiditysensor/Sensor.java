package com.competitionba.humiditysensor;

import java.util.Date;
import java.util.UUID;

public class Sensor {
    private String sensorGUID;
    private double humidity;
    private String nickname;
    private String lastUpdateTime;

    public Sensor(String GUID,double humidity,String nickname,String lastUpdateTime){
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

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }
}
