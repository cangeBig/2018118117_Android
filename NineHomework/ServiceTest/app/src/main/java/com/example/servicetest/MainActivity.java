package com.example.servicetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private MyService.DownloadBinder downloadBinder;
    private TextView text;

    private Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            int progress = msg.arg1;
            text.setText("downloading..." + progress + "%");
        };
    };
    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            downloadBinder = (MyService.DownloadBinder)iBinder;
            //开始下载
            downloadBinder.start();
            //监听进度信息
            listenProgress();
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button startService = (Button) findViewById(R.id.start_service);
        Button stopService = (Button) findViewById(R.id.stop_service);
        startService.setOnClickListener(this);
        stopService.setOnClickListener(this);
        text = (TextView)findViewById(R.id.textView);
        Button download = (Button) findViewById(R.id.download);
        download.setOnClickListener(this);
//        Button bindService = (Button) findViewById(R.id.bind_service);
//        Button unbindService = (Button) findViewById(R.id.unbind_service);
//        bindService.setOnClickListener(this);
//        bindService.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.start_service:
                Intent startIntent = new Intent(this,MyService.class);
                startService(startIntent);
                break;
            case R.id.stop_service:
                Intent stopIntent = new Intent(this,MyService.class);
                stopService(stopIntent);
                break;
            case R.id.download:
                Intent bindIntent = new Intent(this,MyService.class);
                bindService(bindIntent,connection,BIND_AUTO_CREATE);
                break;
//            case R.id.bind_service:
//                Intent bindIntent = new Intent(this,MyService.class);
//                bindService(bindIntent,connection,BIND_AUTO_CREATE);
//                break;
//            case R.id.unbind_service:
//                unbindService(connection);
//                break;
            default:
                break;
        }
    }

    public void listenProgress() {
        new Thread() {
            public void run() {
                while (downloadBinder.getProgress() <= 100) {
                    int progress = downloadBinder.getProgress();
                    Message msg = handler.obtainMessage();
                    msg.arg1 = progress;
                    handler.sendMessage(msg);
                    if (progress == 100) {
                        break;
                    }
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            };
        }.start();
    }
}
