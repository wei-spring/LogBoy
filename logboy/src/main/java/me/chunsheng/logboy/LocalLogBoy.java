package me.chunsheng.logboy;

import android.util.Log;

/**
 * Copyright © 2016 edaixi. All Rights Reserved.
 * Author: wei_spring
 * Date: 16/8/26
 * Email:weichsh@edaixi.com
 * Function: 本地控制台日志输出
 */
public class LocalLogBoy implements ILogBoy {
    @Override
    public void v(String tag, String msg) {
        Log.v(tag, msg);
    }

    @Override
    public void v(String tag, String msg, Throwable tr) {
        Log.v(tag, msg, tr);
    }

    @Override
    public void d(String tag, String msg) {
        Log.d(tag, msg);
    }

    @Override
    public void d(String tag, String msg, Throwable tr) {
        Log.d(tag, msg, tr);
    }

    @Override
    public void i(String tag, String msg) {
        Log.i(tag, msg);
    }

    @Override
    public void i(String tag, String msg, Throwable tr) {
        Log.i(tag, msg, tr);
    }

    @Override
    public void w(String tag, String msg) {
        Log.w(tag, msg);
    }

    @Override
    public void w(String tag, String msg, Throwable e) {
        Log.w(tag, msg, e);
    }

    @Override
    public void e(String tag, String msg) {
        Log.e(tag, msg);
    }

    @Override
    public void e(String tag, String msg, Throwable e) {
        Log.e(tag, msg, e);
    }

    @Override
    public void destory() {

    }
}
