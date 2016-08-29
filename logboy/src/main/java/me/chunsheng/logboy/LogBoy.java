package me.chunsheng.logboy;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.chunsheng.logboy.http.HttpUtils;

/**
 * Copyright © 2016 edaixi. All Rights Reserved.
 * Author: wei_spring
 * Date: 16/8/26
 * Email:weichsh@edaixi.com
 * Function: 日志打印类,本地控制台打印和远程上传
 */
public class LogBoy {

    private static LogBoy instance = new LogBoy();

    private List<ILogBoy> logPrinters;
    private boolean isInited = false;
    private static Context mContext;

    private static boolean enable = false;
    private static String level = FormatLogBoy.FORMAT_DEBUG;
    private static boolean local = true;
    private static boolean remote = true;
    private static int remoteInterval = 1000;
    private static String remoteUrl = "";

    private LogBoy() {
        logPrinters = new ArrayList<>();
    }

    public static void d(String tag, String msg) {
        try {

            for (ILogBoy printer : instance.logPrinters) {
                printer.d(tag, msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static void i(String tag, String msg) {
        try {
            for (ILogBoy printer : instance.logPrinters) {
                printer.i(tag, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void w(String tag, String msg) {
        try {
            for (ILogBoy printer : instance.logPrinters) {
                printer.w(tag, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void w(String tag, String msg, Throwable e) {
        try {

            for (ILogBoy printer : instance.logPrinters) {
                printer.w(tag, msg, e);
            }

        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    public static void e(String tag, String msg) {
        try {
            for (ILogBoy printer : instance.logPrinters) {
                printer.e(tag, msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void e(String tag, String msg, Throwable e) {
        try {
            for (ILogBoy printer : instance.logPrinters) {
                printer.e(tag, msg, e);
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }


    //在Application的attachBaseContext中调用
    public static void init(Context context) {

        try {
            mContext = context;
            if (instance.isInited) {
                return;
            }
            instance.parseConfig(context);

            instance.logPrinters.clear();

            if (!instance.enable) {
                android.util.Log.d("ULOG", "the log is not enabled.");
                return;
            }

            if (instance.local) {
                instance.logPrinters.add(new LocalLogBoy());
            }

            if (instance.remote) {
                instance.logPrinters.add(new ServerLogBoy(instance.remoteUrl, instance.remoteInterval));
            }

            Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {

                @Override
                public void uncaughtException(Thread t, final Throwable e) {

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            try {
                                ServerLogBoyPrinter printer = new ServerLogBoyPrinter();
                                printer.printImmediate(instance.remoteUrl, new FormatLogBoy(FormatLogBoy.FORMAT_ERROR, "Crash", "Application Crashed!!!", e));

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                System.exit(0);
                            }

                        }
                    }).start();

                    try {
                        Thread.sleep(500);

                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }


                }
            });

            instance.isInited = true;

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * 在Application的onTerminate中调用销毁
     */
    public static void destory() {

        try {
            if (instance.logPrinters != null) {
                for (ILogBoy printer : instance.logPrinters) {
                    printer.destory();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void parseConfig(Context ctx) {

        try {
            ApplicationInfo appInfo = ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);

            if (appInfo != null && appInfo.metaData != null) {

                if (appInfo.metaData.containsKey("log_boy.enable")) {
                    enable = appInfo.metaData.getBoolean("log_boy.enable");
                }

                if (appInfo.metaData.containsKey("log_boy.level")) {
                    level = appInfo.metaData.getString("log_boy.level");
                }

                if (appInfo.metaData.containsKey("log_boy.local")) {
                    local = appInfo.metaData.getBoolean("log_boy.local");
                }

                if (appInfo.metaData.containsKey("log_boy.remote")) {
                    remote = appInfo.metaData.getBoolean("log_boy.remote");
                }

                if (appInfo.metaData.containsKey("log_boy.remote_interval")) {
                    remoteInterval = appInfo.metaData.getInt("log_boy.remote_interval");
                }

                if (appInfo.metaData.containsKey("log_boy.remote_url")) {
                    remoteUrl = appInfo.metaData.getString("log_boy.remote_url");
                }

            }

            DeviceUtil deviceUtil = new DeviceUtil(ctx);
            StringBuffer sb = new StringBuffer();
            sb.append("设备:" + deviceUtil.getPhoneName());
            sb.append(" 设备ID: " + deviceUtil.getDeviceID());
            sb.append(" 运营商:" + deviceUtil.getCellInfo());

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(" 应用名称:" + deviceUtil.getAppName());
            stringBuffer.append(" 版本:" + deviceUtil.getVerName());
            stringBuffer.append(" 包名:" + deviceUtil.getPackName());

            if (remoteUrl.length() > 3)
                getDeviceInfo(sb.toString(), stringBuffer.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //设置是否LogBoy可用
    public static void setEnable(boolean isEnable) {
        enable = isEnable;
    }


    //设置日志级别
    public static void setLevel(String levelStr) {
        level = levelStr;
    }


    //设置是否本地控制台打印
    public static void setLocalEnable(boolean isLoaclEnable) {
        local = isLoaclEnable;
    }


    //设置远程日志是否可用
    public static void setRemoteEnable(boolean isRemoteEnable) {
        remote = isRemoteEnable;

    }

    //设置远程服务器上传时间间隔,毫秒
    public void setRemoteInterval(int isRemoteTime) {
        remoteInterval = isRemoteTime;
    }


    //设置远程服务器地址和端口号
    public static void setRemoteUrl(String remoteUrlStr) {
        remoteUrl = remoteUrlStr;
    }

    public void getDeviceInfo(String deviceInfo, String appInfo) {
        try {
            final Map<String, String> params = new HashMap<>();
            params.put("device", deviceInfo);
            params.put("app", appInfo);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    HttpUtils.httpPost(instance.remoteUrl, params);
                }
            }).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}