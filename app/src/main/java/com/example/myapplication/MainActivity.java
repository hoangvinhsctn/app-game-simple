package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class MainActivity extends AppCompatActivity {
    private ImageView imgGoc, imgNhan;
    public static ArrayList<String> arrayName;
    private TextView tv1,tvTimer;
    private Button btnStart;
    private int REQUEST_CODE_IMAGE = 100;
    private String ten_hinh_goc, ten_hinh_nhan;
    private int score = 100;
    private CountDownTimer timer;
    private LinearLayout layout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        anhXa();

        // Lấy mảng từ string.xml
        String[] mang_ten = getResources().getStringArray(R.array.list_name);
        arrayName = new ArrayList<>(Arrays.asList(mang_ten));

        layout.setVisibility(View.GONE);

        // Trộn mảng
        shuffle_Array();

        imgNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(MainActivity.this,MainActivity2.class),REQUEST_CODE_IMAGE);
            }
        });

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout.setVisibility(View.VISIBLE);
                resolve_time(15);
                btnStart.setVisibility(View.INVISIBLE);
                imgNhan.setImageResource(R.drawable.question_mark);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null)
        {
            ten_hinh_nhan = data.getStringExtra("ten_hinh");
            int idHinh = getResources().getIdentifier(ten_hinh_nhan,"drawable", getPackageName());
            imgNhan.setImageResource(idHinh);

             // Xử lý khi hình trả về chính xác
            if(ten_hinh_goc.equals(ten_hinh_nhan))
            {
                Toast.makeText(this, "Bạn đã lựa chọn chính xác\n bạn được cộng 10 điểm ", Toast.LENGTH_SHORT).show();
                score += 10;
                tv1.setText(score + "");
            }else
            {
                Toast.makeText(this, "Bạn đã lựa chọn sai\n bạn bị trừ 5 điểm", Toast.LENGTH_SHORT).show();
                score -= 5;
                tv1.setText(score + "");
                timer.cancel();
                resolve_time(15);
                shuffle_Array();
            }
        }

        if(resultCode == RESULT_CANCELED)
        {
            Toast.makeText(this, "Bạn định ăn gian hả\n trừ 15 điểm ", Toast.LENGTH_SHORT).show();
            score -= 15;
            tv1.setText(score + "");
            timer.cancel();
            resolve_time(15);
            shuffle_Array();
            check_score();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // Xử lý menu reload
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_function,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_reload)
        {
            timer.cancel();
            Alert();
        }
        return super.onOptionsItemSelected(item);
    }

    private void Alert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Cảnh báo");
        alertDialog.setIcon(R.drawable.baseline_warning_24);
        alertDialog.setMessage("Bạn có muốn chơi lại?");
        alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Trộn mảng
                shuffle_Array();
                score = 100;
                tv1.setText(score + "");
                layout.setVisibility(View.GONE);
                btnStart.setVisibility(View.VISIBLE);
                timer.cancel();
                tvTimer.setText("");
            }
        });

        alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                timer.start();
            }
        });
        alertDialog.show();
    }

    private void resolve_time(int thoi_gian){
        timer = new CountDownTimer(thoi_gian * 1000, 1000) {
            @Override
            public void onTick(long l) {
                long seconds = (l / 1000) % 60;
                tvTimer.setText(String.valueOf(seconds));
                if(ten_hinh_goc.equals(ten_hinh_nhan))
                {
                    timer.cancel();
                    timer.onFinish();
                    shuffle_Array();
                }
            }

            @Override
            public void onFinish() {
                // Tạo trễ khoảng 2 giây
                new CountDownTimer(2000,100)
                {
                    @Override
                    public void onTick(long l) {

                    }

                    @Override
                    public void onFinish() {

                    }
                }.start();
                if(ten_hinh_nhan == null || ten_hinh_goc.equals(ten_hinh_nhan) != true)
                {
                    shuffle_Array();
                    Toast.makeText(MainActivity.this, "Bạn đã không lựa chọn \n bạn bị trừ 20 điểm", Toast.LENGTH_SHORT).show();
                    score -= 20;
                    tv1.setText(score + "");
                }
                if(score <= 0)
                {
                    timer.cancel();
                    tvTimer.setText("");
                    score = 0;
                    tv1.setText(String.valueOf(score));
                    Alert();
                }else
                    resolve_time(15);
            }
        }.start();
    }

    // Kiểm tra điểm của người chơi
    private void check_score(){
        if(score <= 0)
        {
            timer.cancel();
            tvTimer.setText("");
            score = 0;
            tv1.setText(String.valueOf(score));
            Alert();
        }
    }

    // Hàm trộn mảng
    private void shuffle_Array (){
        Collections.shuffle(arrayName);
        // Lấy id Hình từ drawable
        ten_hinh_goc = arrayName.get(5);
        int idHinh = getResources().getIdentifier(arrayName.get(5),"drawable",getPackageName());
        imgGoc.setImageResource(idHinh);
    }

    // Hàm ánh xạ các phần tử
    private void anhXa(){
        imgGoc = (ImageView) findViewById(R.id.imageViewGoc);
        imgNhan = (ImageView) findViewById(R.id.imageViewNhan);
        tv1 = (TextView) findViewById(R.id.textViewScore);
        tvTimer = (TextView) findViewById(R.id.textViewTimer);
        btnStart = (Button) findViewById(R.id.buttonStart);
        layout = (LinearLayout) findViewById(R.id.linearHinh);
    }
}