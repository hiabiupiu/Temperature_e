package com.jason.temperature_e;

import android.app.ListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class Mylist2Activity extends ListActivity {
    Handler handler;
    private ArrayList<HashMap<String,String>> listItems;
    private SimpleAdapter listItemAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_mylist2);
        initListView();
        this.setListAdapter(listItemAdapter);
    }

    private void initListView(){
        //取出各国汇率
        SharedPreferences sp = getSharedPreferences("get_rate",0);

        listItems = new ArrayList<HashMap<String,String>>();
        for (int i=1; i<sp.hashCode(); i++){
            HashMap<String,String>map = new HashMap<String, String>();
            map.put("ItemTitle",sp.getString("item"+i,null));
            map.put("ItemDetail",sp.getString("detail"+i,"0.00f"));
            listItems.add(map);
        }
        //adapter Item 和list 元素对应
        listItemAdapter = new SimpleAdapter(this,listItems,
                R.layout.activity_mylist2,
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle,R.id.itemDetail});

    }
}
