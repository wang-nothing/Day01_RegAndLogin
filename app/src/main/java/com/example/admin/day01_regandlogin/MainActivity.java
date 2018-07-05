package com.example.admin.day01_regandlogin;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.admin.day01_regandlogin.Bean.LoginBean;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private EditText tv_mobile;
    private EditText tv_password;
    private Button btn_login;
    private Button btn_reg;
    private OkHttpClient client;
    private Editable mobile;
    private Editable password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)
                .readTimeout(5000, TimeUnit.MILLISECONDS)
                .writeTimeout(5000, TimeUnit.MILLISECONDS)
                .build();
    }

    private void initViews() {
        tv_mobile = findViewById(R.id.tv_mobile);
        tv_password = findViewById(R.id.tv_password);
        btn_login = findViewById(R.id.btn_login);
        btn_reg = findViewById(R.id.btn_reg);

        mobile = tv_mobile.getText();
        password = tv_password.getText();
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url_login = "http://120.27.23.105/user/login?mobile="+mobile+"&password="+password;
                Request request = new Request.Builder()
                        .get()
                        .url(url_login)
                        .build();

                final Call call = client.newCall(request);

                Log.i("TAG", "onClick: "+url_login);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        call.enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.e("TAG", "onFailure: "+"shibai");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                if (response != null && response.isSuccessful()){
                                    String json = response.body().string();
                                    Log.e("TAG", "onResponse: "+json );
                                    LoginBean loginBean = new Gson().fromJson(json, LoginBean.class);
                                    if("0".equalsIgnoreCase(loginBean.getCode())){
                                        Toast.makeText(MainActivity.this,"成功",Toast.LENGTH_SHORT).show();
                                        //阿斯顿撒旦撒
                                    }
                                }
                            }
                        });
                    }
                }).start();
            }
        });
        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegActivity.class);
                startActivity(intent);
            }
        });
    }
}
