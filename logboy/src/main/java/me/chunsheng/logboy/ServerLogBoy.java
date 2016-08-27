package me.chunsheng.logboy;

/**
 * Copyright © 2016 edaixi. All Rights Reserved.
 * Author: wei_spring
 * Date: 16/8/26
 * Email:weichsh@edaixi.com
 * Function: 远程日志上传
 */
public class ServerLogBoy implements ILogBoy {

    private ServerLogBoyPrinter printer;


    public ServerLogBoy(String url, int interval) {
        printer = new ServerLogBoyPrinter(url, interval);
    }

    @Override
    public void v(String tag, String msg) {
        printer.print(new FormatLogBoy(FormatLogBoy.FORMAT_VERBOSE, tag, msg));
    }

    @Override
    public void v(String tag, String msg, Throwable tr) {
        printer.print(new FormatLogBoy(FormatLogBoy.FORMAT_VERBOSE, tag, msg, tr));
    }

    @Override
    public void d(String tag, String msg) {
        printer.print(new FormatLogBoy(FormatLogBoy.FORMAT_DEBUG, tag, msg));
    }

    @Override
    public void d(String tag, String msg, Throwable tr) {
        printer.print(new FormatLogBoy(FormatLogBoy.FORMAT_DEBUG, tag, msg, tr));
    }

    @Override
    public void i(String tag, String msg) {
        printer.print(new FormatLogBoy(FormatLogBoy.FORMAT_INFO, tag, msg));
    }

    @Override
    public void i(String tag, String msg, Throwable tr) {
        printer.print(new FormatLogBoy(FormatLogBoy.FORMAT_INFO, tag, msg, tr));
    }

    @Override
    public void w(String tag, String msg) {
        printer.print(new FormatLogBoy(FormatLogBoy.FORMAT_WARN, tag, msg));
    }

    @Override
    public void w(String tag, String msg, Throwable e) {
        printer.print(new FormatLogBoy(FormatLogBoy.FORMAT_WARN, tag, msg, e));
    }

    @Override
    public void e(String tag, String msg) {
        printer.print(new FormatLogBoy(FormatLogBoy.FORMAT_ERROR, tag, msg));
    }

    @Override
    public void e(String tag, String msg, Throwable e) {
        printer.print(new FormatLogBoy(FormatLogBoy.FORMAT_ERROR, tag, msg, e));
    }

    @Override
    public void destory() {
        printer.stop();
    }
}
