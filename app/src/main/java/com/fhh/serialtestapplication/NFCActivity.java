package com.fhh.serialtestapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NFCActivity extends AppCompatActivity {
    private Button submitButton;
    private EditText ssid,password,guid;
    public static Handler handler;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        StaticVarHolder.isNFCActivityOpened=false;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StaticVarHolder.isNFCActivityOpened=true;
        setContentView(R.layout.activity_nfc);
        submitButton = findViewById(R.id.nfcsubmit);
        ssid = findViewById(R.id.nfcssid);
        password = findViewById(R.id.nfcpwd);
        guid = findViewById(R.id.nfcguid);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //检测是否为空
                if(ssid.getText().length()==0||password.getText().length()==0){
                    Toast.makeText(NFCActivity.this,"信息填写不完整，请检查！",Toast.LENGTH_LONG).show();
                    return;
                }
                //检测是否过长
                //SSID：1～32,密码：0～63
                if(ssid.getText().length()>32||password.getText().length()>63){
                    Toast.makeText(NFCActivity.this,"数据过长，请检查！",Toast.LENGTH_LONG).show();
                    return;
                }
                //开始录入数据
                int i=0;
                char[] tmp = ssid.getText().toString().toCharArray();
                for(int j=0;j<tmp.length;j++)
                    StaticVarHolder.msg[j] = (byte)tmp[j];
                i = tmp.length;
                StaticVarHolder.msg[i] = (byte)0xFE;
                tmp = password.getText().toString().toCharArray();
                for(int j=1;j<=tmp.length;j++)
                    StaticVarHolder.msg[i+j] = (byte)tmp[j-1];
                StaticVarHolder.msg[i+tmp.length+1]=0x00;
                StaticVarHolder.msglen = i+tmp.length+2;//名称+0xFE+密码+0x00
                Toast.makeText(NFCActivity.this,"数据已录入！",Toast.LENGTH_LONG).show();
            }
        });
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                guid.setText((String)msg.obj);
            }
        };
    }
}
