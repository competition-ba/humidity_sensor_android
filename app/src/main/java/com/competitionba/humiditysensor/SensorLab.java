package com.competitionba.humiditysensor;

import android.content.Context;

import java.util.List;

public class SensorLab {
    private static SensorLab sSensorLab;
    private List<Sensor> mSensors;
    public static SensorLab get(Context context){
        if (sSensorLab == null){
            sSensorLab = new SensorLab(context);
        }
        return sSensorLab;
    }
    private SensorLab(Context context){

    }
}
