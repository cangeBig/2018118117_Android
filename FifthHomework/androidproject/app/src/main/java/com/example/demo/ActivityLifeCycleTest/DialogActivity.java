package com.example.demo.ActivityLifeCycleTest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.demo.R;

public class DialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
    }
}
