package com.fhh.serialtestapplication;

import android.nfc.cardemulation.HostApduService;
import android.os.Bundle;
import android.util.Log;

public class NFCCommService extends HostApduService {
    @Override
    public byte[] processCommandApdu(byte[] bytes, Bundle bundle) {
        Log.e("aaaaa",new String(bytes));
        byte[] ret = {'J','I','N','G','S','A','I','!'};
        return ret;
    }

    @Override
    public void onDeactivated(int i) {

    }
}
