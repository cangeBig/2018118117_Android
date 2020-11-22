package com.example.servicetest;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {

    private DownloadBinder mBinder = new DownloadBinder();
    private int progress;

    public void startDownload(){
        int rate = 0;
        while (rate < 100){
            try {
                Thread.sleep(500);
                rate= rate+5;
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            this.progress = rate;
        }
    }

    class DownloadBinder extends Binder{
        //开始下载
        public void start(){
            progress =0;
            new Thread(){
                public void run(){
                    startDownload();
                }
            }.start();
        }

        //返回进度
        public int getProgress(){
            return progress;
        }
    }
    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyService","onCreate executed");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("MyService","onStartCommand executed");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("MyService","onDestory executed");
    }

}
