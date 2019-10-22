package com.jason.temperature_e;


/**
 *  writer : jason
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class CurrencyExchange extends AppCompatActivity implements Runnable {
    Double rate_Dollar;
    Double rate_Euro;
    Double rate_Won;
    String updateDate;
    Handler hander;
    String todayStr;

    private static final String TAG = "CurrentActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_exchange);


        SharedPreferences sp = getSharedPreferences("jzc",MODE_PRIVATE);
        rate_Dollar = Double.valueOf(sp.getString("dollar_rate","7.2"));
        rate_Euro = Double.valueOf(sp.getString("euro_rate","1.0"));
        rate_Won = Double.valueOf(sp.getString("won_rate","1.5"));
        updateDate = sp.getString("update_date","");

        //获取当前日期
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        todayStr = sdf.format(today);

        hander = new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                if(msg.what == 5){
                    Bundle bdl = (Bundle) msg.obj;
                    Float rate1 = bdl.getFloat("dollar-rate");
                    Float rate2 = bdl.getFloat("euro-rate");
                    Float rate3 = bdl.getFloat("won-rate");
                    Log.i(TAG, "handleMessage: rate1"+ rate1);
                    Log.i(TAG, "handleMessage: rate2"+ rate2);
                    Log.i(TAG, "handleMessage: rate3"+ rate3);
                    rate_Dollar = Double.valueOf(rate1.toString());
                    rate_Euro = Double.valueOf(rate2.toString());
                    rate_Won = Double.valueOf(rate3.toString());
                    //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("jzc", Context.MODE_PRIVATE);//创建SharedPreferences对象
                    SharedPreferences.Editor editor = sharedPreferences.edit(); //需要获取SharedPreferences的编辑对象
                    editor.putString("update_date",todayStr );

                    Toast.makeText(CurrencyExchange.this,"汇率已更新",Toast.LENGTH_SHORT).show();
                }
                super.handleMessage(msg);
            }
        };
        if(!todayStr.equals(updateDate)){
            //开启子线程
             Thread t = new Thread(this);
             t.start();
        }
    }


    public void dollar(View a) {
        exchange(rate_Dollar);

    }
    public void euro(View a) {
        exchange(rate_Euro);

    }
    public void won(View a) {
        exchange(rate_Won);

    }
    public void config(View a) {
        Intent config = new Intent(this,config.class);
        config.putExtra("dollar_rate_key",rate_Dollar);
        config.putExtra("euro_rate_key",rate_Euro);
        config.putExtra("won_rate_key",rate_Won);
       // startActivity(config);
        startActivityForResult(config,1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.menu_set){
            Intent config = new Intent(this,config.class);
            config.putExtra("dollar_rate_key",rate_Dollar);
            config.putExtra("euro_rate_key",rate_Euro);
            config.putExtra("won_rate_key",rate_Won);
            // startActivity(config);
            startActivityForResult(config,1);
        } else if(item.getItemId() == R.id.open_list){
            //打开列表窗口
            // Intent RateListActivity = new Intent(this,Mylist2Activity.class);
            //startActivity(RateListActivity);
            //测试数据库
            RateItem item1 = new RateItem("aaa","234");
            RateManager manager = new RateManager(this);
            manager.add(item1);
            manager.add(new RateItem("bbb","23.6"));
            Log.i(TAG, "onOptionsItemSelected: 写入数据完毕");

            //查询所有数据
            List<RateItem> testlist = manager.listall();
            for(RateItem i : testlist){
                Log.i(TAG, "onOptionsItemSelected: 取出数据[id= "+i.getId()+"]name="+i.getCurName()+"rate="+i.getCurRate());

            }

        }
        return super.onOptionsItemSelected(item);
    }

    public void exchange(double rate){
        EditText firsPrice = findViewById(R.id.edit1);
        TextView finalPrice = findViewById(R.id.showmoney);
        double price = rate* Float.parseFloat((firsPrice.getText().toString()));
        finalPrice.setText(String.format("%.2f",price));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==1 && resultCode==2){
            Bundle bundle = data.getExtras();
            rate_Dollar = bundle.getDouble("dollar_key",0.1f);
            rate_Euro = bundle.getDouble("euro_key",0.1f);
            rate_Won = bundle.getDouble("won_key",0.1f);

            Log.i(TAG, "onActivityResult: ");
            Log.i(TAG, "dollar: " + rate_Dollar);
            Log.i(TAG, "euro: "+ rate_Euro);

        }
    }




    @Override
    public void run() {
        Log.i(TAG, "run: run.........");
        for (int i =1;i<3;i++){
            Log.i(TAG, "run: i = " +i);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

        Bundle bundle = new Bundle();

            Document doc = null;
            try {

                doc = Jsoup.connect("http://www.usd-cny.com/bankofchina.htm").get();
                Log.i(TAG, "run: "+doc.title());

                Elements tables = doc.getElementsByTag("table");
                Element table1 = tables.get(0);
             //   Log.i(TAG, "run: table1"+table1);

                Elements tds = table1.getElementsByTag("td");

                SharedPreferences sp = getSharedPreferences("get_rate",MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                int j=1;
                for (int i=0 ; i <tds.size();i+=6){
                    Element td_name = tds.get(i);
                    Element td_moeny = tds.get(i+5);
                    //   Log.i(TAG, "run: "+td_name.text()+"==>"+td_moeny.text());
                    String name = td_name.text();
                    String exchange = td_moeny.text();

                    //传递外币汇率到mylist2Activity

                    editor.putString("item"+j ,name);
                    editor.putString("detail"+j,exchange);
                    Log.i(TAG, "run: name: " + name );
                    Log.i(TAG, "run: name: " + exchange );
                    j++;

                    if(name.equals("美元")){
                        Log.i(TAG, "run: dollar"+exchange);
                        bundle.putFloat("dollar-rate", 100f/Float.parseFloat(td_moeny.text()));
                    } else if(name.equals("欧元")){
                        Log.i(TAG, "run: euro"+ exchange);
                        bundle.putFloat("euro-rate", 100f/Float.parseFloat(td_moeny.text()));
                    } else if(name.equals("韩元")){
                        Log.i(TAG, "run: won"+exchange);
                        bundle.putFloat("won-rate", 100f/Float.parseFloat(td_moeny.text()));
                    }
                }
                editor.commit();

                //obtain the message object, to return to the main thread
                Message msg = hander.obtainMessage(5);
                // what == 5
                msg.obj = bundle;
                hander.sendMessage(msg);


            } catch (IOException e1) {
                e1.printStackTrace();
            }


    }

    private String inputStram2String(InputStream inputStream) throws IOException {
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();

    }
}
