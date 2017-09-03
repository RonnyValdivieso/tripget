package com.tripget.tripget.Conexion;

import android.app.Application;

/**
 * Created by ivonne on 03/09/17.
 */

public class GlobalVariable extends Application {

    private String mGlobalVarValue;

    public String getGlobalVarValue() {
        return mGlobalVarValue;
    }

    public void setGlobalVarValue(String str) {
        mGlobalVarValue = str;
    }
}
