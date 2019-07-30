package com.fhh.serialtestapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    private Button abutton,submit,nfcButton,serialButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //若buffer没有创建，此处创建buffer
        if(StaticVarHolder.msg==null)
            StaticVarHolder.msg = new byte[128];
        setContentView(R.layout.activity_main);
        nfcButton = findViewById(R.id.NFC);
        serialButton = findViewById(R.id.Serial);
        nfcButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openNFC = new Intent(MainActivity.this,NFCActivity.class);
                getApplication().startActivity(openNFC);
            }
        });
        serialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent openSerial = new Intent(MainActivity.this,SerialActivity.class);
                getApplication().startActivity(openSerial);
            }
        });
    }


}
