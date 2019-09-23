package com.competitionba.humiditysensor;

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


    public void setSensorGUID(String sensorGUID) {
        this.sensorGUID = sensorGUID;
    }

    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }
}
