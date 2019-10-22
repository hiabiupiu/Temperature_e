package com.jason.temperature_e;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.linear);
    }


    public void click(View a) {
        EditText t = findViewById(R.id.EditText);
        TextView result = findViewById(R.id.ViewResult);
        Double temperC = Double.valueOf(t.getText().toString());
        Double temperF = temperC*9/5+32.0;
        result.setText(temperF.toString());

    }

}