package com.jason.temperature_e;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class config extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);
        Intent intent = getIntent();
        double dollar = intent.getDoubleExtra("dollar_rate_key",0.0f);
        double euro = intent.getDoubleExtra("euro_rate_key",0.0f);
        double won= intent.getDoubleExtra("won_rate_key",0.0f);

        EditText t1 = findViewById(R.id.show_dollar);
        t1.setText(String.format("%.2f",dollar));
        EditText t2 = findViewById(R.id.show_euro);
        t2.setText(String.format("%.2f",euro));
        EditText t3 = findViewById(R.id.show_won);
        t3.setText(String.format("%.2f",won));
    }

    public  void submit(View btn){
        EditText t1 = findViewById(R.id.show_dollar);
        EditText t2 = findViewById(R.id.show_euro);
        EditText t3 = findViewById(R.id.show_won);
        double newdollar = Double.parseDouble(t1.getText().toString());
        double neweuro = Double.parseDouble(t2.getText().toString());
        double newwon= Double.parseDouble(t3.getText().toString());

        //将数据保存在xml文件中
        SharedPreferences.Editor editor = getEditor(neweuro, newdollar, newwon);
        editor.commit();


        //将数据打包参数回传
        Intent intent = getIntent();
        Bundle bdl = new Bundle();
        bdl.putDouble("dollar_key",newdollar);
        bdl.putDouble("euro_key",neweuro);
        bdl.putDouble("won_key",newwon);

        intent.putExtras(bdl);
        setResult(2,intent);

        finish();

    }

    public SharedPreferences.Editor getEditor(double neweuro, double newdollar, double newwon) {
        SharedPreferences sharedPreferences = getSharedPreferences("jzc", Context.MODE_PRIVATE);//创建SharedPreferences对象
        SharedPreferences.Editor editor = sharedPreferences.edit(); //需要获取SharedPreferences的编辑对象
        editor.putString("dollar_rate", String.format("%.2f",newdollar)); //向preferences写入数据：
        editor.putString("euro_rate", String.format("%.2f",neweuro));
        editor.putString("won_rate", String.format("%.2f",newwon));
        return editor;
    }
}
