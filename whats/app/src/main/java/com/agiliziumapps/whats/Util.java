package com.agiliziumapps.whats;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.google.firebase.auth.FirebaseUser;

public class Util {
    public static FirebaseUser firebaseUser;
    public static Usuario usuario;
    public static String mVerificationId;
    public static String getCoutryISO(Context ctx)
    {
        String iso = null;
        TelephonyManager telephonyManager = (TelephonyManager) ctx.getSystemService(ctx.TELEPHONY_SERVICE);
        if(telephonyManager.getNetworkCountryIso() != null)
        {
            if (!telephonyManager.getNetworkCountryIso().equals(""))
            {
                iso = telephonyManager.getNetworkCountryIso();
            }
        }
        return CountryToPhonePrefix.getPhone(iso);
    }


}
