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


        //初始化如下:输入自己的IP+port
        LogBoy.setRemoteUrl("http://192.168.1.108:8080/");
        mLog.setText("Click to show the log print:\n http://192.168.1.108:8080/");
        LogBoy.init(this);

        for (int i = 0; i < 5; i++) {
            LogBoy.i("TagBoyI::", i + "有问题你可以去这里呀 http://baidu.com");
            LogBoy.d("TagBoyD::", i + "Man cannot discover new oceans unless he has courage to lose sight of the shore. —Gide");
            LogBoy.w("TagBoyW::", i + "Towering genius disdains a beaten path. It seeks regions hitherto unexplored. —Lincoln ");
            LogBoy.e("TagBoyE:", i + "Tomorrow is never clear. Our time is here.");
        }
    }
}
