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

public class LoginActivity extends AppCompatActivity {
    private EditText mName;
    private EditText mPsw;
    private Button mButtonLogin;
    private Button mButtonRegister;
    final OkHttpClient client = new OkHttpClient();
    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            /*登录返回信息：
              1.登录成功（OK）。
              2.登录失败(其他任何信息)。*/
            String result = (String)msg.obj;
            if(!result.equals("OK")){
                //Toast.makeText(LoginActivity.this,"返回值："+result,Toast.LENGTH_SHORT).show();
                Toast.makeText(LoginActivity.this,R.string.login_failure,Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(LoginActivity.this,R.string.login_done,Toast.LENGTH_SHORT).show();
                String username= mName.getText().toString();
                //TODO:恢复为列表界面
                Intent mainintent = new Intent(LoginActivity.this,SensorActivity.class);
                mainintent.putExtra("username",username);
                startActivity(mainintent);
                finish();
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mName= findViewById(R.id.user_name);
        mPsw= findViewById(R.id.psw);
        mButtonLogin= findViewById(R.id.button_login);

        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name1= mName.getText().toString().trim();
                String psw1=mPsw.getText().toString().trim();
                if(name1.length()==0 || psw1.length()==0) {
                    Toast.makeText(LoginActivity.this,R.string.invalid_input,Toast.LENGTH_SHORT).show();
                    return;
                }
                //组装登录信息
                JSONObject data = new JSONObject();
                try{
                    data.put("user",name1);
                    data.put("password",psw1);
                    data.put("state","login");
                }
                catch (JSONException ex) {
                    ex.printStackTrace();
                }
                //假设用户名正确（不正确时可在后方清空）
                RequestBody formBody = new FormBody.Builder()
                        .add("users",data.toString())
                        .build();
                final Request request = new Request.Builder()
                        .url("http://cloud.fhh200000.com/Arduino/Users")
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
                            Toast.makeText(getApplicationContext(),R.string.transfer_failure,Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }).start();
            }

        });
        mButtonRegister= findViewById(R.id.button_register);
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
    }
}
