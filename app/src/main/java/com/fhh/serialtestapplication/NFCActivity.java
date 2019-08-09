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
    private EditText ssid,password,guid,ip;
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
        ip = findViewById(R.id.nfcip);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ssid.getText().toString().isEmpty()||password.getText().toString().isEmpty()||ip.getText().toString().isEmpty()){
                    Toast.makeText(NFCActivity.this,"信息填写不完整，请检查！",Toast.LENGTH_LONG).show();
                    return;
                }
                int i;
                char[] tmp = ssid.getText().toString().toCharArray();
                for(int j=0;j<tmp.length;j++)
                    StaticVarHolder.msg[j] = (byte)tmp[j];
                i = tmp.length;
                StaticVarHolder.msg[i] = 0x00;
                tmp = password.getText().toString().toCharArray();
                for(int j=1;j<=tmp.length;j++)
                    StaticVarHolder.msg[i+j] = (byte)tmp[j-1];
                StaticVarHolder.msg[i+tmp.length+1]=0x00;
                StaticVarHolder.msglen = i+tmp.length+2;//名称+0x00+密码+0x00
                String[] ipaddr = ip.getText().toString().split("\\.");
                if(ipaddr.length!=4){
                    Toast.makeText(NFCActivity.this,R.string.invalid_ip,Toast.LENGTH_LONG).show();
                    return;
                }
                int tmpip;
                try {
                    for(int j=0;j<4;j++) {
                        tmpip = Integer.parseInt(ipaddr[j]);
                        if (tmpip > 255)
                            throw new NumberFormatException();
                        StaticVarHolder.msg[StaticVarHolder.msglen+j] = (byte) tmpip;
                    }
                }
                catch(NumberFormatException ex) {
                    Toast.makeText(NFCActivity.this,R.string.invalid_ip,Toast.LENGTH_LONG).show();
                    return;
                }
                StaticVarHolder.msglen+=4;
                Toast.makeText(NFCActivity.this,"配置已保存！",Toast.LENGTH_LONG).show();
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
