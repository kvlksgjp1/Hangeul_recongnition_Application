package com.example.hongseonggi.chatting_client;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ColorItemView extends LinearLayout {

   TextView textView;
    public ColorItemView(Context context) {
        super(context);
        init(context);
    }

    public ColorItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context){
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.color,this,true);
        textView = (TextView)findViewById(R.id.textView);
    }

    public void setColor(int color){
        textView.setBackgroundColor(color);
    }
}
