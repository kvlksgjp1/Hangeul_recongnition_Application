package com.example.hongseonggi.chatting_client;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
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
    String []hangeul_list = {"가" ,"각" ,"간" ,"갈" ,"감" ,"갑" ,"강" ,"갖" ,"같" ,"개" ,"객" ,"거" ,"걱" ,"건" ,"걷" ,"걸" ,"검" ,"겁" ,"것" ,"게" ,"겨" ,"격" ,"견" ,"결" ,"경" ,"계" ,"고" ,"곡" ,"곤" ,"곧" ,"골" ,"곳" ,"공" ,"과" ,"관" ,"광" ,"괴" ,"교" ,"구" ,"국" ,"군" ,"굳" ,"궁" ,"권" ,"귀" ,"규" ,"그" ,"극" ,"근" ,"글" ,"금" ,"급" ,"기" ,"긴" ,"길" ,"김" ,"깊" ,"까" ,"깔" ,"깨" ,"꺼" ,"껏" ,"께" ,"꼭" ,"꽃" ,"꾸" ,"끄" ,"끊" ,"끌" ,"끗" ,"끝" ,"끼" ,"나" ,"난" ,"날" ,"남" ,"낮" ,"내" ,"냉" ,"너" ,"널" ,"넓" ,"넘" ,"네" ,"넷" ,"녀" ,"녁" ,"년" ,"념" ,"녕" ,"노" ,"녹" ,"논" ,"놀" ,"농" ,"높" ,"놓" ,"누" ,"눈" ,"뉴" ,"느" ,"늘" ,"능" ,"늦" ,"니" ,"님" ,"다" ,"닥" ,"단" ,"달" ,"닭" ,"담" ,"답" ,"닷" ,"당" ,"대" ,"더" ,"덕" ,"데" ,"도" ,"독" ,"돌" ,"동" ,"되" ,"두" ,"드" ,"득" ,"들" ,"듯" ,"등" ,"디" ,"따" ,"딸" ,"때" ,"뜨" ,"라" ,"락" ,"란" ,"람" ,"랑" ,"랗" ,"래" ,"램" ,"량" ,"러" ,"런" ,"럽" ,"렇" ,"레" ,"려" ,"력" ,"련" ,"령" ,"례" ,"로" ,"록" ,"론" ,"롭" ,"료" ,"루" ,"류" ,"르" ,"른" ,"름" ,"릇" ,"리" ,"린" ,"림" ,"립" ,"마" ,"막" ,"만" ,"많" ,"말" ,"맛" ,"망" ,"맞" ,"맡" ,"매" ,"머" ,"먹" ,"멀" ,"멋" ,"멍" ,"메" ,"면" ,"명" ,"몇" ,"모" ,"목" ,"몰" ,"몸" ,"못" ,"무" ,"문" ,"묻" ,"물" ,"미" ,"민" ,"밀" ,"바" ,"박" ,"반" ,"받" ,"발" ,"밝" ,"밤" ,"밥" ,"방" ,"배" ,"백" ,"버" ,"번" ,"벌" ,"범" ,"법" ,"베" ,"벽" ,"변" ,"별" ,"병" ,"보" ,"복" ,"본" ,"볼" ,"봉" ,"부" ,"북" ,"분" ,"불" ,"붙" ,"비" ,"빌" ,"빗" ,"빛" ,"빠" ,"빨" ,"빼" ,"쁘" ,"사" ,"산" ,"살" ,"삼" ,"상" ,"새" ,"색" ,"생" ,"서" ,"석" ,"선" ,"설" ,"섭" ,"섯" ,"성" ,"세" ,"센" ,"소" ,"속" ,"손" ,"송" ,"쇠" ,"수" ,"숙" ,"순" ,"술" ,"숨" ,"쉬" ,"쉽" ,"스" ,"슬" ,"습" ,"승" ,"시" ,"식" ,"신" ,"실" ,"싫" ,"심" ,"십" ,"싱" ,"싸" ,"쌍" ,"쓰" ,"쓸" ,"씨" ,"씩" ,"아" ,"악" ,"안" ,"앉" ,"알" ,"암" ,"앞" ,"애" ,"액" ,"야" ,"약" ,"양" ,"얘" ,"어" ,"억" ,"언" ,"얼" ,"엄" ,"업" ,"없" ,"엉" ,"에" ,"여" ,"역" ,"연" ,"열" ,"염" ,"영" ,"옆" ,"예" ,"오" ,"옥" ,"온" ,"올" ,"옷" ,"와" ,"완" ,"왕" ,"외" ,"왼" ,"요" ,"욕" ,"용" ,"우" ,"운" ,"울" ,"움" ,"웃" ,"워" ,"원" ,"월" ,"웨" ,"위" ,"유" ,"육" ,"율" ,"으" ,"은" ,"을" ,"음" ,"응" ,"의" ,"이" ,"익" ,"인" ,"일" ,"임" ,"입" ,"있" ,"잊" ,"잎" ,"자" ,"작" ,"잔" ,"잘" ,"잠" ,"잡" ,"장" ,"재" ,"쟁" ,"저" ,"적" ,"전" ,"절" ,"점" ,"접" ,"정" ,"제" ,"져" ,"조" ,"족" ,"존" ,"졸" ,"종" ,"좋" ,"좌" ,"죄" ,"주" ,"죽" ,"준" ,"줄" ,"중" ,"즈" ,"즐" ,"증" ,"지" ,"직" ,"진" ,"질" ,"짐" ,"집" ,"짓" ,"징" ,"짜" ,"짝" ,"째" ,"쩌" ,"쪽" ,"찌" ,"찍" ,"차" ,"착" ,"찬" ,"찰" ,"참" ,"창" ,"찾" ,"채" ,"책" ,"처" ,"척" ,"천" ,"철" ,"첫" ,"청" ,"체" ,"초" ,"촌" ,"총" ,"최" ,"추" ,"축" ,"출" ,"충" ,"취" ,"층" ,"치" ,"친" ,"칠" ,"침" ,"카" ,"커" ,"컵" ,"코" ,"크" ,"큰" ,"키" ,"킬" ,"타" ,"탁" ,"탕" ,"태" ,"택" ,"터" ,"테" ,"토" ,"통" ,"퇴" ,"투" ,"튀" ,"트" ,"특" ,"튼" ,"틀" ,"티" ,"파" ,"판" ,"팔" ,"패" ,"팩" ,"팬" ,"퍼" ,"페" ,"편" ,"평" ,"포" ,"폭" ,"표" ,"풀" ,"품" ,"풍" ,"프" ,"피" ,"필" ,"하" ,"학" ,"한" ,"할" ,"함" ,"합" ,"항" ,"해" ,"행" ,"향" ,"허" ,"험" ,"혀" ,"현" ,"형" ,"호" ,"혼" ,"홍" ,"화" ,"확" ,"환" ,"활" ,"회" ,"효" ,"후" ,"휴" ,"흐" ,"흔" ,"흘" ,"흥" ,"희" ,"히" ,"힘"};

    String globalLine;
    String myNickName;
    Uri photoUri;
    File tempFile;
    String imageString=null;

    private DrawView drawing;
    ListView listView;
    ImageView camera;
    ArrayList<String> Data ;
    ImageView ok;  //???
    EditText editText;
    TextView textView;
    ImageView paint;// ???
    int file_num = 0;
    ChattingAdapter adapter;

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_FROM_ALBUM)
        {
            photoUri = data.getData();

            Cursor cursor = null;
            try {

                /*
                 *  Uri 스키마를
                 *  content:/// 에서 file:/// 로  변경한다.
                 */
                String[] proj = { MediaStore.Images.Media.DATA };

                assert photoUri != null;
                cursor = getContentResolver().query(photoUri, proj, null, null, null);

                assert cursor != null;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                cursor.moveToFirst();

                tempFile = new File(cursor.getString(column_index));

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            String str = tempFile.toString();
            System.out.println(str);
            imageString = str.split("/0/")[1];
            System.out.println(imageString);

            PrintWriter out = new PrintWriter(MyGlobals.getInstance().getNetworkWriter(), true);
            File file = new File(Environment.getExternalStorageDirectory() + "/" + imageString);
            long file_length = file.length();
            String nick_name = MyGlobals.getInstance().getNick_name();
            out.println(nick_name + "@image_send_client_to_server@test_image.jpg@" + String.valueOf(file_length));

            try {
                FileInputStream fis = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(fis);
                DataOutputStream dos = new DataOutputStream(MyGlobals.getInstance().getSocket().getOutputStream());

                int len;
                int total_len = 0;
                int size = 1024;
                byte[] image_data = new byte[size];
                while ((len = dis.read(image_data)) != -1) {
                    total_len += len;
                    dos.write(image_data,0,len);
                    dos.flush();
                    if (total_len >= file_length) {
                        break;
                    }
                }

            }catch (Exception e) { }


            out.println(nick_name + "@image_send_server_to_client@test_image.jpg@");

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        mHandler = new Handler();

        tedPermission();

        camera = (ImageView)findViewById(R.id.camera);//????(????)
        paint = (ImageView) findViewById(R.id.paint); //???
        ok = (ImageView) findViewById(R.id.ok); //?????
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

