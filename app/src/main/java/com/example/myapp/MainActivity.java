package com.example.myapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.view.View;
import android.graphics.Color;
import android.widget.Button;
import android.widget.TextView;
import android.content.SharedPreferences;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import java.io.CharConversionException;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private EditText Password;
    private Button SendButton;

     String HOST = "148.70.246.45:10000";
     String LOGURL="http://"+HOST+"/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Password = (EditText) findViewById(R.id.password);
        SendButton = (Button) findViewById(R.id.button);
    }

    public void SendMessage(View view){

        EditText password = (EditText) findViewById(R.id.password);
        final String content = password.getText().toString();
        CheckFromServer(content);
        System.out.print(LOGURL);
    }

    private void CheckFromServer(final String password){
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder fromBuilder = new FormBody.Builder();
        fromBuilder.add("password", password);
        Request request = new Request.Builder().url(LOGURL).post(fromBuilder.build()).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView content = (TextView) findViewById(R.id.log);
                        content.setText("wrong server");
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String res = response.body().string();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (res.equals("0")){
                            showWarnSweetDialog("Success");
                        }
                        if (res.equals("1")){
                            showWarnSweetDialog("go");
                        }
                    }
                });
            }
        });
    }

    private void showWarnSweetDialog(String info)
    {
        SweetAlertDialog pDialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(info);
        pDialog.setCancelable(true);
        pDialog.show();
    }
}
