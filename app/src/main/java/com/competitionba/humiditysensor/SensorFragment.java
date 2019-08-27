package com.competitionba.humiditysensor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class SensorFragment extends Fragment {
    private Sensor mSensor;
    private EditText mETName;
    private EditText mETPsw;
    private EditText mETGUID;
    private Button mButtonCancel;
    private Button mButtonConnect;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensor = new Sensor();
    }
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_sensor,container,false);
        mETName=(EditText)v.findViewById(R.id.et_name);
        mETName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mETPsw=(EditText)v.findViewById(R.id.et_psw);
        mETPsw.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mETGUID=(EditText)v.findViewById(R.id.et_GUID);
        mETGUID.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mButtonCancel=(Button)v.findViewById(R.id.button_cancel);
        mButtonConnect=(Button)v.findViewById(R.id.button_connect);
        return v;
    }
}
