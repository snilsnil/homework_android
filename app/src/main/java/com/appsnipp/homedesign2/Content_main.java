package com.appsnipp.homedesign2;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.namespace.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Content_main  extends Fragment {
    TextView mainbmi, mainstatus;
    LinearLayout ganam, uiam, chuejangam, jeohyeolap, gohyeolap;

    DB db;
    SQLiteDatabase sql;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.content_main, container, false);

        mainbmi=v.findViewById(R.id.mainbmi);
        mainstatus=v.findViewById(R.id.mainstatus);

        ganam=v.findViewById(R.id.ganam);
        uiam=v.findViewById(R.id.uiam);
        chuejangam=v.findViewById(R.id.chuejangam);

        jeohyeolap=v.findViewById(R.id.jeohyeolap);
        gohyeolap=v.findViewById(R.id.gohyeolap);

        db=new DB(getActivity());
        sql = db.getWritableDatabase();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());



        Cursor cursor = sql.rawQuery("SELECT day FROM user", null);
        if (cursor.moveToFirst()) {
            String dayValue = cursor.getString(0);
            if (dayValue == null || dayValue.isEmpty() || !dayValue.equals(currentDate)) {
                mainbmi.setText("-");
                mainstatus.setText("-");
            } else {
                Cursor cbmi = sql.rawQuery("SELECT bmi FROM user WHERE day='" + dayValue + "';", null);
                if (cbmi.moveToFirst()) {
                    float bmiValue = cbmi.getFloat(0);
                    if(bmiValue<18.5){
                        mainstatus.setText("저체중");
                    } else if(bmiValue>=18.5&&bmiValue<=22.9){
                        mainstatus.setText("정상 체중");
                    } else if(bmiValue>=23.0&&bmiValue<=24.9){
                        mainstatus.setText("과체중");
                    } else if (bmiValue>=25.0) {
                        mainstatus.setText("비만");
                    }
                    mainbmi.setText(String.valueOf(bmiValue));
                }
                cbmi.close();
            }
        }


        ganam.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), Ganam_txt.class);
            startActivity(intent);
        });

        uiam.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), Uiam_txt.class);
            startActivity(intent);

        });
        chuejangam.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(), Chuejangam_txt.class);
            startActivity(intent);
        });
        jeohyeolap.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(), Jeohyeolap_txt.class);
            startActivity(intent);
        });
        gohyeolap.setOnClickListener(view ->{
            Intent intent = new Intent(getActivity(), Gohyeolap_txt.class);
            startActivity(intent);
        });



        return v;
    }
    public class DB extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "user.db";
        private static final int DATABASE_VERSION = 1;

        public DB(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // 데이터베이스 테이블 생성 및 초기 설정
            db.execSQL("create table user(day date primary key, k number, weight number, bmi float)");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // 데이터베이스 업그레이드 처리
            db.execSQL("drop table if exists db");
            onCreate(db);
        }
    }
}
