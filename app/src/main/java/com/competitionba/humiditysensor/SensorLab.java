package com.competitionba.humiditysensor;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SensorLab {
    private static SensorLab sSensorLab;
    private List<Sensor> mSensors;
    public static SensorLab get(Context context){
        if (sSensorLab == null){
            sSensorLab = new SensorLab(context);
        }
        return sSensorLab;
    }
    public void addSensor(Sensor s) {
        mSensors.add(s);
    }
    public List<Sensor> getSensors(){
        return mSensors;
    }
    private SensorLab(Context context){
        mSensors = new ArrayList<>();
        //---------------测试
        //for (int i = 0; i < 5; i++) {
        //    mSensors.add(new Sensor(UUID.randomUUID().toString(),
        //                            Math.random()*5,
        //                            String.format("传感器#%d",i),
         //                            new Date()));
        //}
        //---------------------
    }
    public Sensor getSensor(String GUID){
        for (Sensor sensor:mSensors){
            if (sensor.getSensorGUID().equals(GUID)){
                return sensor;
            }
        }
        return null;
    }
    public void updateSensor(String jsonstring) {
        try {
            JSONObject value;
            jsonstring = "{\"data\":" + jsonstring + "}";
            JSONObject sensors = new JSONObject(jsonstring);
            JSONArray sensorarray = sensors.getJSONArray("data");
            for(int i=0;i<sensorarray.length();i++) {
                value = sensorarray.getJSONObject(i);
                mSensors.add(new Sensor(
                        value.getString("senNo"),
                        Double.valueOf(value.getString("data")),
                        value.getString("nickname"),
                        value.getString("time")
                ));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }
}
