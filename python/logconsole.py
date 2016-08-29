#!/usr/bin/env python
# -*- coding: utf-8 -*-
#
# ***********************************************************************************
# LogBoy调试日志服务器
# 
# 需要安装web.py库，easy_install web.py 或者pip install web.py
# 
# 启动应用之前先，启动日志控制台服务器
# 默认端口是8080，启动之后，可以浏览器中访问http://localhost:8080
# 就可以在网页中来查看了，网页会自动刷新
# ***********************************************************************************


import sys
import argparse
import stat
import json
import web

reload(sys)
sys.setdefaultencoding('utf-8')

web.config.debug = False

urls = (
    '/', 'index'
)

localLogs = ""
deviceInfoLog = "设备:小米"
appInfoLog = "应用:e袋洗"


class index:
    def GET(self):

        htmlFormat = '''
        <!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>LogBoy日志输出</title>
</head>
<body bgcolor="#212121">

<!--按钮样式-->

<style>
input {
	color: #ffffff;
	background: #F44336;
	border-color: #F44336;
    -webkit-border-radius: 3px;
    -moz-border-radius: 3px;
    border-radius: 3px;
}

form{
 margin:0px;
 display: inline
}

.img-circle {
  -webkit-border-radius: 20px 20px 20px 20px;
  -moz-border-radius: 20px 20px 20px 20px;
  border-radius: 20px 20px 20px 20px;
}

p{ margin:5px auto}

</style>

<!--定义js函数-->

<script language="JavaScript">

 function myrefresh(){
      window.location.reload()
      window.scrollTo(0,document.body.scrollHeight)
  }

 result = setInterval('myrefresh()',1000)

function hao() {
   if (document.form1.ok.value=="停止自动刷新")
   {
     document.form1.ok.value="开始自动刷新"
     clearInterval(result)
   }
   else
   {
     document.form1.ok.value="停止自动刷新"
     myrefresh()
   }
}

</script>

<div>
    %s
</div>

<HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3)" color=#E91E63 SIZE=3>

<div>

    <img class="img-circle"
         src="http://images2015.cnblogs.com/blog/535280/201608/535280-20160827163732585-1617323665.jpg" align="left"
         width="35" height="35">

    <center bgcolor="#212121">

        <form name="form0" method="post" action="">
            <input name="name" type="button" id="name" value="%s">
        </form>

        <form name="form2" method="post" action="">
            <input name="device" type="button" id="device" value="%s">
        </form>

        <form name="form1" method="post" action="">
            <input name="ok" type="button" id="ok" value="停止自动刷新" onClick="hao()">
        </form>
    </center>
</div>
</body>
</html>'''

        global deviceInfoLog
        global appInfoLog
        global localLogs

        deviceInfoLog = deviceInfoLog.encode('utf-8')
        appInfoLog = appInfoLog.encode('utf-8')
        localLogs = localLogs.encode('utf-8')

        print "->" + deviceInfoLog
        print "->" + appInfoLog

        return htmlFormat % (localLogs, deviceInfoLog, appInfoLog)

    def POST(self):

        inputs = web.input()

        content = inputs.get('log')
        if content is None:
            content = "[{\"level\":\"INFO\",\"tag\":\"\",\"msg\":\"-->LogBoy日志输出控制台\",\"time\":\"\"}]"
        deviceInfo = inputs.get('device')
        if not deviceInfo is None:
            print deviceInfo
        appInfo = inputs.get('app')
        if not appInfo is None:
            print  appInfo

        if not appInfo is None:
            global deviceInfoLog
            global appInfoLog
            deviceInfoLog = deviceInfo
            appInfoLog = appInfo

        if content.startswith('{') and content.endswith('}'):
            content = '[' + content + ']'
        logs = json.loads(content)

        for log in logs:

            if 'stack' not in log:
                log['stack'] = " "

            color = '#808080'
            if log['level'] == 'INFO':
                color = '#008000'
            elif log['level'] == 'WARNING':
                color = '#FFA500'
            elif log['level'] == 'ERROR':
                color = '#FF0000'

            strLog = '<div style="color:%s">%s  %s: [%s] %s </div>' % (
                color, log['time'], log['level'], log['tag'], convertTemp(log['msg']))

            stacks = log['stack'].split('\n')
            strLog = strLog + ('<div color="%s">' % color)
            for s in stacks:
                strLog = strLog + ('<div><p style=\"face:serif;\">%s</p></div>' % (s.strip()))

            strLog = strLog + '</div>'

            global localLogs
            localLogs = localLogs + strLog

        return ""


def convertTemp(stringLog):
    if stringLog.find("http") != -1:
        print "进入格式化URL了"
        return stringLog[0:stringLog.find("http")] + "<a href=\"" + stringLog[
                                                                    stringLog.find(
                                                                        "http"):] + "\" target=\"view_window\" >" \
               + stringLog[stringLog.find("http"):] + "</a>"
    else:
        return stringLog


if __name__ == '__main__':
    app = web.application(urls, globals())
    app.run()
