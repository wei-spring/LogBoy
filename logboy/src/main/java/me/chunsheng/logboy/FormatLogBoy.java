package me.chunsheng.logboy;

import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;

/**
 * Copyright © 2016 edaixi. All Rights Reserved.
 * Author: wei_spring
 * Date: 16/8/26
 * Email:weichsh@edaixi.com
 * Function: 格式化日志,然后进行打印或者上传处理
 */
public class FormatLogBoy {

    public static final String FORMAT_VERBOSE = "VERBOSE";
    public static final String FORMAT_DEBUG = "DEBUG";
    public static final String FORMAT_INFO = "INFO";
    public static final String FORMAT_WARN = "WARNING";
    public static final String FORMAT_ERROR = "ERROR";

    private String level;
    private String tag;
    private String msg;
    private Throwable stack;
    private Date time;

    public FormatLogBoy(String level, String tag, String msg) {
        this.level = level;
        this.tag = tag;
        this.msg = msg;
        this.stack = null;
        this.time = new Date();
    }

    public FormatLogBoy(String level, String tag, String msg, Throwable e) {
        this.level = level;
        this.tag = tag;
        this.msg = msg;
        this.stack = e;
        this.time = new Date();
    }

    private String parseStack(Throwable e) {
        final StringWriter result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        e.printStackTrace(printWriter);
        String stacktrace = result.toString();
        printWriter.close();
        return stacktrace;
    }

    public String toJSON() {
        try {
            JSONObject json = new JSONObject();
            json.put("level", level);
            json.put("tag", tag);
            json.put("msg", msg);
            json.put("stack", stack == null ? "" : parseStack(stack));
            json.put("time", time);

            return json.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Format LogBoy Exception...";
    }
}
