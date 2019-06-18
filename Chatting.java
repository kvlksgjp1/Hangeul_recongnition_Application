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


    Thread connect_and_check = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                setSocket(ip, port);
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line = "";
            adapter = new ChattingAdapter();

            while (true) {
                try {
                    line = networkReader.readLine().toString();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                globalLine = line;

                if(globalLine.split("@")[1].equals("image_send_server_to_client"))
                {
                    String temp_name = globalLine.split("@")[2];
                    String file_name = temp_name.split("\\.")[0] + "_" + file_num + "." + temp_name.split("\\.")[1];
                    System.out.println(file_name);
                    //String file_name = temp_name;

                    String file_path = Environment.getExternalStorageDirectory() + "/" + file_name;
                    File file = new File(file_path);
                    FileOutputStream output = null;

                    byte[] buf = new byte[1024];
                    int total_size = 0;
                    int max_size = Integer.parseInt(globalLine.split("@")[3]);
                    int recv_size = 0;
                    try {
                        output = new FileOutputStream(file);
                        InputStream is = socket.getInputStream();
                        DataInputStream dis = new DataInputStream(is);

                        while ((recv_size = dis.read(buf)) != -1) {
                            total_size += recv_size;
                            output.write(buf, 0, recv_size);
                            output.flush();
                            if (total_size >= max_size) {
                                output.close();
                                break;
                            }
                        }

                    }
                    catch (IOException e) { e.printStackTrace(); }

                    final String inner_name = file_name;
                    file_num++;

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Uri uri = Uri.parse("file:///" + Environment.getExternalStorageDirectory() + "/" + inner_name);
                            Useritem item = new Useritem( globalLine.split("@")[0],uri,"");
                            if(globalLine.split("@")[0].equals("heewon"))
                            {
                                item.setResId(R.drawable.heewon);
                            }
                            else if(globalLine.split("@")[0].equals("sungki"))
                            {
                                item.setResId(R.drawable.sung);
                            }
                            else if(globalLine.split("@")[0].equals("jihye"))
                            {
                                item.setResId(R.drawable.j);
                            }
                            else {
                                item.setResId(-1);
                            }
                            adapter.addItem(item);
                            listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            listView.setSelection(adapter.getCount() - 1);
                        }
                    });

                }
                else if (globalLine.split("@")[1].equals("normal_chatting"))
                { //
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Useritem item = new Useritem( globalLine.split("@")[0],null,globalLine.split("@")[2]);
                            if(globalLine.split("@")[0].equals("heewon"))
                            {
                                item.setResId(R.drawable.heewon);
                            }
                            else if(globalLine.split("@")[0].equals("sungki"))
                            {
                                item.setResId(R.drawable.sung);
                            }
                            else if(globalLine.split("@")[0].equals("jihye"))
                            {
                                item.setResId(R.drawable.j);
                            }
                            else {
                                item.setResId(-1);
                            }

                            adapter.addItem(item);
                            listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
                            listView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            listView.setSelection(adapter.getCount() - 1);


                        }
                    });
                }
                else if (globalLine.split("@")[1].equals("hangeul_result"))
                { //
                    if(globalLine.split("@")[0].equals(myNickName))
                    {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final ArrayList<String> array = new ArrayList<String>();
                                String s = globalLine.split("@")[2];
                                array.add(hangeul_list[Integer.parseInt(s.split("#")[0])]);
                                array.add(hangeul_list[Integer.parseInt(s.split("#")[1])]);
                                array.add(hangeul_list[Integer.parseInt(s.split("#")[2])]);
                                array.add(hangeul_list[Integer.parseInt(s.split("#")[3])]);
                                array.add(hangeul_list[Integer.parseInt(s.split("#")[4])]);

                                final String[] items = new String[5];

                                for (int i = 0; i < 5; i++) {
                                    items[i] = array.get(i);
                                }

                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Chatting.this);
                                alertDialogBuilder.setTitle("원하시는 글자를 선택하세요.");
                                alertDialogBuilder.setItems(items, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //Toast.makeText(getApplicationContext(), items[id] + " 선택했습니다.", Toast.LENGTH_SHORT).show();
                                        editText.setText(editText.getText() + items[id]);
                                    }
                                });
                                AlertDialog alertDialog2 = alertDialogBuilder.create();
                                alertDialog2.show();
                            }
                        });

                    }
                }
            }
        }
    });


    public void setSocket(String ip, int port) throws IOException {

        try {
            socket = new Socket(ip, port);
            networkWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            MyGlobals.getInstance().setSocket(socket);
            MyGlobals.getInstance().setNetworkReader(networkReader);
            MyGlobals.getInstance().setNetworkWriter(networkWriter);

        } catch (IOException e) {
            System.out.println(e);
            e.printStackTrace();
        }

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

