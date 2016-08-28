package me.chunsheng.logboytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import me.chunsheng.logboy.LogBoy;
import me.chunsheng.logboytest.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView mLog = (TextView) findViewById(R.id.log);
        mLog.setText("点击查看输出:\n http://192.168.1.108:8080/");

        LogBoy.init(this);

        for (int i = 50; i < 80; i++) {
            LogBoy.i("我是打印信息::", i + "http://baidu.com");
            LogBoy.e("我是错误:", i + "http://chunsheng.me");
        }
    }
}
