package com.competitionba.humiditysensor;

import android.content.Intent;
import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.os.Message;
import android.widget.Toast;

public class NFCCommService extends HostApduService {
    @Override
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {
        //GUID中不允许出现符号“O”,可用来判断配置是否完成
        if(bytes[0]!='O'){
            if(!StaticVarHolder.isNFCActivityOpened) {
                Intent dialogIntent = new Intent(getBaseContext(),MainActivity.class);
                dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(dialogIntent);
            }
            Message msg = Message.obtain();
            msg.obj = new String(bytes);
            NFCActivity.handler.sendMessage(msg);
            if(StaticVarHolder.msglen==0) {
                Toast.makeText(getBaseContext(),"尚未设置SSID/密码，请设置！",Toast.LENGTH_LONG).show();
                StaticVarHolder.msg = new byte[128];
                int currindex=0;
                for(byte i:new byte[]{0x00,0x4A,0x49,0x4E,0x47,0x53,0x41,0x49})//0x00+"JINGSAI"
                    StaticVarHolder.msg[currindex++] = i;
                StaticVarHolder.msglen=8;
            }
            return StaticVarHolder.getAvailMsg();
        }
        else {
            Toast.makeText(getBaseContext(),"配置完成！",Toast.LENGTH_LONG).show();
            return null;
        }
    }

    @Override
    public void onDeactivated(int i) {

    }
}
