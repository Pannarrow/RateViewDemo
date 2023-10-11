package com.pan.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private RateView rateView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rateView = findViewById(R.id.rate_view);
        rateView.setRateType(1);
        rateView.setOnRateListener(new RateView.OnRateListener() {
            @Override
            public void onRate(int rateType) {
                switch (rateType) {
                    case 0:
                        Toast.makeText(MainActivity.this, "未评价", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        Toast.makeText(MainActivity.this, "赞", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(MainActivity.this, "踩", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 防止动画进行过程中关闭页面，导致泄漏
        rateView.cancelAnim();
    }
}