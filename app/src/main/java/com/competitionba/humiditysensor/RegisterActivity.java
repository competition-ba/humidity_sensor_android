package com.competitionba.humiditysensor;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText mName;
    private EditText mPsw;
    private EditText mPswAgain;
    private Button mButtonRegister;
    private Button mButtonReturnLogin;

    final OkHttpClient client = new OkHttpClient();

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Toast.makeText(RegisterActivity.this,"返回信息："+msg.toString(),Toast.LENGTH_SHORT).show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mName= findViewById(R.id.user_name);
        mPsw= findViewById(R.id.psw);
        mPswAgain= findViewById(R.id.psw_again);
        mButtonRegister= findViewById(R.id.button_register);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name1= mName.getText().toString().trim();
                String psw1=mPsw.getText().toString().trim();
                String psw2=mPswAgain.getText().toString().trim();
                if (psw1.equals(psw2)){
                    postRequest(name1,psw1);
                }else
                    Toast.makeText(RegisterActivity.this,"两次输入密码不同", Toast.LENGTH_SHORT).show();
            }
        });
        mButtonReturnLogin= findViewById(R.id.button_return_login);
        mButtonReturnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                finish();
            }
        });
    }
    private void postRequest(String name,String password){
        //将数据转换成为JSON
        JSONObject data = new JSONObject();
        try{
            data.put("username",name);
            data.put("password",password);
            data.put("state","register");
        }
        catch (JSONException ex) {
            ex.printStackTrace();
        }
        RequestBody formBody = new FormBody.Builder()
                .add("users",data.toString())
                .build();
        final Request request = new Request.Builder()
                .url("http://cloud.fhh200000.com/Arduino")
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
                            mHandler.obtainMessage(1, response.body().string()).sendToTarget();

                        } else {
                            throw new IOException("Unexpected code:" + response);
                        }
                    }
                    else
                        throw new IOException("Empty response!");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
