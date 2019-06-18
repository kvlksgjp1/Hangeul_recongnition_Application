package com.example.hongseonggi.chatting_client;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;

public class ColorDialog extends AppCompatActivity {
    GridView gridView;
    ColorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_dialog);

        gridView = (GridView)findViewById(R.id.gridView);

        adapter = new ColorAdapter();
        adapter.addItem(0xff000000);
        adapter.addItem(0xffff0000);
        adapter.addItem(0xffffff00);
        adapter.addItem(0xff00ff00);
        adapter.addItem(0xff00ffff);
        adapter.addItem(0xff0000ff);
        adapter.addItem(0xff7f007f);
        adapter.addItem(0xff7f00ff);
        adapter.addItem(0xff7f7f00);
        adapter.addItem(0xffffff7f);
        adapter.addItem(0xffff007f);
        adapter.addItem(0xff777777);

        gridView.setAdapter(adapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int color = (Integer)adapter.getItem(position);
                Toast.makeText(getApplicationContext(),"색상을 선택하셨습니다",Toast.LENGTH_LONG).show();
                Intent intent = new Intent();
                intent.putExtra("color",color);
                setResult(Activity.RESULT_OK,intent);
            }
        });
    }

    class ColorAdapter extends BaseAdapter{
        ArrayList<Integer> items = new ArrayList<Integer>();

        @Override
        public int getCount() {
            return items.size();
        }
        public void addItem (int color){
            items.add(color);
        }
        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ColorItemView view = null;
            if(convertView ==null){
                view = new ColorItemView(getApplicationContext());
            }else
            {
                view = (ColorItemView)convertView;
            }
            int color = (Integer)items.get(position);
            view.setColor(color);
            return view;

        }
    }
}
