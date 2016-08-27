package me.chunsheng.logboy;

/**
 * Copyright © 2016 edaixi. All Rights Reserved.
 * Author: wei_spring
 * Date: 16/8/26
 * Email:weichsh@edaixi.com
 * Function: LogBoy输出接口
 */
public interface ILogBoy {

    void v(String tag, String msg);

    void v(String tag, String msg, Throwable tr);

    void d(String tag, String msg);

    void d(String tag, String msg, Throwable tr);

    void i(String tag, String msg);

    void i(String tag, String msg, Throwable tr);

    void w(String tag, String msg);

    void w(String tag, String msg, Throwable e);

    void e(String tag, String msg);

    void e(String tag, String msg, Throwable e);

    void destory();

}
