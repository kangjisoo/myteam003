package org.techtown.loginactivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends Activity {
    public static final int REQUEST_CODE_MENU = 101;
    final Context context = this;
    EditText et_id, et_pw;
    CheckBox chk_auto;
    Button btn_login;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    String sId, sPw;


    //연동코드
    ImageView imView;

    //1

    //액티비티 옮겼을때 메세지
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_MENU) {
            Toast.makeText(getApplicationContext(),
                    "onActivityResult 메서드 호출됨. 요청 코드 : " + requestCode +
                            ", 결과 코드 : " + resultCode, Toast.LENGTH_LONG).show();

            if (resultCode == RESULT_OK) {
                String name = data.getStringExtra("name");
                Toast.makeText(getApplicationContext(), "응답으로 전달된 name : " + name, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_id = (EditText) findViewById(R.id.editTextTextEmailAddress);
        et_pw = (EditText) findViewById(R.id.editTextTextPassword);
        chk_auto = (CheckBox) findViewById(R.id.checkBox);  //자동로그인 체크박스
        btn_login = (Button) findViewById(R.id.button4); //로그인버튼

        /*setting = getSharedPreferences("setting", 0);   //자동로그인할때 필요함
        editor = setting.edit();*/

        Button button = findViewById(R.id.SignButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignActivty.class);
                startActivityForResult(intent, REQUEST_CODE_MENU);
            }
        });

       /* if (setting.getBoolean("chk_auto", false)) {
            et_id.setText(setting.getString("ID", ""));
            et_pw.setText(setting.getString("PW", ""));
            chk_auto.setChecked(true);
        }*/
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (chk_auto.isChecked()) { //자동로그인 체크시
                    sId = et_id.getText().toString();
                    sPw = et_pw.getText().toString();
                    editor.putString("ID", sId);
                    editor.putString("PW", sPw);
                    editor.putBoolean("chk_auto", true);
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    MainActivity.this.startActivity(intent);
                } else {    //자동로그인 체크 해제시
                    editor.clear();
                    editor.commit();
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    MainActivity.this.startActivity(intent);
                }*/

                try {
                    sId = et_id.getText().toString();
                    sPw = et_pw.getText().toString();

                } catch (NullPointerException e) {
                    Log.e("err", e.getMessage());
                }

                loginDB lDB = new loginDB();
                lDB.execute();

            }
        });
    }
    public class loginDB extends AsyncTask<Void, Integer, Void> {
        String data = "";
        @Override
        protected Void doInBackground(Void... unused) {

            /* 인풋 파라메터값 생성 */
            String param = "u_id=" + sId + "&u_pw=" + sPw + "";
            try {
                /* 서버연결 */
                URL url = new URL(
                        "http://10.210.14.164/login.php");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.connect();

                /* 안드로이드 -> 서버 파라메터값 전달 */
                OutputStream outs = conn.getOutputStream();
                outs.write(param.getBytes("UTF-8"));
                outs.flush();
                outs.close();

                /* 서버 -> 안드로이드 파라메터값 전달 */
                InputStream is = null;
                BufferedReader in = null;
                String data = "";

                is = conn.getInputStream();
                in = new BufferedReader(new InputStreamReader(is), 8 * 1024);
                String line = null;
                StringBuffer buff = new StringBuffer();
                while ((line = in.readLine()) != null) {
                    buff.append(line + "\n");
                }
                data = buff.toString().trim();

                /* 서버에서 응답 */
                Log.e("RECV DATA", data);

                if (data.equals("0")) {
                    Log.e("RESULT", "성공적으로 처리되었습니다!");
                } else {
                    Log.e("RESULT", "에러 발생! ERRCODE = " + data);
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {

            super.onPostExecute(aVoid);


            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);

            if(data.equals("1"))
            {
                Log.e("RESULT","성공적으로 처리되었습니다!");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("성공적으로 등록되었습니다!")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else if(data.equals("0"))
            {
                Log.e("RESULT","비밀번호가 일치하지 않습니다.");
                alertBuilder
                        .setTitle("알림")
                        .setMessage("비밀번호가 일치하지 않습니다.")
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
            else
            {
                Log.e("RESULT","에러 발생! ERRCODE = " + data);
                alertBuilder
                        .setTitle("알림")
                        .setMessage("등록중 에러가 발생했습니다! errcode : "+ data)
                        .setCancelable(true)
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //finish();
                            }
                        });
                AlertDialog dialog = alertBuilder.create();
                dialog.show();
            }
        }



    }
}