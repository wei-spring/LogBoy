package me.chunsheng.logboy;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

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

    private boolean enable = false;
    private String level = FormatLogBoy.FORMAT_DEBUG;
    private boolean local = true;
    private boolean remote = true;
    private int remoteInterval = 1000;
    private String remoteUrl = "";

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


    /**
     * 在Application的attachBaseContext中调用
     *
     * @param context
     */
    public static void init(Context context) {

        try {

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

            android.util.Log.e("Tag", "开始获取清单文件内容啦");
            if (appInfo != null && appInfo.metaData != null) {
                android.util.Log.e("TagB", "已经获取清单文件内容啦");

                if (appInfo.metaData.containsKey("log_boy.enable")) {
                    enable = appInfo.metaData.getBoolean("log_boy.enable");
                    android.util.Log.e("TagC", "获取清单文件内容啦" + enable);
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
