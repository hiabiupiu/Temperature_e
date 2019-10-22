package com.jason.temperature_e;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class BasketActivity extends AppCompatActivity {

    private static final String TAG = "BasketActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
    }

    public void click1(View a) {
     //   Log.i(TAG, "click1: kkkkkkkkkkkkkkkkkk");

        show(1);

    }
    public void click2(View a) {


        show(2);

    }
    public void click3(View a) {


        show(3);

    }

    public void click1_b(View a) {
        //   Log.i(TAG, "click1: kkkkkkkkkkkkkkkkkk");

        show2(1);

    }
    public void click2_b(View a) {


        show2(2);

    }
    public void click3_b(View a) {


        show2(3);

    }
    public void reset(View a) {
        TextView score = findViewById(R.id.scoreA);
        TextView score2 = findViewById(R.id.scoreB);
        score.setText("0");
        score2.setText("0");



    }

    public  void  show(int i){
        TextView score = findViewById(R.id.scoreA);
        int Oldscore = Integer.parseInt(score.getText().toString());
        String newscore = String.valueOf(Oldscore + i);
        score.setText(newscore);
    }
    public  void  show2(int i){
        TextView score = findViewById(R.id.scoreB);
        int Oldscore = Integer.parseInt(score.getText().toString());
        String newscore = String.valueOf(Oldscore + i);
        score.setText(newscore);
    }



}
