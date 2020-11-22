package com.example.asynctasktest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    ProgressBar progressBar;
    Mytask mytask;
    TextView text,addText;

    private class Mytask extends AsyncTask<Void,Integer,Boolean>{
        @Override
        protected void onPreExecute() {
            text.setText("load...");
            progressBar.setProgress(0);
        }

        @Override
        protected Boolean doInBackground(Void ... parms) {
            Log.i("MainActivity", "当前线程id: "+Thread.currentThread().getId());
            Log.i("MainActivity", "主线程id: "+getMainLooper().getThread().getId());
            try {
                int count = 0;
                int length = 1;
                while (count<100) {

                    count += length;
                    // 可调用publishProgress（）显示进度, 之后将执行onProgressUpdate（）
                    publishProgress(count);
                    // 模拟耗时任务
                    Thread.sleep(50);
                }
            }catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            progressBar.setProgress(values[0]);
            text.setText("loading..."+values[0]+"%");
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            text.setText("Download success!");
            if(aBoolean){
                Log.d("MainActivity","success");
            }else{
                Log.d("MainActivity","fasle");
            }
        }

        @Override
        protected void onCancelled() {
            text.setText("cancel");
            progressBar.setProgress(0);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //绑定UI组件
        setContentView(R.layout.activity_main);
        Button download = findViewById(R.id.download);
        Button cancel = findViewById(R.id.cancel);
        Button add = findViewById(R.id.addButton);
        text =(TextView)findViewById(R.id.textView);
        addText=(TextView)findViewById(R.id.addTextView);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mytask = new Mytask();
                mytask.execute();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mytask.cancel(true);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addText.setText("我在下载东西时打印，哈哈哈！");
            }
        });
    }
}
