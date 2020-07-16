package org.techtown.loginactivity;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SignActivty extends AppCompatActivity {


    EditText NameText, PhoneText, IdText,PasswordText1, PasswordText2;

    TextView textCheck;
    String sname, sphone, sid, spw, spwck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ///////////////////////

        ///////////////////////

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_activty);

        NameText=(EditText)findViewById(R.id.NameText);
        PhoneText=(EditText)findViewById(R.id.PhoneText);
        IdText=(EditText)findViewById(R.id.IdText);
        PasswordText1=(EditText)findViewById(R.id.PasswordText1);
        PasswordText2=(EditText)findViewById(R.id.PasswordText2);


        Button button = findViewById(R.id.BackButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("name", "mike");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        //◀ 버튼 누르면 전 액티비티로 돌아가는 코드




    }

    public void bt_Join(View view)
    {
        textCheck=(TextView)findViewById(R.id.textCheck);
        sname = NameText.getText().toString();
        sphone = PhoneText.getText().toString();
        sid = IdText.getText().toString();
        spw = PasswordText1.getText().toString();
        spwck = PasswordText2.getText().toString();

        if (spw.equals(spwck)) {
            registDB rdb = new registDB();
            rdb.execute();
            textCheck.setText("가입완료");


        } else {
            textCheck.setText("오류");

        }

    }


        public class registDB extends AsyncTask<Void, Integer, Void> {

            @Override
            protected Void doInBackground(Void... unused) {
                /* 인풋 파라메터값 생성 */
                String param = "&u_name=" + sname + "&u_phone=" + sphone + "&u_id=" + sid + "&u_pw=" + spw + "&u_pwck=" + spwck+"";
                try {
                    /* 서버연결 */
                    URL url = new URL(
                            "http://10.210.14.164/snclib_join.php");
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
                    Log.e("RECV DATA", data);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;


            }
        }
    }





