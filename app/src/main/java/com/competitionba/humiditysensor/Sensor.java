package com.competitionba.humiditysensor;

public class Sensor {
    private String sensorGUID;
    private double humidity;
    private String nickname;
    private String lastUpdateTime;
    private int NH3;
    private boolean mClear;

    public Sensor(String GUID,double humidity,String nickname,String lastUpdateTime,int NH3){
        super();
        this.sensorGUID = GUID;
        this.humidity = humidity;
        this.nickname = nickname;
        this.lastUpdateTime = lastUpdateTime;
        this.NH3 = NH3;
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

    public int getNH3() {
        return NH3;
    }

    public void setNH3(int NH3) {
        this.NH3 = NH3;
    }

    public boolean isClear() {
        return mClear;
    }

    public void setClear(boolean clear) {
        mClear = clear;
    }
}
