package com.example.chatting_client;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.os.Bundle;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Chatting extends AppCompatActivity {

    private Handler mHandler;
    private Socket socket;

    private BufferedReader networkReader;
    private BufferedWriter networkWriter;
    private static final int PICK_FROM_ALBUM =1;

    private String line;
    private String ip= "172.30.1.36";
    private int port = 9100;

    String globalLine;
    String myNickName;
    File tempFile;
    String imageString=null;

    private DrawView drawing;
    ListView listView;
    ImageView camera;
    ArrayList<String> Data ;
    ImageView ok;
    EditText editText;
    TextView textView;
    ImageView paint;
    ChattingAdapter adapter;

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FROM_ALBUM)
        {
            
            out.println(nick_name + "@image_send_server_to_client@test_image.jpg@");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        mHandler = new Handler();

        tedPermission();

        camera = (ImageView)findViewById(R.id.camera);
        paint = (ImageView) findViewById(R.id.paint);
        ok = (ImageView) findViewById(R.id.ok);
        editText = (EditText) findViewById(R.id.editText);
        listView = (ListView) findViewById(R.id.listView);

        connect_and_check.start();

        AlertDialog.Builder ad = new AlertDialog.Builder(Chatting.this);

        ad.setTitle("NICK NAME?");
        final EditText d_et = new EditText(Chatting.this);
        ad.setView(d_et);
        ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                PrintWriter out = new PrintWriter(networkWriter, true);
                myNickName = d_et.getText().toString();
                MyGlobals.getInstance().setNick_name(myNickName);
                out.println(myNickName);
            }
        });

        ad.show();


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                startActivityForResult(intent, PICK_FROM_ALBUM);


            }
        });


        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString() != null && !editText.getText().toString().equals("")) {
                    PrintWriter out = new PrintWriter(networkWriter, true);
                    out.println(myNickName + "@normal_chatting@" + editText.getText().toString());
                    editText.setText("");
                }
            }
        });



        paint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Painting.class);
                startActivity(intent);
            }
        });


    }

    public void tedPermission(){
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                // 권한 요청 성공

            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                // 권한 요청 실패
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();

    }

    public class ChattingAdapter extends BaseAdapter {
        ArrayList<Useritem> items = new ArrayList<Useritem>();

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void addItem(Useritem item) {
            items.add(item);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            UseritemView view =null;
            if(convertView == null)
            {
                view = new UseritemView(getApplicationContext());
            }else{
                view = (UseritemView)convertView;
            }
            Useritem item = items.get(position);

            view.setImage(item.getResId());
            view.setName(item.getName());
            view.setContents(item.getContents());
            view.setUri(item.getUri());
            return view;
        }
    }

}

