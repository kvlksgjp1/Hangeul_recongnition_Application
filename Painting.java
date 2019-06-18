import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Painting extends AppCompatActivity {


    private DrawView drawing;
    private ImageView[] colorbtn;
    private ImageView reset;
    private ImageView save;
    private LinearLayout layout;

    @Override // 다이얼로그 띄워준 곳에서 받기

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 101){
            if(data != null){
                int color = data.getIntExtra("color",0);
                drawing.setColor(color);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw);

        layout = (LinearLayout)findViewById(R.id.LinearLayout);
        drawing = (DrawView) findViewById(R.id.drawingView);
        reset = (ImageView) findViewById(R.id.reset);
        save = (ImageView) findViewById(R.id.save);

        colorbtn = new ImageView[4];
        colorbtn[0] = (ImageView) findViewById(R.id.colorselect);
        colorbtn[1] = (ImageView) findViewById(R.id.up);
        colorbtn[2] = (ImageView) findViewById(R.id.down);


        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawing.reset();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //drawing.save(Painting.this);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Painting.this);
                alertDialogBuilder.setTitle("선택하세요!!");
                alertDialogBuilder.setMessage("그림 or 글자 ?").setCancelable(false)
                        .setPositiveButton("그림",
                                new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                    drawing.save(Painting.this);
                                    PrintWriter out = new PrintWriter(MyGlobals.getInstance().getNetworkWriter(), true);
                                    File file = new File(Environment.getExternalStorageDirectory() + "/ctos_image.png");
                                    long file_length = file.length();
                                    String nick_name = MyGlobals.getInstance().getNick_name();
                                    out.println(nick_name + "@image_send_client_to_server@ctos_image.png@" + String.valueOf(file_length));

                                    try {
                                        FileInputStream fis = new FileInputStream(file);
                                        DataInputStream dis = new DataInputStream(fis);
                                        DataOutputStream dos = new DataOutputStream(MyGlobals.getInstance().getSocket().getOutputStream());

                                        int len;
                                        int total_len = 0;
                                        int size = 1024;
                                        byte[] data = new byte[size];
                                        while ((len = dis.read(data)) != -1) {
                                            total_len += len;
                                            dos.write(data,0,len);
                                            dos.flush();
                                            if (total_len >= file_length) {
                                                break;
                                            }
                                        }

                                    }catch (Exception e) { }


                                    out.println(nick_name + "@image_send_server_to_client@ctos_image.png@");
                                    Painting.this.finish();

                                }
                                }).setNegativeButton("글자", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        drawing.save(Painting.this);

                                        PrintWriter out = new PrintWriter(MyGlobals.getInstance().getNetworkWriter(), true);
                                        File file = new File(Environment.getExternalStorageDirectory() + "/ctos_image.png");
                                        long file_length = file.length();
                                        String nick_name = MyGlobals.getInstance().getNick_name();
                                        out.println(nick_name + "@image_send_client_to_server_hangeul@ctos_image.png@" + String.valueOf(file_length));

                                        try {
                                            FileInputStream fis = new FileInputStream(file);
                                            DataInputStream dis = new DataInputStream(fis);
                                            DataOutputStream dos = new DataOutputStream(MyGlobals.getInstance().getSocket().getOutputStream());

                                            int len;
                                            int total_len = 0;
                                            int size = 1024;
                                            byte[] data = new byte[size];
                                            while ((len = dis.read(data)) != -1) {
                                                total_len += len;
                                                dos.write(data,0,len);
                                                dos.flush();
                                                if (total_len >= file_length) {
                                                    break;
                                                }
                                            }

                                        }catch (Exception e) { }

                                        //Intent intent = new Intent(getApplicationContext(), Chatting.class);
                                        //startActivity(intent);
                                        Painting.this.finish();

                                        }
                                });
                                        AlertDialog alertDialog = alertDialogBuilder.create();
                                        alertDialog.show();
                                    }
        });
    }
    public void onClick(View v)
    {
        switch(v.getId())
        {
            case R.id.colorselect:
                Intent intent = new Intent(getApplicationContext(),ColorDialog.class);
                startActivityForResult(intent,101);
                break;
            case R.id.up:
                drawing.setLineWith(drawing.getLineWith()+1);
                Toast.makeText(getApplicationContext(),"선 굵기 : "+drawing.getLineWith(),Toast.LENGTH_SHORT).show();
                break;
            case R.id.down:
                if(drawing.getLineWith()>1)
                    drawing.setLineWith(drawing.getLineWith()-1);
                Toast.makeText(getApplicationContext(),"선 굵기 : "+drawing.getLineWith(),Toast.LENGTH_SHORT).show();
        }
    }
}
