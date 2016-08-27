package me.chunsheng.logboy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.chunsheng.logboy.http.HttpUtils;

/**
 * Copyright © 2016 edaixi. All Rights Reserved.
 * Author: wei_spring
 * Date: 16/8/26
 * Email:weichsh@edaixi.com
 * Function: 远程日志上传工具类
 */
public class ServerLogBoyPrinter {

    private List<FormatLogBoy> logs;
    private String url;
    private int interval = 1000; //单位 毫秒

    private Timer timer;
    private boolean running;

    public ServerLogBoyPrinter() {
    }

    public ServerLogBoyPrinter(String remoteUrl, int interval) {
        this.logs = Collections.synchronizedList(new ArrayList<FormatLogBoy>());
        this.url = remoteUrl;
        this.interval = interval;
    }

    public void print(FormatLogBoy log) {
        start();
        synchronized (logs) {
            logs.add(log);
        }
    }

    public void printImmediate(String url, FormatLogBoy log) {
        Map<String, String> params = new HashMap<>();
        params.put("log", log.toJSON());
        HttpUtils.httpPost(url, params);
    }

    public List<FormatLogBoy> getAndClear() {
        synchronized (logs) {
            List<FormatLogBoy> all = new ArrayList<>(logs);
            logs.clear();
            return all;
        }
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        TimerTask task = new LogPrintTask();
        timer = new Timer(true);
        timer.scheduleAtFixedRate(task, 100, interval);
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
        }
        running = false;
    }

    class LogPrintTask extends TimerTask {

        @Override
        public void run() {
            try {

                List<FormatLogBoy> logs = getAndClear();

                if (logs.size() > 0) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("[");
                    for (FormatLogBoy log : logs) {
                        sb.append(log.toJSON()).append(",");
                    }
                    sb.deleteCharAt(sb.length() - 1).append("]");

                    Map<String, String> params = new HashMap<>();
                    params.put("log", sb.toString());

                    HttpUtils.httpPost(url, params);
                }

            } catch (Exception e) {
                e.printStackTrace();
                stop();
            }
        }

    }
}
