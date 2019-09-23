package com.competitionba.humiditysensor;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class SensorListActivity extends SingleFragmentActivity {
    private String username;
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        username = getIntent().getStringExtra("username");
        SensorListFragment.username = username;
    }
    @Override
    protected Fragment createFragment(){
        return new SensorListFragment();
    }

}
