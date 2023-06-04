package com.appsnipp.homedesign2;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.namespace.R;

public class Uiam_txt extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 타이틀 숨기기
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.uiam_text);

        // NeckstrechingActivity 초기화 작업 수행
    }
}