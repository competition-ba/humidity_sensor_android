package com.competitionba.humiditysensor;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SensorActivity extends AppCompatActivity {
    public static final String TAG = "com.competitionba.usbdriver";
    private static final String ACTION_USB_PERMISSION = "com.fhh.competitionba.USB_PERMISSION";
    private EditText mETName;
    private EditText mETPsw;
    private EditText mETIP;
    private EditText mETNickname;
    private Button mButtonCancel;
    private Button mButtonConnect;
    private TextView guid;
    private byte[] msg;
    private boolean isOpen;
    ReadThread rt;
    public static Handler usbhandler;
    private int msglen=0;
    private int retval=0;
    File storedata;
    String username;
    final OkHttpClient client = new OkHttpClient();
    private Handler httphandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            String result = (String)msg.obj;
            if(result.equals("OK")){
                Toast.makeText(SensorActivity.this,R.string.sensor_register_done,Toast.LENGTH_SHORT).show();
            }
            else {
                //Toast.makeText(RegisterActivity.this,"返回值："+result,Toast.LENGTH_SHORT).show();
                Toast.makeText(SensorActivity.this,R.string.sensor_register_fail,Toast.LENGTH_SHORT).show();
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);
        mETName=findViewById(R.id.et_name);
        mETPsw=findViewById(R.id.et_psw);
        mETIP=findViewById(R.id.et_IP);
        mETNickname = findViewById(R.id.et_nickname);
        mButtonCancel=findViewById(R.id.button_cancel);
        mButtonConnect=findViewById(R.id.button_connect);
        guid = findViewById(R.id.id_GUID);
        Intent intent = getIntent();
        username = intent.getStringExtra("username");
        //Toast.makeText(SensorActivity.this,username,Toast.LENGTH_SHORT).show();
        //创建USB GUID接收Handler
        usbhandler = new Handler() {
            public void handleMessage(Message msga) {
                StaticVarHolder.driver.WriteData(msg,msglen);
                guid.setText((String)msga.obj);
                StaticVarHolder.driver.CloseDevice();
                isOpen = false;
            }
        };
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
                trigUSB();
            }
        });
        //当监测到GUID的内容变化时，我们就可以向服务器发送注册数据了。
        guid.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                JSONObject data = new JSONObject();
                try {
                    data.put("user", username);
                    data.put("senNo",editable.toString());
                    data.put("nickname",mETNickname.getText().toString());
                    //创建线程
                    RequestBody formBody = new FormBody.Builder()
                            .add("usersAndGUID",data.toString())
                            .build();
                    final Request request = new Request.Builder()
                            .url("http://cloud.fhh200000.com/Arduino/RegSensor")
                            .post(formBody)
                            .build();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Response response;
                            try {
                                response = client.newCall(request).execute();
                                if(response!=null) {
                                    if (response.isSuccessful()) {
                                        httphandler.obtainMessage(1, response.body().string()).sendToTarget();

                                    } else {
                                        throw new IOException("Unexpected code:" + response);
                                    }
                                }
                                else
                                    throw new IOException("Empty response!");
                            }
                            catch (IOException e) {
                                httphandler.obtainMessage(1, "FATAL").sendToTarget();
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                catch (JSONException ex){
                    ex.printStackTrace();
                }
            }
        });
    }
    private void verify(){
        msg = new byte[128];
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
                msg[msglen+j] = (byte) tmpip;
            }
        }
        catch(NumberFormatException ex) {
            Toast.makeText(SensorActivity.this,R.string.invalid_ip,Toast.LENGTH_LONG).show();
            return;
        }
            msglen+=4;
        try{
            FileOutputStream fos = new FileOutputStream(storedata);
            JSONObject data = new JSONObject();
            data.put("ssid",mETName.getText().toString());
            data.put("pwd",mETPsw.getText().toString());
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
    private void trigUSB(){
        //申请权限
        StaticVarHolder.driver = new CH34xUARTDriver(
                (UsbManager) getSystemService(Context.USB_SERVICE), this,
                ACTION_USB_PERMISSION);
        //判断设备是否支持USB Host（OTG）
        if (!StaticVarHolder.driver.UsbFeatureSupported())// 判断系统是否支持USB HOST
        {
            Dialog dialog = new AlertDialog.Builder(SensorActivity.this)
                    .setTitle("提示")
                    .setMessage(R.string.otg_not_supported)
                    .setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface arg0, int arg1) {
                                    return;
                                }
                            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        if (!isOpen) {
            retval = StaticVarHolder.driver.ResumeUsbList();
            if (retval == -1)// ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
            {
                Toast.makeText(SensorActivity.this, "打开设备失败!",
                        Toast.LENGTH_SHORT).show();
                StaticVarHolder.driver.CloseDevice();
            } else if (retval == 0) {
                if (!StaticVarHolder.driver.UartInit()) {//对串口设备进行初始化操作
                    Toast.makeText(SensorActivity.this, R.string.USB_comm_failure,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                isOpen = true;
                if (!StaticVarHolder.driver.SetConfig(9600, (byte)8, (byte)1, (byte)0,(byte)0)) {//串口设置失败
                    return;
                }
                //发送启动信号
                if(StaticVarHolder.driver.WriteData(new byte[]{'O'},1)==0){
                    Toast.makeText(SensorActivity.this, R.string.USB_comm_failure,
                            Toast.LENGTH_SHORT).show();
                };
                rt = new ReadThread();
                rt.start();//开启读线程读取串口接收的数据
            } else {
                Toast.makeText(SensorActivity.this,R.string.USB_perm_failure,Toast.LENGTH_SHORT).show();
                return;
            }
        }
        else {
            isOpen = false;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            StaticVarHolder.driver.CloseDevice();
        }
    }
    private class ReadThread extends Thread {
        public void run() {
            byte[] buffer = new byte[4096];
            while (true) {
                Message msg = Message.obtain();
                if (!isOpen) {
                    break;
                }
                int length = StaticVarHolder.driver.ReadData(buffer, buffer.length);
                if (length > 0) {
                    String content = new String(buffer, 0, length);
                    msg.obj = content + "";
                    usbhandler.sendMessage(msg);
                }
            }
        }
    }

}
