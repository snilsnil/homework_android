package com.appsnipp.homedesign2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.namespace.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class User extends Fragment {
    CardView key, weight, bmi;
    TextView keyt, weightt, bmit;
    DB db;
    SQLiteDatabase sql;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.user, container, false);

        key = v.findViewById(R.id.key);
        weight = v.findViewById(R.id.weight);
        bmi = v.findViewById(R.id.bmi);

        keyt = v.findViewById(R.id.keytext);
        weightt = v.findViewById(R.id.weighttext);
        bmit = v.findViewById(R.id.bmitext);



        db=new DB(getActivity());
        sql = db.getWritableDatabase();

        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        try {
            calendar.setTime(sdf.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        while (true) {
            // currentDate를 사용하여 DB에서 날짜 확인 및 처리
            Cursor cursor = sql.rawQuery("SELECT day FROM user WHERE day='" + currentDate + "'", null);
            if (cursor.getCount() > 0) {
                break; // 날짜가 있는 경우 루프 종료
            }
            cursor.close();

            calendar.add(Calendar.DAY_OF_YEAR, -1);
            currentDate = sdf.format(calendar.getTime());
        }
        Cursor cursor = sql.rawQuery("SELECT day FROM user", null);
        if (cursor.moveToFirst()) {
            String dayValue = cursor.getString(0);
            if (dayValue == null || dayValue.isEmpty() || !dayValue.equals(currentDate)) {
                keyt.setText("-");
                weightt.setText("-");
                bmit.setText("-");
            } else {
                Cursor ckey = sql.rawQuery("SELECT k FROM user WHERE day='" + dayValue + "';", null);

                if (ckey.moveToFirst()) {
                    int keyValue = ckey.getInt(0);
                    keyt.setText(keyValue + " cm");
                }
                ckey.close();

                Cursor cweight = sql.rawQuery("SELECT weight FROM user WHERE day='" + dayValue + "';", null);
                if (cweight.moveToFirst()) {
                    int weightValue = cweight.getInt(0);
                    weightt.setText(weightValue + " kg");
                }
                cweight.close();

                Cursor cbmi = sql.rawQuery("SELECT bmi FROM user WHERE day='" + dayValue + "';", null);
                if (cbmi.moveToFirst()) {
                    float bmiValue = cbmi.getFloat(0);
                    bmit.setText(String.valueOf(bmiValue));
                }
                cbmi.close();
            }
        }

        key.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dia = new AlertDialog.Builder(getActivity());
                dia.setTitle("키를 입력하세요");

                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.diakey, null);
                dia.setView(dialogView);

                EditText dkey = dialogView.findViewById(R.id.dkey);

                dia.setPositiveButton("확인", (dialogInterface, i) -> {
                    String rekey = dkey.getText().toString();
                    if (TextUtils.isEmpty(rekey) && !isInteger(rekey)) {
                        AlertDialog.Builder retryDia = new AlertDialog.Builder(getActivity());
                        retryDia.setTitle("키를 올바르게 입력하세요");
                        retryDia.setPositiveButton("확인", (dialogInterface1, i1) -> showKey());
                        retryDia.show();
                    } else {
                        try{
                            int value=Integer.parseInt(rekey);
                            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                            try {
                                calendar.setTime(sdf.parse(currentDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            while (true) {
                                // currentDate를 사용하여 DB에서 날짜 확인 및 처리
                                Cursor cursor = sql.rawQuery("SELECT day FROM user WHERE day='" + currentDate + "'", null);
                                if (cursor.getCount() > 0) {
                                    break; // 날짜가 있는 경우 루프 종료
                                }
                                cursor.close();

                                calendar.add(Calendar.DAY_OF_YEAR, -1);
                                currentDate = sdf.format(calendar.getTime());
                            }
                            Cursor cursor = sql.rawQuery("SELECT day FROM user", null);
                            if (cursor.moveToFirst()) {
                                String dayValue = cursor.getString(0);
                                if (dayValue == null || dayValue.isEmpty()) {
                                    sql.execSQL("INSERT INTO user VALUES ('" + currentDate + "'," + value + ", 0, 0);");
                                } else {
                                    sql.execSQL("UPDATE user SET k=" + value + " WHERE day='" + dayValue + "';");
                                    cursor.close();

                                    Cursor cweight = sql.rawQuery("SELECT weight FROM user", null);
                                    if(cweight.moveToFirst()){
                                        float weightValue = Float.parseFloat(cweight.getString(0));
                                        if(weightValue!=0){
                                            float bmi = weightValue / (((float) value/100) * ((float) value/100));
                                            bmi = Math.round(bmi * 10) / 10.0f;
                                            sql.execSQL("UPDATE user SET bmi=" + bmi + " WHERE day='" + dayValue + "';");
                                            bmit.setText(String.valueOf(bmi));
                                        }
                                    }
                                    cweight.close();
                                }
                            } else {
                                sql.execSQL("INSERT INTO user VALUES ('" + currentDate + "'," + value + ", 0, 0);");
                            }
                            cursor.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        keyt.setText(rekey + " cm");
                    }
                });

                dia.show();
            }
        });


        weight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dia = new AlertDialog.Builder(getActivity());
                dia.setTitle("몸무게를 입력하세요");

                LayoutInflater inflater = requireActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.diaweight, null);
                dia.setView(dialogView);

                EditText dweight = dialogView.findViewById(R.id.dweight);

                dia.setPositiveButton("확인", (dialogInterface, i) -> {
                    String rekey = dweight.getText().toString();
                    if (TextUtils.isEmpty(rekey) && !isInteger(rekey)) {
                        AlertDialog.Builder retryDia = new AlertDialog.Builder(getActivity());
                        retryDia.setTitle("몸무게를 올바르게 입력하세요");
                        retryDia.setPositiveButton("확인", (dialogInterface1, i1) -> showWeight());
                        retryDia.show();
                    } else {
                        try{
                            int value=Integer.parseInt(rekey);
                            String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                            Calendar calendar = Calendar.getInstance();
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                            try {
                                calendar.setTime(sdf.parse(currentDate));
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            while (true) {
                                // currentDate를 사용하여 DB에서 날짜 확인 및 처리
                                Cursor cursor = sql.rawQuery("SELECT day FROM user WHERE day='" + currentDate + "'", null);
                                if (cursor.getCount() > 0) {
                                    break; // 날짜가 있는 경우 루프 종료
                                }
                                cursor.close();

                                calendar.add(Calendar.DAY_OF_YEAR, -1);
                                currentDate = sdf.format(calendar.getTime());
                            }
                            Cursor cursor = sql.rawQuery("SELECT day FROM user", null);
                            if (cursor.moveToFirst()) {
                                String dayValue = cursor.getString(0);
                                if (dayValue == null || dayValue.isEmpty()) {
                                    sql.execSQL("INSERT INTO user VALUES ('" + currentDate + "'," + value + ", 0, 0);");
                                } else {
                                    sql.execSQL("UPDATE user SET weight=" + value + " WHERE day='" + dayValue + "';");
                                    cursor.close();

                                    Cursor ckey = sql.rawQuery("SELECT k FROM user", null);
                                    if(ckey.moveToFirst()){
                                        float keyValue = Float.parseFloat(ckey.getString(0));
                                        if(keyValue!=0){
                                            float bmi = value / (((float) keyValue/100) * ((float) keyValue/100));
                                            bmi = Math.round(bmi * 10) / 10.0f;
                                            sql.execSQL("UPDATE user SET bmi=" + bmi + " WHERE day='" + dayValue + "';");
                                            bmit.setText(String.valueOf(bmi));
                                        }
                                    }
                                    ckey.close();
                                }
                            } else {
                                sql.execSQL("INSERT INTO user VALUES ('" + currentDate + "'," + value + ", 0, 0);");
                            }
                            cursor.close();
                        }catch(Exception e){
                            e.printStackTrace();
                        }
                        weightt.setText(rekey + " kg");
                    }
                });

                dia.show();
            }
        });





        return v;
    }

    private void showKey() {
        AlertDialog.Builder dia = new AlertDialog.Builder(getActivity());
        dia.setTitle("키를 입력하세요");

        // 커스텀 레이아웃을 인플레이션하여 AlertDialog에 설정
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.diakey, null);
        dia.setView(dialogView);

        EditText dkey = dialogView.findViewById(R.id.dkey);

        dia.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String rekey = dkey.getText().toString();
                if (TextUtils.isEmpty(rekey)&& !isInteger(rekey)) {
                    AlertDialog.Builder retryDia = new AlertDialog.Builder(getActivity());
                    retryDia.setTitle("키를 올바르게 입력하세요");
                    retryDia.setPositiveButton("확인", (dialogInterface1, i1) -> showKey());
                    retryDia.show();
                } else {
                    try{
                        int value=Integer.parseInt(rekey);
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                        try {
                            calendar.setTime(sdf.parse(currentDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        while (true) {
                            // currentDate를 사용하여 DB에서 날짜 확인 및 처리
                            Cursor cursor = sql.rawQuery("SELECT day FROM user WHERE day='" + currentDate + "'", null);
                            if (cursor.getCount() > 0) {
                                break; // 날짜가 있는 경우 루프 종료
                            }
                            cursor.close();

                            calendar.add(Calendar.DAY_OF_YEAR, -1);
                            currentDate = sdf.format(calendar.getTime());
                        }
                        Cursor cursor = sql.rawQuery("SELECT day FROM user", null);
                        if (cursor.moveToFirst()) {
                            String dayValue = cursor.getString(0);
                            if (dayValue == null || dayValue.isEmpty()) {
                                sql.execSQL("INSERT INTO user VALUES ('" + currentDate + "'," + value + ", 0, 0);");
                            } else {
                                sql.execSQL("UPDATE user SET k=" + value + " WHERE day='" + dayValue + "';");
                                cursor.close();

                                Cursor cweight = sql.rawQuery("SELECT weight FROM user", null);
                                if(cweight.moveToFirst()){
                                    float weightValue = Float.parseFloat(cweight.getString(0));
                                    if(weightValue!=0){
                                        float bmi = weightValue / (((float) value/100) * ((float) value/100));
                                        bmi = Math.round(bmi * 10) / 10.0f;
                                        sql.execSQL("UPDATE user SET bmi=" + bmi + " WHERE day='" + dayValue + "';");
                                        bmit.setText(String.valueOf(bmi));
                                    }
                                }
                                cweight.close();
                            }
                        } else {
                            sql.execSQL("INSERT INTO user VALUES ('" + currentDate + "'," + value + ", 0, 0);");
                        }
                        cursor.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    keyt.setText(rekey + " cm");
                }
            }
        });

        dia.show();
    }


    private void showWeight() {
        AlertDialog.Builder dia = new AlertDialog.Builder(getActivity());
        dia.setTitle("몸무게를 입력하세요");

        // 커스텀 레이아웃을 인플레이션하여 AlertDialog에 설정
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.diaweight, null);
        dia.setView(dialogView);

        EditText dweight = dialogView.findViewById(R.id.dweight);

        dia.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String rekey = dweight.getText().toString();
                if (TextUtils.isEmpty(rekey) && !isInteger(rekey)) {
                    AlertDialog.Builder retryDia = new AlertDialog.Builder(getActivity());
                    retryDia.setTitle("몸무게를 올바르게 입력하세요");
                    retryDia.setPositiveButton("확인", (dialogInterface1, i1) -> showWeight());
                    retryDia.show();
                } else {
                    try{
                        int value=Integer.parseInt(rekey);
                        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                        try {
                            calendar.setTime(sdf.parse(currentDate));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        while (true) {
                            // currentDate를 사용하여 DB에서 날짜 확인 및 처리
                            Cursor cursor = sql.rawQuery("SELECT day FROM user WHERE day='" + currentDate + "'", null);
                            if (cursor.getCount() > 0) {
                                break; // 날짜가 있는 경우 루프 종료
                            }
                            cursor.close();

                            calendar.add(Calendar.DAY_OF_YEAR, -1);
                            currentDate = sdf.format(calendar.getTime());
                        }
                        Cursor cursor = sql.rawQuery("SELECT day FROM user", null);
                        if (cursor.moveToFirst()) {
                            String dayValue = cursor.getString(0);
                            if (dayValue == null || dayValue.isEmpty()) {
                                sql.execSQL("INSERT INTO user VALUES ('" + currentDate + "'," + value + ", 0, 0);");
                            } else {
                                sql.execSQL("UPDATE user SET weight=" + value + " WHERE day='" + dayValue + "';");
                                cursor.close();

                                Cursor ckey = sql.rawQuery("SELECT k FROM user", null);
                                if(ckey.moveToFirst()){
                                    float keyValue = Float.parseFloat(ckey.getString(0));
                                    if(keyValue!=0){
                                        float bmi = value / (((float) keyValue/100) * ((float) keyValue/100));
                                        bmi = Math.round(bmi * 10) / 10.0f;
                                        sql.execSQL("UPDATE user SET bmi=" + bmi + " WHERE day='" + dayValue + "';");
                                        bmit.setText(String.valueOf(bmi));
                                    }
                                }
                                ckey.close();
                            }
                        } else {
                            sql.execSQL("INSERT INTO user VALUES ('" + currentDate + "'," + value + ", 0, 0);");
                        }
                        cursor.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    weightt.setText(rekey + " kg");
                }

            }
        });

        dia.show();
    }

    private boolean isInteger(String str) {
        try {
            if (str.length() > 3) {
                return false;
            }
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
