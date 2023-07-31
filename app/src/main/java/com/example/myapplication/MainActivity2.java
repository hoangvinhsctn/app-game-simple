package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;

import java.util.Collections;

public class MainActivity2 extends Activity {
    private TableLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        tableLayout = (TableLayout) findViewById(R.id.tableImage);

        int soDong = 6;
        int soCot = 3;

        //Trộn mảng
        Collections.shuffle(MainActivity.arrayName);

        // Thêm các dòng và các cột vào TableLayout
        for(int i = 1; i <= soDong; i++){
            TableRow tableRow = new TableRow(this);
            for(int j = 1; j <= soCot; j++)
            {
                ImageView imageView = new ImageView(this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams((int) convertDpToPixel(100,this), (int) convertDpToPixel(180,this));
                imageView.setLayoutParams(layoutParams);
                int vitri = soCot * (i - 1) + (j - 1);
                int idHinh = getResources().getIdentifier(MainActivity.arrayName.get(vitri),"drawable",getPackageName());
                imageView.setImageResource(idHinh);
                tableRow.addView(imageView);

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.putExtra("ten_hinh",MainActivity.arrayName.get(vitri));
                        setResult(RESULT_OK,intent);
                        finish();
                    }
                });
            }
            tableLayout.addView(tableRow);
        }
    }

    //Convert dp to pixel
    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return px;
    }
}

