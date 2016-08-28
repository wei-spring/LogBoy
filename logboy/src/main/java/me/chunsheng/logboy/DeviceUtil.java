package me.chunsheng.logboy;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Copyright © 2016 edaixi. All Rights Reserved.
 * Author: wei_spring
 * Date: 16/8/27
 * Email:weichsh@edaixi.com
 * Function: 获取设备信息
 */
public class DeviceUtil {

    private Context mContext;

    public DeviceUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 获取应用包名称
     */
    public String getPackName() {
        return mContext.getPackageName();
    }

    /**
     * 获取应用版本名称
     */
    public String getVerName() {
        String verName = "";
        try {
            verName = mContext.getPackageManager().getPackageInfo(getPackName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return verName;
    }

    /**
     * 获取应用版本号
     */
    public int getVerCode() {
        int versionCode = 0;
        try {
            versionCode = mContext.getPackageManager().getPackageInfo(getPackName(),
                    0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return versionCode;
    }

    /**
     * 获取应用名称
     */
    public String getAppName() {
        String appName = "";
        try {
            PackageManager packManager = mContext.getPackageManager();
            ApplicationInfo appInfo = mContext.getApplicationInfo();
            appName = (String) packManager.getApplicationLabel(appInfo);
        } catch (Exception e) {
        }
        return appName;
    }

    /**
     * 获取手机名字,系统版本号
     */
    public String getPhoneName() {
        return android.os.Build.MANUFACTURER + " 系统版本号:" + android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取移动用户标志，IMSI
     * <p>
     * 用到的权限：
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */
    public String getSubscriberId() {
        String strResult = "";
        TelephonyManager telephonyManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            strResult = telephonyManager.getSubscriberId();
        }
        return strResult;
    }

    /**
     * 获取设备ID
     * <p>
     * 用到的权限：
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */
    public String getDeviceID() {
        String strResult = null;
        TelephonyManager telephonyManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            strResult = telephonyManager.getDeviceId();
        }
        if (strResult == null) {
            strResult = Settings.Secure.getString(mContext.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        return strResult;
    }

    /**
     * 获取SIM卡号
     * <p>
     * 用到的权限：
     * <uses-permission android:name="android.permission.READ_PHONE_STATE" />
     */
    public String getSim() {
        String strResult = "";
        TelephonyManager telephonyManager = (TelephonyManager) mContext
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            strResult = telephonyManager.getSimSerialNumber();
        }
        return strResult;
    }

    /**
     * 获取Wifi Mac地址
     * <p>
     * 要想获取更多Wifi相关信息请查阅WifiInfo资料
     * <p>
     * 用到的权限：
     * <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     */
    public String getMac() {

        WifiManager wifiManager = (WifiManager) mContext
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            WifiInfo wi = wifiManager.getConnectionInfo();
            return wi.getMacAddress();
        }
        return null;
    }

    /**
     * 服务商名称：
     * 例如：中国移动、联通
     */
    public String getCellInfo() {
        TelephonyManager tm = null;
        try {
            tm = (TelephonyManager) mContext
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String imsi = tm.getSubscriberId();
            if (imsi != null) {
                if (imsi.startsWith("46000") || imsi.startsWith("46002") || imsi.startsWith("46007")) {
                    return "中国移动";
                } else if (imsi.startsWith("46001") || imsi.startsWith("46006")) {
                    return "中国联通";
                } else if (imsi.startsWith("46003") || imsi.startsWith("46005")) {
                    return "中国电信";
                }
            }
            return "信息获取失败";
        } catch (Exception e) {
            return null;
        }
    }


}


