# Logboy

![Logo](/app/src/main/res/mipmap-hdpi/ic_launcher.png)

> 关于:

Logboy是一个日志管理库，可以本地控制台输出日志亦可远程输出，
通过搭建本地的Python环境，可以在本地浏览器查看输出的日志.

> 使用：

 1. 首先本地搭建python环境，这里使用时轻量级的web.py作为本地的服务器：
  * Mac & Windows，配置Python开发环境（Mac系统自带Python环境）
  * 下载：wget http://webpy.org/static/web.py-0.37.tar.gz
  * 解压，进入目录，安装：python setup.py install
  * 然后运行Android工程中python目录下的logconsole.py, 访问http://localhost:8080/

 2. Android项目使用：

 ```Java
compile 'me.chunsheng.logboy:logboy:1.0.2'
 ```

 在Application或者Activity的OnCreate方法初始化：

 ```Java
//初始化如下:输入自己的IP+port
LogBoy.setRemoteUrl("http://192.168.1.108:8080/");
LogBoy.init(this);

//输出日志，和系统使用方法一样，分Tag ,Msg 以及日志级别(DEBUG|INFO|WARNING|ERROR)
LogBoy.d("TagBoyD::", i + "Man cannot discover new oceans unless he has courage to lose sight of the shore. —Gide");
LogBoy.w("TagBoyW::", i + "Towering genius disdains a beaten path. It seeks regions hitherto unexplored. —Lincoln ");
LogBoy.e("TagBoyE:", i + "Tomorrow is never clear. Our time is here.");
 ```

> 有图有真相


1. 工程demo运行效果图:


<img src="/images/logboy_screen_1.jpg" width="425" height="480" />

<img src="/images/logboy_screen_2.jpg" width="425" height="480" />


2. 远程服务器运行效果图:


<img src="/images/logboy_print.jpg" width="1200" height="800" />


## License

LogBoy is made available under the [Apache-2.0](https://opensource.org/licenses/Apache-2.0)


[1]: http://www.uustory.com/?p=2049
[2]: http://www.cnblogs.com/coder2012/p/4023442.html


