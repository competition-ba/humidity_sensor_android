package com.fhh.serialtestapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import cn.wch.ch34xuartdriver.CH34xUARTDriver;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "com.fhh.usbdriver";
    private static final String ACTION_USB_PERMISSION = "com.fhh.usbdriver.USB_PERMISSION";
    boolean isOpen;
    int retval, totalrecv;
    private Button abutton,submit;
    public static Handler handler;
    private EditText name,pwd;
    readThread rt;
    @Override
    protected void onDestroy() {
        super.onDestroy();
        StaticVarHolder.isNFCActivityOpened=false;
        StaticVarHolder.driver.CloseDevice();
        if(rt!=null)
            rt.interrupt();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //若buffer没有创建，此处创建buffer
        if(StaticVarHolder.msg==null)
            StaticVarHolder.msg = new byte[128];
        StaticVarHolder.isNFCActivityOpened=true;
        handler = new Handler() {

            public void handleMessage(Message msg) {
                //Toast.makeText(MainActivity.this,"收到消息："+msg,Toast.LENGTH_SHORT).show();
                //               readText.setText((String) msg.obj);
//				readText.append((String) msg.obj);
                EditText a = findViewById(R.id.editText);
                a.setText((String)msg.obj);
            }
        };
        setContentView(R.layout.activity_main);
        abutton = findViewById(R.id.button);
        submit = findViewById(R.id.submit);
        name = findViewById(R.id.editText2);
        pwd = findViewById(R.id.editText3);
        abutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trigSerial();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i=0;
                char[] tmp = name.getText().toString().toCharArray();
                for(int j=0;j<tmp.length;j++)
                    StaticVarHolder.msg[j] = (byte)tmp[j];
                i = tmp.length;
                StaticVarHolder.msg[i] = (byte)0xFE;
                tmp = pwd.getText().toString().toCharArray();
                for(int j=1;j<=tmp.length;j++)
                    StaticVarHolder.msg[i+j] = (byte)tmp[j-1];
                StaticVarHolder.msglen = i+tmp.length+1;//名称+0xFE+密码
            }
        });
        StaticVarHolder.driver = new CH34xUARTDriver(
                (UsbManager) getSystemService(Context.USB_SERVICE), this,
                ACTION_USB_PERMISSION);
        if (!StaticVarHolder.driver.UsbFeatureSupported())// 判断系统是否支持USB HOST
        {
            Dialog dialog = new AlertDialog.Builder(MainActivity.this)
                    .setTitle("提示")
                    .setMessage("您的手机不支持USB HOST，请更换其他手机再试！")
                    .setPositiveButton("确认",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    System.exit(0);
                                }
                            }).create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        trigSerial();
    }
    private void trigSerial(){
        if (!isOpen) {
            retval = StaticVarHolder.driver.ResumeUsbList();
            if (retval == -1)// ResumeUsbList方法用于枚举CH34X设备以及打开相关设备
            {
                Toast.makeText(MainActivity.this, "打开设备失败!",
                        Toast.LENGTH_SHORT).show();
                StaticVarHolder.driver.CloseDevice();
            } else if (retval == 0) {
                if (!StaticVarHolder.driver.UartInit()) {//对串口设备进行初始化操作
                    Toast.makeText(MainActivity.this, "设备初始化失败!",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                isOpen = true;
                //openButton.setText("Close");
                // configButton.setEnabled(true);
                //writeButton.setEnabled(true);
                if (!StaticVarHolder.driver.SetConfig(9600, (byte)8, (byte)1, (byte)0,(byte)0)) {//串口设置失败
                    return;
                }
                //发送启动信号
                if(StaticVarHolder.driver.WriteData(new byte[]{'O'},1)==0){
                    Toast.makeText(MainActivity.this, "发送启动信号失败!",
                            Toast.LENGTH_SHORT).show();
                };
                rt = new readThread();
                rt.start();//开启读线程读取串口接收的数据
            } else {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle("授权未经许可");
                builder.setMessage("确认退出吗？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
//								MainFragmentActivity.this.finish();
                        System.exit(0);
                    }
                });
                builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                });
                builder.show();

            }
        } else {
            //openButton.setText("Open");
            //configButton.setEnabled(false);
            // writeButton.setEnabled(false);
            isOpen = false;
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            StaticVarHolder.driver.CloseDevice();
            totalrecv = 0;
        }
    }
    private class readThread extends Thread {
        public void run() {
            byte[] buffer = new byte[4096];
            while (true) {
                Message msg = Message.obtain();
                if (!isOpen) {
                    break;
                }
                int length = StaticVarHolder.driver.ReadData(buffer, buffer.length);
                if (length > 0) {
//					String recv = toHexString(buffer, length);
//					String recv = new String(buffer, 0, length);
                    //totalrecv += length;
//                    String content = String.valueOf(totalrecv);
//                    String content = new String(buffer);
//                    String content = hexStringToString(toHexString(buffer,length));
                    String content = new String(buffer,0,length);
                    msg.obj = content + "";
                    handler.sendMessage(msg);
                }

            }
        }
    }
}
