package com.competitionba.humiditysensor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class SensorActivity extends AppCompatActivity {
    private EditText mETName;
    private EditText mETPsw;
    private EditText mETIP;
    private Button mButtonCancel;
    private Button mButtonConnect;
    private byte[] msg;
    private int msglen=0;
    File storedata;
    String username;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        msg = new byte[128];
        mETName=findViewById(R.id.et_name);
        mETPsw=findViewById(R.id.et_psw);
        mETIP=findViewById(R.id.et_IP);
        mButtonCancel=findViewById(R.id.button_cancel);
        mButtonConnect=findViewById(R.id.button_connect);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        Toast.makeText(SensorActivity.this,username,Toast.LENGTH_SHORT).show();
        //从存储的文件中读取出数据信息
        storedata = new File(getFilesDir(), "info.json");
        try{
            if(!storedata.exists()){
                storedata.createNewFile();
            }
            Scanner getJson = new Scanner(storedata);
            String data = new String();
            while(getJson.hasNext())
                data += getJson.next();
            getJson.close();
            JSONObject jsondata = new JSONObject(data);
            mETName.setText(jsondata.getString("ssid"));
            mETPsw.setText(jsondata.getString("pwd"));
            mETIP.setText(jsondata.getString("ip"));
        }
        catch(IOException ex){
            Toast.makeText(SensorActivity.this,R.string.configuration_read_failure,Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
        catch(JSONException ex){
            ex.printStackTrace();
        }
        mButtonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verify();
            }
        });
    }
    private void verify(){
        if(mETName.getText().toString().isEmpty()||mETPsw.getText().toString().isEmpty()||mETIP.getText().toString().isEmpty()){
            Toast.makeText(SensorActivity.this,R.string.invalid_input,Toast.LENGTH_LONG).show();
            return;
        }
        int i;
        char[] tmp = mETName.getText().toString().toCharArray();
        for(int j=0;j<tmp.length;j++)
            msg[j] = (byte)tmp[j];
        i = tmp.length;
        msg[i] = 0x00;
        tmp = mETPsw.getText().toString().toCharArray();
        for(int j=1;j<=tmp.length;j++)
            msg[i+j] = (byte)tmp[j-1];
        msg[i+tmp.length+1]=0x00;
        msglen = i+tmp.length+2;//名称+0x00+密码+0x00
        String[] ipaddr = mETIP.getText().toString().split("\\.");
        if(ipaddr.length!=4){
            Toast.makeText(SensorActivity.this,R.string.invalid_ip,Toast.LENGTH_LONG).show();
            return;
        }
        int tmpip;
        try {
            for(int j=0;j<4;j++) {
                tmpip = Integer.parseInt(ipaddr[j]);
                if (tmpip > 255)
                    throw new NumberFormatException();
                msg[StaticVarHolder.msglen+j] = (byte) tmpip;
            }
        }
        catch(NumberFormatException ex) {
            Toast.makeText(SensorActivity.this,R.string.invalid_ip,Toast.LENGTH_LONG).show();
            return;
        }
        StaticVarHolder.msglen+=4;
        try{
            FileOutputStream fos = new FileOutputStream(storedata);
            JSONObject data = new JSONObject();
            data.put("ssid",mETName.getText().toString());
            data.put("pwd",mETName.getText().toString());
            data.put("ip",mETIP.getText().toString());
            fos.write(data.toString().getBytes());
            fos.close();
        }
        catch (Exception ex){
            Toast.makeText(SensorActivity.this,R.string.configuration_write_failure,Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
        Toast.makeText(SensorActivity.this,R.string.configuration_saved,Toast.LENGTH_LONG).show();
    }
}
