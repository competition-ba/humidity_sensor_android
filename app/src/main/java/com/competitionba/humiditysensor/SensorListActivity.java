package com.competitionba.humiditysensor;

import android.support.v4.app.Fragment;

public class SensorListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment(){
        return new SensorListFragment();
    }

}
